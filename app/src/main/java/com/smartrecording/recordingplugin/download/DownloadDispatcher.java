package com.smartrecording.recordingplugin.download;

import android.net.Uri;
import android.os.Process;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.smartrecording.recordingplugin.app.MyApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.BlockingQueue;

import static com.smartrecording.recordingplugin.download.Utils.HTTP_OK;
import static com.smartrecording.recordingplugin.download.Utils.HTTP_PARTIAL;


/**
 * This class used to dispatch downloader, this is desinged according to NetworkDispatcher in
 * Android-Volley.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class DownloadDispatcher extends Thread {
    private static final int SLEEP_BEFORE_DOWNLOAD = 500;
    private static final int BUFFER_SIZE = 4096;
    private static final int END_OF_STREAM = -1;
    private static final String DEFAULT_THREAD_NAME = "DownloadDispatcher";
    private static final String IDLE_THREAD_NAME = "DownloadDispatcher-Idle";
    private static final String TAG = "DownloadDispatcher";

    private final BlockingQueue<DownloadRequest> queue;
    private final DownloadDelivery delivery;
    private final Logger logger;
    private long lastProgressTimestamp;
    private volatile boolean quit = false;

    /**
     * Default constructor, with queue and delivery.
     *
     * @param queue    a {@link BlockingQueue} with {@link DownloadRequest}
     * @param delivery {@link DownloadDelivery}
     * @param logger   {@link Logger}
     */
    public DownloadDispatcher(BlockingQueue<DownloadRequest> queue, DownloadDelivery delivery,
                              Logger logger) {
        this.queue = queue;
        this.delivery = delivery;
        this.logger = logger;

        /* set thread name to idle */
        setName(IDLE_THREAD_NAME);
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        DownloadRequest request = null;

        while (true) {
            try {
                setName(IDLE_THREAD_NAME);
                request = queue.take();
                logger.log("A new download request taken, download id: " + request.downloadId());
                sleep(SLEEP_BEFORE_DOWNLOAD);
                setName(DEFAULT_THREAD_NAME);

                /* start download */
                executeDownload(request);
            } catch (InterruptedException e) {
                /* we may have been interrupted because it was time to quit */
                if (quit) {
                    if (request != null) {
                        request.finish();
                    }

                    return;
                }
            }
        }
    }

    /* update download state */
    private void updateState(DownloadRequest request, DownloadState state) {
        request.updateDownloadState(state);
    }

    /* update download start state */
    private void updateStart(DownloadRequest request, long totalBytes) {
        /* if the request has failed before, donnot deliver callback */
        if (request.downloadState() == DownloadState.FAILURE) {
            updateState(request, DownloadState.RUNNING);
            return;
        }

        /* set the download state of this request as running */
        updateState(request, DownloadState.RUNNING);
        delivery.postStart(request, totalBytes);
    }

    /* update download retrying */
    private void updateRetry(DownloadRequest request) {
        delivery.postRetry(request);
    }

    /* update download progress */
    private void updateProgress(DownloadRequest request, long bytesWritten, long totalBytes, double aaa) {
        long currentTimestamp = System.currentTimeMillis();
        if (bytesWritten != totalBytes
                && currentTimestamp - lastProgressTimestamp < request.progressInterval()) {
            return;
        }

        /* save progress timestamp */
        lastProgressTimestamp = currentTimestamp;

        if (!request.isCanceled()) {
            delivery.postProgress(request, bytesWritten, totalBytes, aaa);
        }
    }

    /* update download success */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void updateSuccess(
            DownloadRequest request) {
        updateState(request, DownloadState.SUCCESSFUL);

        /* notify the request download finish */
        request.finish();

        File file = new File(request.destinationFilePath());
        if (file.exists()) {
            //  file.renameTo(new File(request.destinationFilePath()));
        }

        /* deliver success message */
        delivery.postSuccess(request);
    }

    /* update download failure */
    private void updateFailure(DownloadRequest request, int statusCode, String errMsg) {
        updateState(request, DownloadState.FAILURE);

        /* if the status code is 0, may be cause by the net error */
        int leftRetryTime = request.retryTime();
        if (leftRetryTime >= 0) {
            try {
                /* sleep a while before retrying */
                sleep(request.retryInterval());
            } catch (InterruptedException e) {
                /* we may have been interrupted because it was time to quit */
                if (quit) {
                    request.finish();
                    return;
                }
            }

            /* retry downloading */
            if (!request.isCanceled()) {
                logger.log("Retry DownloadRequest: "
                        + request.downloadId()
                        + " left retry time: "
                        + leftRetryTime);
                updateRetry(request);
                executeDownload(request);
            }

            return;
        }

        /* notify the request that downloading has finished */
        request.finish();

        /* deliver failure message */
        delivery.postFailure(request, statusCode, errMsg);
    }

    OutputStream outputStream;
    boolean isext = false;

    /* execute downloading */
    private void executeDownload(DownloadRequest request) {
        Log.e(TAG, "executeDownload: called");
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        Downloader downloader = request.downloader();
        RandomAccessFile raf = null;
        InputStream is = null;

        try {
            if (request.destinationFilePath() == null) {
                request.updateDestinationFilePath(downloader.detectFilename(request.uri()));
            }
            if (!request.destinationFilePath().contains("emulated")) {
                isext = true;
                String external_url = MyApplication.getInstance().getPrefManager().
                        getExternalStorageUri();
                DocumentFile document = DocumentFile.fromTreeUri(MyApplication.getContext(),
                        Uri.parse(external_url));
                if (document != null) {
                    try {
                        Log.e(TAG, "backgroundTask: can write?:" + document.canWrite());
                        Log.e(TAG, "backgroundTask: file_name:" + request.filenames());
                        DocumentFile apkFile = document.createFile("video/MP2T",
                                request.filenames());
                        //com.purple.iptv.player.utils.UtilMethods.LogMethod("app1234_apkFile", String.valueOf(apkFile));
                        Log.e(TAG, "executeDownload: " + (apkFile == null ? "its null" : "not null"));
                        if (apkFile != null) {
                            outputStream = MyApplication.getContext().getContentResolver().
                                    openOutputStream(apkFile.getUri());
                        } else {

                            Log.e(TAG, "backgroundTask: apkfile is null");
                            //    Toast.makeText(mContext, "Something went wrong please change directory", Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {
                        Log.e(TAG, "executeDownload: catch:" + e.getMessage());
                        logger.log(e.getMessage());


                    }
                }
            } else {
                isext = false;
            }
            File file;
            boolean fileExsits;
            long breakpoint = 100;
            long bytesWritten = 0;
            int statusCode = 0;
            if (!isext) {
                file = new File(request.destinationFilePath());
                fileExsits = file.exists();
                raf = new RandomAccessFile(file, "rw");
                breakpoint = file.length();


                if (fileExsits) {
                    /* set the range to continue the downloading */
                    raf.seek(breakpoint);
                    bytesWritten = breakpoint;
                    logger.log(
                            "Detect existed file with " + breakpoint + " bytes, start breakpoint downloading");
                }


            }
            statusCode = downloader.start(request.uri(), breakpoint);

            is = downloader.byteStream();
            if (statusCode != HTTP_OK && statusCode != HTTP_PARTIAL) {
                logger.log("Incorrect http code got: " + statusCode);
                throw new DownloadException(statusCode, "download fail");
            }

            long contentLength = downloader.contentLength();
            if (contentLength <= 0 && is == null) {
                throw new DownloadException(statusCode, "content length error");
            }
            boolean noContentLength = contentLength <= 0;
            contentLength += bytesWritten;

            updateStart(request, contentLength);
            logger.log("Start to download, content length: " + contentLength + " bytes");

            if (is != null) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                long total = 0;
                long startTime = System.currentTimeMillis();
                long endTime1 = 0;
                double downloadElapsedTime = 0;
                double spwe = 0;
                while (true) {

                    /* if the request has canceld, stop the downloading */
                    if (Thread.currentThread().isInterrupted() || request.isCanceled()) {
                        request.finish();
                        return;
                    }
                    Log.e(TAG, "executeDownload: System.currentTimeMillis() :" + System.currentTimeMillis());
                    Log.e(TAG, "executeDownload: request.endtime() :" + request.endtime());
                    if (request.endtime() != -1) {
                        if (System.currentTimeMillis() >= request.endtime()) {
                            Log.e(TAG, "executeDownload: time over:");
                            updateSuccess(request);
                            return;
                        }
                    } else {
                        throw new DownloadException(statusCode, "end time given");

                    }

                    /* if current is not wifi and mobile network is not allowed, stop */
                    if (request.allowedNetworkTypes() != 0
                            && !Utils.isWifi(request.context())
                            && (request.allowedNetworkTypes() & DownloadRequest.NETWORK_MOBILE) == 0) {
                        throw new DownloadException(statusCode, "allowed network error");
                    }

                    /* read data into buffer from input stream */
                    length = readFromInputStream(buffer, is);
                    long fileSize = raf.length();
                    long totalBytes = noContentLength ? fileSize : contentLength;

                    if (length == END_OF_STREAM) {
                        updateSuccess(request);
                        return;
                    } else if (length == Integer.MIN_VALUE) {
                        throw new DownloadException(statusCode, "transfer data error");
                    }

                    bytesWritten += length;
                    total += length;
                    /* write buffer into local file */
                    if (!isext) {

                        raf.write(buffer, 0, length);
                    } else {
                        outputStream.write(buffer, 0, length);
                    }

                    /* deliver progress callback */
                    if (total > 0) {
                        endTime1 = System.currentTimeMillis();
                        downloadElapsedTime = (endTime1 - startTime) / 1000.0;
                        spwe = setInstantDownloadRate(total, downloadElapsedTime);
                    }

                    updateProgress(request, bytesWritten, totalBytes, spwe);
                }
            } else {
                throw new DownloadException(statusCode, "input stream error");
            }
        } catch (IOException e) {
            Log.e(TAG, "executeDownload: catch" + e.getMessage());
            logger.log("Caught new exception: " + e.getMessage());

            if (e instanceof DownloadException) {
                DownloadException exception = (DownloadException) e;
                updateFailure(request, exception.getCode(), exception.getMessage());
            } else {
                updateFailure(request, 0, e.getMessage());
            }
        } finally {
            downloader.close();
            silentCloseFile(raf);
            silentCloseInputStream(is);
            silentCloseOutputStream(outputStream);
        }
    }

    private void silentCloseOutputStream(OutputStream outputStream) {
        try {

            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException ignore) {
        }
    }

    double instantDownloadRate = 0;

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd;
        try {
            bd = new BigDecimal(value);
        } catch (Exception ex) {
            return 0.0;
        }
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double setInstantDownloadRate(long downloadedByte, double elapsedTime) {

        if (downloadedByte >= 0) {
            this.instantDownloadRate = round((Double) (((downloadedByte * 8) / (1000 * 1000)) / elapsedTime), 2);
        } else {
            this.instantDownloadRate = 0.0;
        }
        return instantDownloadRate;
    }

    /* read data from input stream */
    int readFromInputStream(byte[] buffer, InputStream is) {
        try {
            return is.read(buffer);
        } catch (IOException e) {
            return Integer.MIN_VALUE;
        }
    }

    /* a utility function to close a random access file without raising an exception */
    static void silentCloseFile(RandomAccessFile raf) {
        if (raf != null) {
            try {
                raf.close();
            } catch (IOException ignore) {
            }
        }
    }

    /* a utility function to close an input stream without raising an exception */
    static void silentCloseInputStream(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ignore) {
        }
    }

    /**
     * Forces this dispatcher to quit immediately. If any download requests are still in
     * the queue, they are not guaranteed to be processed.
     */
    void quit() {
        logger.log("Download dispatcher quit");
        quit = true;

        /* interrupt current thread */
        interrupt();
    }
}
