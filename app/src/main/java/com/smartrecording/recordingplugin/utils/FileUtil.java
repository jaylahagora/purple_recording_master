/*
 * FileUtil.java
 *
 * Copyright (C) 2015-2018 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>,
 * Emmanuel Messulam<emmanuelbendavid@gmail.com>, Raymond Lai <airwave209gt at gmail.com> and Contributors.
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.smartrecording.recordingplugin.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import android.util.Log;


import com.xunison.recordingplugin.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility class for helping parsing file systems.
 */
public abstract class FileUtil {

    private static final String LOG = "AmazeFileUtils";

    private static final Pattern FILENAME_REGEX = Pattern.compile("[\\\\\\/:\\*\\?\"<>\\|\\x01-\\x1F\\x7F]", Pattern.CASE_INSENSITIVE);
    private static final String TAG = "FileUtil";

    /**
     * Determine the camera folder. There seems to be no Android API to work for real devices, so this is a best guess.
     *
     * @return the default camera folder.
     **/
    //TODO the function?

    /**
     * Copy a file. The target file may even be on external SD card for Kitkat.
     *
     * @param source The source file
     * @param target The target file
     * @return true if the copying was successful.
     */
    @SuppressWarnings("null")
  /*  private static boolean copyFile(final File source, final File target, Context context) {
        FileInputStream inStream = null;
        OutputStream outStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inStream = new FileInputStream(source);

            // First try the normal way
            if (isWritable(target)) {
                // standard way
                outStream = new FileOutputStream(target);
                inChannel = inStream.getChannel();
                outChannel = ((FileOutputStream) outStream).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Storage Access Framework
                    DocumentFile targetDocument = getDocumentFile(target, false, context);
                    outStream =
                            context.getContentResolver().openOutputStream(targetDocument.getUri());
                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    // Workaround for Kitkat ext SD card
                    Uri uri = MediaStoreHack.getUriFromFile(target.getAbsolutePath(), context);
                    outStream = context.getContentResolver().openOutputStream(uri);
                } else {
                    return false;
                }

                if (outStream != null) {
                    // Both for SAF and for Kitkat, write to output stream.
                    byte[] buffer = new byte[16384]; // MAGIC_NUMBER
                    int bytesRead;
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                }

            }
        } catch (Exception e) {
            Log.e(LOG,
                    "Error when copying file from " + source.getAbsolutePath() + " to " + target.getAbsolutePath(), e);
            return false;
        } finally {
            try {
                inStream.close();
            } catch (Exception e) {
                // ignore exception
            }

            try {
                outStream.close();
            } catch (Exception e) {
                // ignore exception
            }

            try {
                inChannel.close();
            } catch (Exception e) {
                // ignore exception
            }

            try {
                outChannel.close();
            } catch (Exception e) {
                // ignore exception
            }
        }
        return true;
    }*/

    /*public static OutputStream getOutputStream(final File target, Context context) throws FileNotFoundException {
        OutputStream outStream = null;
        // First try the normal way
        if (isWritable(target)) {
            // standard way
            outStream = new FileOutputStream(target);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Storage Access Framework
                DocumentFile targetDocument = getDocumentFile(target, false, context);
                if (targetDocument == null) return null;
                outStream = context.getContentResolver().openOutputStream(targetDocument.getUri());
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                // Workaround for Kitkat ext SD card
                return MediaStoreHack.getOutputStream(context, target.getPath());
            }
        }
        return outStream;
    }*/


    /**
     * Delete a file. May be even on external SD card.
     *
     * @param file the file to be deleted.
     * @return True if successfully deleted.
     */
    static boolean deleteFile(@NonNull final File file, Context context) {
        // First try the normal deletion.
        if (file == null) return true;
        boolean fileDelete = rmdir(file, context);
        if (file.delete() || fileDelete)
            return true;

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && FileUtil.isOnExtSdCard(file, context)) {

            DocumentFile document = getDocumentFile(file, false, context);
            return document.delete();
        }

