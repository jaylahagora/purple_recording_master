package com.smartrecording.recordingplugin.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import jcifs.smb.SmbException;

/**
 * Created by arpitkh996 on 13-01-2016, modified by Emmanuel Messulam<emmanuelbendavid@gmail.com>
 */
public class Operations {

    // reserved characters by OS, shall not be allowed in file names
    private static final String FOREWARD_SLASH = "/";
    private static final String BACKWARD_SLASH = "\\";
    private static final String COLON = ":";
    private static final String ASTERISK = "*";
    private static final String QUESTION_MARK = "?";
    private static final String QUOTE = "\"";
    private static final String GREATER_THAN = ">";
    private static final String LESS_THAN = "<";

    private static final String FAT = "FAT";
    private static final String TAG = "Operations";

    public interface ErrorCallBack {

        /**
         * Callback fired when file being created in process already exists
         */
        void exists(HybridFile file);

        /**
         * Callback fired when creating new file/directory and required storage access framework permission
         * to access SD Card is not available
         */
        void launchSAF(HybridFile file);

        /**
         * Callback fired when renaming file and required storage access framework permission to access
         * SD Card is not available
         */
        void launchSAF(HybridFile file, HybridFile file1);

        /**
         * Callback fired when we're done processing the operation
         *
         * @param b defines whether operation was successful
         */
        void done(HybridFile hFile, boolean b);

        /**
         * Callback fired when an invalid file name is found.
         */
        void invalidName(HybridFile file);
    }



    public static void mkfile(@NonNull final HybridFile file, final Context context, final boolean rootMode,
                              @NonNull final ErrorCallBack errorCallBack) {

        new AsyncTask<Void, Void, Void>() {



            @Override
            protected Void doInBackground(Void... params) {
                // check whether filename is valid or not
                if (!Operations.isFileNameValid(file.getName(context))) {
                    errorCallBack.invalidName(file);
                    Log.e(TAG, "doInBackground: ...............1" );
                    return null;
                }

                if (file.exists()) {
                    errorCallBack.exists(file);
                    Log.e(TAG, "doInBackground: ...............2" );
                    return null;
                }

                if (file.isSmb()) {
                    try {
                        file.getSmbFile(2000).createNewFile();
                    } catch (SmbException e) {
                        e.printStackTrace();
                        errorCallBack.done(file, false);
                        Log.e(TAG, "doInBackground: ...............3" );
                        return null;
                    }
                    Log.e(TAG, "doInBackground: ...............4" );
                    errorCallBack.done(file, file.exists());
                    return null;
                }  else if (file.isOtgFile()) {

                    // first check whether new file already exists
                    DocumentFile fileToCreate = OTGUtil.getDocumentFile(file.getPath(), context, false);
                    if (fileToCreate != null) errorCallBack.exists(file);

                    DocumentFile parentDirectory = OTGUtil.getDocumentFile(file.getParent(), context, false);
                    if (parentDirectory.isDirectory()) {
                        parentDirectory.createFile(file.getName(context).substring(file.getName().lastIndexOf(".")),
                                file.getName(context));
                        errorCallBack.done(file, true);
                    } else errorCallBack.done(file, false);
                    return null;
                } else {
                    Log.e(TAG, "doInBackground: ...............5" );
                    if (file.isLocal() || file.isRoot()) {
                        Log.e(TAG, "doInBackground: ...............6" );
                        int mode = checkFolder(new File(file.getParent()), context);
                        if (mode == 2) {
                            errorCallBack.launchSAF(file);
                            return null;
                        }
                        if (mode == 1 || mode == 0)
                            FileUtil.mkfile(file.getFile(), context);
                        if (!file.exists() && rootMode) {
//                            file.setMode(OpenMode.ROOT);
//                            if (file.exists()) errorCallBack.exists(file);
//                            try {
//
//                                RootUtils.mkFile(file.getPath());
//                            } catch (ShellNotRunningException e) {
//                                e.printStackTrace();
//                            }
//                            errorCallBack.done(file, file.exists());
                            return null;
                        }
                        errorCallBack.done(file, file.exists());
                        return null;
                    }
                    errorCallBack.done(file, file.exists());
//

                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private static int checkFolder(final File folder, Context context) {
        Log.e(TAG, "checkFolder: ............1" );
        boolean lol = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        if (lol) {
            Log.e(TAG, "checkFolder: ............2" );
            boolean ext = FileUtil.isOnExtSdCard(folder, context);
            Log.e(TAG, "checkFolder: ............2.1:"+ext );
            if (ext) {
                Log.e(TAG, "checkFolder: ............3" );
                if (!folder.exists() || !folder.isDirectory()) {
                    Log.e(TAG, "checkFolder: ............4" );
                    return 0;
                }

                // On Android 5, trigger storage access framework.
                if (!FileUtil.isWritableNormalOrSaf(folder, context)) {
                    Log.e(TAG, "checkFolder: ............5" );
                    return 2;
                }
                Log.e(TAG, "checkFolder: ............6" );
                return 1;
            }else {
                Log.e(TAG, "checkFolder: ............2.2 else:"+ext );
            }
        } else if (Build.VERSION.SDK_INT == 19) {
            // Assume that Kitkat workaround works
            if (FileUtil.isOnExtSdCard(folder, context)) return 1;

        }
        Log.e(TAG, "checkFolder: ............7" );
        // file not on external sd card
        if (FileUtil.isWritable(new File(folder, "DummyFile"))) {
            return 1;
        } else {
            return 0;
        }
    }


    /**
     * Well, we wouldn't want to copy when the target is inside the source
     * otherwise it'll end into a loop
     *
     * @return true when copy loop is possible
     */
    public static boolean isCopyLoopPossible(HybridFileParcelable sourceFile, HybridFile targetFile) {
        return targetFile.getPath().contains(sourceFile.getPath());
    }

    /**
     * Validates file name
     * special reserved characters shall not be allowed in the file names on FAT filesystems
     *
     * @param fileName the filename, not the full path!
     * @return boolean if the file name is valid or invalid
     */
    public static boolean isFileNameValid(String fileName) {
        //String fileName = builder.substring(builder.lastIndexOf("/")+1, builder.length());

        // TODO: check file name validation only for FAT filesystems
        return !(fileName.contains(ASTERISK) || fileName.contains(BACKWARD_SLASH) ||
                fileName.contains(COLON) || fileName.contains(FOREWARD_SLASH) ||
                fileName.contains(GREATER_THAN) || fileName.contains(LESS_THAN) ||
                fileName.contains(QUESTION_MARK) || fileName.contains(QUOTE));
    }

    private static boolean isFileSystemFAT(String mountPoint) {
        String[] args = new String[]{"/bin/bash", "-c", "df -DO_NOT_REPLACE | awk '{print $1,$2,$NF}' | grep \"^"
                + mountPoint + "\""};
        try {
            Process proc = new ProcessBuilder(args).start();
            OutputStream outputStream = proc.getOutputStream();
            String buffer = null;
            outputStream.write(buffer.getBytes());
            return buffer != null && buffer.contains(FAT);
        } catch (IOException e) {
            e.printStackTrace();
            // process interrupted, returning true, as a word of cation
            return true;
        }
    }
}