        // Try the Kitkat workaround.
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ContentResolver resolver = context.getContentResolver();

            try {
                Uri uri = MediaStoreHack.getUriFromFile(file.getAbsolutePath(), context);
                resolver.delete(uri, null, null);
                return !file.exists();
            } catch (Exception e) {
                Log.e(LOG, "Error when deleting file " + file.getAbsolutePath(), e);
                return false;
            }
        }

        return !file.exists();
    }


//    public static boolean mkdir(final File file, Context context) {
//        if(file==null)
//            return false;
//        if (file.exists()) {
//            // nothing to create.
//            return file.isDirectory();
//        }
//
//        // Try the normal way
//        if (file.mkdirs()) {
//            return true;
//        }
//
//        // Try with Storage Access Framework.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && FileUtil.isOnExtSdCard(file, context)) {
//            DocumentFile document = getDocumentFile(file, true, context);
//            // getDocumentFile implicitly creates the directory.
//            return document.exists();
//        }
//
//        // Try the Kitkat workaround.
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
//            try {
//                return MediaStoreHack.mkdir(context, file);
//            } catch (IOException e) {
//                return false;
//            }
//        }
//
//        return false;
//    }

//    public static boolean mkdirs(Context context, HybridFile file) {
//        boolean isSuccessful = true;
//        switch (file.mode) {
//            case SMB:
//                try {
//                    SmbFile smbFile = new SmbFile(file.getPath());
//                    smbFile.mkdirs();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    isSuccessful =  false;
//                } catch (SmbException e) {
//                    e.printStackTrace();
//                    isSuccessful = false;
//                }
//                break;
//            case OTG:
//                DocumentFile documentFile = OTGUtil.getDocumentFile(file.getPath(), context, true);
//                isSuccessful = documentFile != null;
//                break;
//            case FILE:
//                isSuccessful = mkdir(new File(file.getPath()), context);
//                break;
//            default:
//                isSuccessful = true;
//                break;
//        }
//
//        return isSuccessful;
//    }

    public static boolean mkfile(final File file, Context context) {
        if (file == null)
            return false;
        if (file.exists()) {
            // nothing to create.
            return !file.isDirectory();
        }

        // Try the normal way
        try {
            if (file.createNewFile()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && FileUtil.isOnExtSdCard(file, context)) {
            DocumentFile document = getDocumentFile(file.getParentFile(), true, context);
            // getDocumentFile implicitly creates the directory.
            try {
                return document.createFile(MimeTypes.getMimeType(file.getPath(), file.isDirectory()), file.getName()) != null;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            try {
                return MediaStoreHack.mkfile(context, file);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Delete a folder.
     *
     * @param file The folder name.
     * @return true if successful.
     */
    private static boolean rmdir(@NonNull final File file, Context context) {
        if (!file.exists()) return true;

        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File child : files) {
                rmdir(child, context);
            }
        }

        // Try the normal way
        if (file.delete()) {
            return true;
        }

        // Try with Storage Access Framework.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DocumentFile document = getDocumentFile(file, true, context);
            if (document != null && document.delete()) {
                return true;
            }
        }

        // Try the Kitkat workaround.
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Delete the created entry, such that content provider will delete the file.
            resolver.delete(MediaStore.Files.getContentUri("external"), MediaStore.MediaColumns.DATA + "=?",
                    new String[]{file.getAbsolutePath()});
        }

        return !file.exists();
    }

    /**
     * Check if a file is readable.
     *
     * @param file The file
     * @return true if the file is reabable.
     */
    public static boolean isReadable(final File file) {
        if (file == null)
            return false;
        if (!file.exists()) return false;

        boolean result;
        try {
            result = file.canRead();
        } catch (SecurityException e) {
            return false;
        }

        return result;
    }

    /**
     * Check if a file is writable. Detects write issues on external SD card.
     *
     * @param file The file
     * @return true if the file is writable.
     */
    public static boolean isWritable(final File file) {
        Log.e(TAG, "isWritable: ........1");
        if (file == null) {
            Log.e(TAG, "isWritable: ........2");
            return false;
        }
        boolean isExisting = file.exists();

        try {
            Log.e(TAG, "isWritable: ........3");
            FileOutputStream output = new FileOutputStream(file, true);
            try {
                Log.e(TAG, "isWritable: ........4");
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "isWritable: ........5:" + e.getMessage());
                // do nothing.
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "isWritable: ........6" + e.getMessage());
            e.printStackTrace();
            return false;
        }
        boolean result = file.canWrite();
        Log.e(TAG, "isWritable: ........7:file.canWrite()?:" + result);
        // Ensure that file is not created during this process.
        if (!isExisting) {
            Log.e(TAG, "isWritable: ........8");
            file.delete();
        }
        Log.e(TAG, "isWritable: ........9");
        return result;
    }

    // Utility methods for Android 5

    /**
     * Check for a directory if it is possible to create files within this directory, either via normal writing or via
     * Storage Access Framework.
     *
     * @param folder The directory
     * @return true if it is possible to write in this directory.
     */
    public static boolean isWritableNormalOrSaf(final File folder, Context c) {
        Log.e(TAG, "isWritableNormalOrSaf: ...........1");
        // Verify that this is a directory.
        if (folder == null) {
            Log.e(TAG, "isWritableNormalOrSaf: ...........2");
            return false;
        }
        if (!folder.exists() || !folder.isDirectory()) {
            Log.e(TAG, "isWritableNormalOrSaf: ...........3");
            return false;
        }

        // Find a non-existing file in this directory.
        int i = 0;
        File file;
        Log.e(TAG, "isWritableNormalOrSaf: ...........4");
        do {
            String fileName = "AugendiagnoseDummyFile" + (++i);
            file = new File(folder, fileName);
            Log.e(TAG, "isWritableNormalOrSaf: ...........5");
        } while (file.exists());

        // First check regular writability
        if (isWritable(file)) {
            Log.e(TAG, "isWritableNormalOrSaf: ...........6");
            return true;
        }

        // Next check SAF writability.
        DocumentFile document = getDocumentFile(file, false, c);

        if (document == null) {
            Log.e(TAG, "isWritableNormalOrSaf: ...........7");
            return false;
        }
        Log.e(TAG, "isWritableNormalOrSaf: ...........8");
        // This should have created the file - otherwise something is wrong with access URL.
        boolean result = document.canWrite() && file.exists();
        Log.e(TAG, "isWritableNormalOrSaf: ...........9: result?:" + result);
        // Ensure that the dummy file is not remaining.
        deleteFile(file, c);
        Log.e(TAG, "isWritableNormalOrSaf: ...........10");
        return result;
    }

    /**
     * Get a list of external SD card paths. (Kitkat or higher.)
     *
     * @return A list of external SD card paths.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String[] getExtSdCardPaths(Context context) {
        Log.e(TAG, "getExtSdCardPaths: ..........1");
        List<String> paths = new ArrayList<>();
        for (File file : context.getExternalFilesDirs("external")) {

            if (file != null && !file.equals(context.getExternalFilesDir("external"))) {
                try {
                    Log.e(TAG, "getExtSdCardPaths: ..........2+:" + file.getName());
                    Log.e(TAG, "getExtSdCardPaths: ..........2.1+:" + file.getCanonicalPath());
                } catch (Exception e) {
                    Log.e(TAG, "getExtSdCardPaths: catch:" + e.getMessage());
                }

                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    Log.e(TAG, "getExtSdCardPaths: ..........2.1:error :");
                    Log.w(LOG, "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    } catch (IOException e) {
                        Log.e(TAG, "getExtSdCardPaths: ..........3:" + e.getMessage());
                        // Keep non-canonical path.
                    }
                    Log.e(TAG, "getExtSdCardPaths: ..........4");
                    paths.add(path);
                }
            } else {
                if (file != null) {
                    try {
                        Log.e(TAG, "getExtSdCardPaths: .......... else 2.1+:" + file.getAbsolutePath());
                        Log.e(TAG, "getExtSdCardPaths: .......... else 2.1+:" + file.getCanonicalPath());
                    } catch (Exception e) {
                        Log.e(TAG, "getExtSdCardPaths: else catch :" + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "getExtSdCardPaths: file is null");
                }

            }
        }
        try {
            File rootFile = new File("/storage");
            if (rootFile.exists()) {
                File[] files = rootFile.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().equals("self") || file.getName().equals("emulated")) {
                            continue;
                        }
                        if (file.getAbsolutePath().startsWith("/storage/udisk")) {
                            if (Environment.getStorageState(new File(file.getAbsolutePath())).equals("mounted")) {
                                Log.e(TAG, "getRootStorageList: mounted:" + file.getAbsolutePath());
                                paths.add(file.getAbsolutePath());
                            }
                        } else {
                            Log.e(TAG, "getRootStorageList: not mounted:" + file.getAbsolutePath());
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "getExtSdCardPaths: catch:" + e.getMessage());
        }
        if (paths.isEmpty()) {
            Log.e(TAG, "getExtSdCardPaths: ..........5");
            paths.add("/storage/sdcard1");

        }
        return paths.toArray(new String[0]);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String[] getExtSdCardPathsForActivity(Context context) {
        List<String> paths = new ArrayList<>();
        for (File file : context.getExternalFilesDirs("external")) {
            if (file != null) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    Log.w(LOG, "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    } catch (IOException e) {
                        // Keep non-canonical path.
                    }
                    paths.add(path);
                }
            }
        }
        if (paths.isEmpty()) paths.add("/storage/sdcard1");
        return paths.toArray(new String[0]);
    }

    /**
     * Determine the main folder of the external SD card containing the given file.
     *
     * @param file the file.
     * @return The main folder of the external SD card containing this file, if the file is on an SD card. Otherwise,
     * null is returned.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getExtSdCardFolder(final File file, Context context) {
        Log.e(TAG, "getExtSdCardFolder: ..........1");
        String[] extSdPaths = getExtSdCardPaths(context);
        for (String s : extSdPaths) {
            Log.e(TAG, "getExtSdCardFolder: list:" + s);
        }
        Log.e(TAG, "getExtSdCardFolder: ..........1.1:" + extSdPaths.length);

        try {
            for (int i = 0; i < extSdPaths.length; i++) {
                Log.e(TAG, "getExtSdCardFolder: ..........2:" + file.getCanonicalPath());
                if (file.getCanonicalPath().startsWith(extSdPaths[i])) {
                    Log.e(TAG, "getExtSdCardFolder: ..........2.1");
                    return extSdPaths[i];
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "getExtSdCardFolder: ..........3");
            return null;
        }
        Log.e(TAG, "getExtSdCardFolder: ..........4");
        return null;
    }

    /**
     * Determine if a file is on external sd card. (Kitkat or higher.)
     *
     * @param file The file.
     * @return true if on external sd card.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isOnExtSdCard(final File file, Context c) {
        return getExtSdCardFolder(file, c) != null;
    }

    /**
     * Get a DocumentFile corresponding to the given file (for writing on ExtSdCard on Android 5). If the file is not
     * existing, it is created.
     *
     * @param file        The file.
     * @param isDirectory flag indicating if the file should be a directory.
     * @return The DocumentFile
     */
    public static DocumentFile getDocumentFile(final File file, final boolean isDirectory, Context context) {
        Log.e(TAG, "getDocumentFile: ...............1");
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Log.e(TAG, "getDocumentFile: ...............2");
            return DocumentFile.fromFile(file);
        }

        String baseFolder = getExtSdCardFolder(file, context);
        Log.e(TAG, "getDocumentFile: ...............3");
        boolean originalDirectory = false;
        if (baseFolder == null) {
            Log.e(TAG, "getDocumentFile: ...............4");
            return null;
        }

        String relativePath = null;
        Log.e(TAG, "getDocumentFile: ...............5");
        try {
            String fullPath = file.getCanonicalPath();
            if (!baseFolder.equals(fullPath)) {
                Log.e(TAG, "getDocumentFile: ...............6");
                relativePath = fullPath.substring(baseFolder.length() + 1);
            } else {
                Log.e(TAG, "getDocumentFile: ...............7");
                originalDirectory = true;
            }
        } catch (IOException e) {
            Log.e(TAG, "getDocumentFile: ...............8:" + e.getMessage());
            return null;
        } catch (Exception f) {
            Log.e(TAG, "getDocumentFile: ...............9" + f.getMessage());
            originalDirectory = true;
            //continue
        }
        String as = PreferenceManager.getDefaultSharedPreferences(context).getString(PreferencesConstants.PREFERENCE_URI,
                null);
        Log.e(TAG, "getDocumentFile: ...............10");
        Uri treeUri = null;
        if (as != null) treeUri = Uri.parse(as);
        if (treeUri == null) {
            Log.e(TAG, "getDocumentFile: ...............11");
            return null;
        }

        // start with root of SD card and then parse through document tree.
        DocumentFile document = DocumentFile.fromTreeUri(context, treeUri);
        if (originalDirectory) {
            Log.e(TAG, "getDocumentFile: ...............12");
            return document;
        }
        String[] parts = relativePath.split("\\/");
        for (int i = 0; i < parts.length; i++) {
            Log.e(TAG, "getDocumentFile: ...............13");
            DocumentFile nextDocument = document.findFile(parts[i]);

            if (nextDocument == null) {
                if ((i < parts.length - 1) || isDirectory) {
                    nextDocument = document.createDirectory(parts[i]);
                } else {
                    nextDocument = document.createFile("image", parts[i]);
                }
            }
            document = nextDocument;
        }
        Log.e(TAG, "getDocumentFile: ...............14");
        return document;
    }

    // Utility methods for Kitkat

    /**
     * Copy a resource file into a private target directory, if the target does not yet exist. Required for the Kitkat
     * workaround.
     *
     * @param resource   The resource file.
     * @param folderName The folder below app folder where the file is copied to.
     * @param targetName The name of the target file.
     * @return the dummy file.
     */
    private static File copyDummyFile(final int resource, final String folderName, final String targetName, Context context)
            throws IOException {
        File externalFilesDir = context.getExternalFilesDir(folderName);
        if (externalFilesDir == null) {
            return null;
        }
        File targetFile = new File(externalFilesDir, targetName);

        if (!targetFile.exists()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = context.getResources().openRawResource(resource);
                out = new FileOutputStream(targetFile);
                byte[] buffer = new byte[4096]; // MAGIC_NUMBER
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        // do nothing
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        // do nothing
                    }
                }
            }
        }
        return targetFile;
    }

    /**
     * Checks whether the target path exists or is writable
     * @param f the target path
     * @return 1 if exists or writable, 0 if not writable
     */

    /**
     * Copy the dummy image and dummy mp3 into the private folder, if not yet there. Required for the Kitkat workaround.
     *
     * @return the dummy mp3.
     */
    private static File copyDummyFiles(Context c) {
        try {
            copyDummyFile(R.mipmap.ic_launcher, "mkdirFiles", "albumart.jpg", c);
            return copyDummyFile(R.raw.temptrack, "mkdirFiles", "temptrack.mp3", c);

        } catch (IOException e) {
            Log.e(LOG, "Could not copy dummy files.", e);
            return null;
        }
    }

    static class MediaFile {
        private static final String NO_MEDIA = ".nomedia";
        private static final String ALBUM_ART_URI = "content://media/external/audio/albumart";
        private static final String[] ALBUM_PROJECTION = {BaseColumns._ID, MediaStore.Audio.AlbumColumns.ALBUM_ID, "media_type"};

        private static File getExternalFilesDir(Context context) {


            try {
                Method method = Context.class.getMethod("getExternalFilesDir", String.class);
                return (File) method.invoke(context, (String) null);
            } catch (SecurityException ex) {
                //   Log.d(Maui.LOG_TAG, "Unexpected reflection error.", ex);
                return null;
            } catch (NoSuchMethodException ex) {
                //     Log.d(Maui.LOG_TAG, "Unexpected reflection error.", ex);
                return null;
            } catch (IllegalArgumentException ex) {
                // Log.d(Maui.LOG_TAG, "Unexpected reflection error.", ex);
                return null;
            } catch (IllegalAccessException ex) {
                //Log.d(Maui.LOG_TAG, "Unexpected reflection error.", ex);
                return null;
            } catch (InvocationTargetException ex) {
                //Log.d(Maui.LOG_TAG, "Unexpected reflection error.", ex);
                return null;
            }
        }


        private final File file;
        private final Context context;
        private final ContentResolver contentResolver;
        Uri filesUri;

        MediaFile(Context context, File file) {
            this.file = file;
            this.context = context;
            contentResolver = context.getContentResolver();
            filesUri = MediaStore.Files.getContentUri("external");
        }

        /**
         * Deletes the file. Returns true if the file has been successfully deleted or otherwise does not exist. This operation is not
         * recursive.
         */
        public boolean delete() {

            if (!file.exists()) {
                return true;
            }

            boolean directory = file.isDirectory();
            if (directory) {
                // Verify directory does not contain any files/directories within it.
                String[] files = file.list();
                if (files != null && files.length > 0) {
                    return false;
                }
            }

            String where = MediaStore.MediaColumns.DATA + "=?";
            String[] selectionArgs = new String[]{file.getAbsolutePath()};

            // Delete the entry from the media database. This will actually delete media files (images, audio, and video).
            contentResolver.delete(filesUri, where, selectionArgs);

            if (file.exists()) {
                // If the file is not a media file, create a new entry suggesting that this location is an image, even
                // though it is not.
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                // Delete the created entry, such that content provider will delete the file.
                contentResolver.delete(filesUri, where, selectionArgs);
            }

            return !file.exists();
        }

        public File getFile() {
            return file;
        }

        private int getTemporaryAlbumId() {
            final File temporaryTrack;
            try {
                temporaryTrack = installTemporaryTrack();
            } catch (IOException ex) {
                return 0;
            }

            final String[] selectionArgs = {temporaryTrack.getAbsolutePath()};
            Cursor cursor = contentResolver.query(filesUri, ALBUM_PROJECTION, MediaStore.MediaColumns.DATA + "=?",
                    selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, temporaryTrack.getAbsolutePath());
                values.put(MediaStore.MediaColumns.TITLE, "{MediaWrite Workaround}");
                values.put(MediaStore.MediaColumns.SIZE, temporaryTrack.length());
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
                values.put(MediaStore.Audio.AudioColumns.IS_MUSIC, true);
                contentResolver.insert(filesUri, values);
            }
            cursor = contentResolver.query(filesUri, ALBUM_PROJECTION, MediaStore.MediaColumns.DATA + "=?",
                    selectionArgs, null);
            if (cursor == null) {
                return 0;
            }
            if (!cursor.moveToFirst()) {
                cursor.close();
                return 0;
            }
            int id = cursor.getInt(0);
            int albumId = cursor.getInt(1);
            int mediaType = cursor.getInt(2);
            cursor.close();

            ContentValues values = new ContentValues();
            boolean updateRequired = false;
            if (albumId == 0) {
                values.put(MediaStore.Audio.AlbumColumns.ALBUM_ID, 13371337);
                updateRequired = true;
            }
            if (mediaType != 2) {
                values.put("media_type", 2);
                updateRequired = true;
            }
            if (updateRequired) {
                contentResolver.update(filesUri, values, BaseColumns._ID + "=" + id, null);
            }
            cursor = contentResolver.query(filesUri, ALBUM_PROJECTION, MediaStore.MediaColumns.DATA + "=?",
                    selectionArgs, null);
            if (cursor == null) {
                return 0;
            }

            try {
                if (!cursor.moveToFirst()) {
                    return 0;
                }
                return cursor.getInt(1);
            } finally {
                cursor.close();
            }
        }

        private File installTemporaryTrack()
                throws IOException {
            File externalFilesDir = getExternalFilesDir(context);
            if (externalFilesDir == null) {
                return null;
            }
            File temporaryTrack = new File(externalFilesDir, "temptrack.mp3");
            if (!temporaryTrack.exists()) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = context.getResources().openRawResource(R.raw.temptrack);
                    out = new FileOutputStream(temporaryTrack);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ex) {
                            return null;
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                            return null;
                        }
                    }
                }
            }
            return temporaryTrack;
        }

        public boolean mkdir()
                throws IOException {
            if (file.exists()) {
                return file.isDirectory();
            }

            File tmpFile = new File(file, ".MediaWriteTemp");
            int albumId = getTemporaryAlbumId();

            if (albumId == 0) {
                throw new IOException("Fail");
            }

            Uri albumUri = Uri.parse(ALBUM_ART_URI + '/' + albumId);
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, tmpFile.getAbsolutePath());

            if (contentResolver.update(albumUri, values, null, null) == 0) {
                values.put(MediaStore.Audio.AlbumColumns.ALBUM_ID, albumId);
                contentResolver.insert(Uri.parse(ALBUM_ART_URI), values);
            }

            try {
                ParcelFileDescriptor fd = contentResolver.openFileDescriptor(albumUri, "r");
                fd.close();
            } finally {
                MediaFile tmpMediaFile = new MediaFile(context, tmpFile);
                tmpMediaFile.delete();
            }

            return file.exists();
        }

        /**
         * Returns an OutputStream to write to the file. The file will be truncated immediately.
         */
        public OutputStream write(long size)
                throws IOException {

            if (NO_MEDIA.equals(file.getName().trim())) {
                throw new IOException("Unable to create .nomedia file via media content provider API.");
            }

            if (file.exists() && file.isDirectory()) {
                throw new IOException("File exists and is a directory.");
            }

            // Delete any existing entry from the media database.
            // This may also delete the file (for media types), but that is irrelevant as it will be truncated momentarily in any case.
            String where = MediaStore.MediaColumns.DATA + "=?";
            String[] selectionArgs = new String[]{file.getAbsolutePath()};
            contentResolver.delete(filesUri, where, selectionArgs);

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            values.put(MediaStore.MediaColumns.SIZE, size);
            Uri uri = contentResolver.insert(filesUri, values);

            if (uri == null) {
                // Should not occur.
                throw new IOException("Internal error.");
            }

            return contentResolver.openOutputStream(uri);
        }

    }

    public static String fSize(long sizeInByte) {
        if (sizeInByte < 1024)
            return String.format("%s", sizeInByte);
        else if (sizeInByte < 1024 * 1024)
            return String.format(Locale.CANADA, "%.2fKB", sizeInByte / 1024.);
        else if (sizeInByte < 1024 * 1024 * 1024)
            return String.format(Locale.CANADA, "%.2fMB", sizeInByte / 1024. / 1024);
        else
            return String.format(Locale.CANADA, "%.2fGB", sizeInByte / 1024. / 1024 / 1024);
    }


    /**
     * Validate given text is a valid filename.
     *
     * @param text
     * @return true if given text is a valid filename
     */
    public static boolean isValidFilename(String text) {
        //It's not easy to use regex to detect single/double dot while leaving valid values (filename.zip) behind...
        //So we simply use equality to check them
        return (!FILENAME_REGEX.matcher(text).find())
                && !".".equals(text) && !"..".equals(text);
    }
}
