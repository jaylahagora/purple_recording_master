package com.smartrecording.recordingplugin.utils;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.xunison.recordingplugin.R;
import com.smartrecording.recordingplugin.activity.MainActivity;
import com.smartrecording.recordingplugin.app.MyApplication;
import com.smartrecording.recordingplugin.model.ColorModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;

import static com.purple.iptv.player.common.CustomDialogs.getSelectorDrawablewithbottomleftradious;
import static com.purple.iptv.player.common.CustomDialogs.getSelectorDrawablewithbottomrightradious;

public class FileUtils {

    private static final String TAG = "FileUtils";

    /**
     * @param mode: Context.MODE_APPEND     --> write more to exist file.
     *              Context.MODE_PRIVATE    --> only available within app.
     *              FileProvider with the FLAG_GRANT_READ_URI_PERMISSION    --> share file.
     */
    public static void write_file_internal(Context context, int mode, String fileName, String data) {

        try {

            // 2 lines of these are the same with one line below.
            // Notice:
            //      + Instead using getFilesDir(), you can use getCacheDir() to get directory
            // of cache place in internal storage.
            //      + openFileOutput will create in app private storage, not in cache.

//                File file = new File(context.getFilesDir(), fileName);
//                FileOutputStream fos = new FileOutputStream(file);

            FileOutputStream fos = context.openFileOutput(fileName, mode);

            // Instantiate a stream writer.
            OutputStreamWriter out = new OutputStreamWriter(fos);

            // Add data.
            if (mode == Context.MODE_APPEND) {
                out.append(data + "\n");
            } else {
                out.write(data);
            }

            // Close stream writer.
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String read_file_internal(Context context, String fileName) {

        try {

            // 2 lines of these are the same with one line below.
            // Notice:
            //      + Instead using getFilesDir(), you can use getCacheDir() to get directory
            // of cache place in internal storage.
            //      + openFileInput will open a file in app private storage, not in cache.

//            File file = new File(context.getFilesDir(), fileName);
//            FileInputStream fis = new FileInputStream(file);

            FileInputStream fis = context.openFileInput(fileName);

            // Instantiate a buffer reader. (Buffer )
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(fis));

            String s;
            StringBuilder fileContentStrBuilder = new StringBuilder();

            // Read every lines in file.
            while ((s = bufferedReader.readLine()) != null) {
                fileContentStrBuilder.append(s);
            }

            // Close buffer reader.
            bufferedReader.close();

            return fileContentStrBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public boolean delete_file_internal(Context context, String fileName) {
        // If the file is saved on internal storage, you can also ask the Context to locate and delete a file by calling deleteFile()
        return context.deleteFile(fileName);
    }

    /* ******************************************************************************************** *
     *                                                                                              *
     *  - If your app uses the WRITE_EXTERNAL_STORAGE permission, then it implicitly has permission *
     *  to read the external storage as well.                                                       *
     *  - Must declare READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE before manipulate with       *
     *  external storage.                                                                           *
     *  - Handle file in private external storage in low api (below 18), don't require permission.  *
     *  - Don't be confused external storage with SD external card, since internal SD card is       *
     *  considered as external storage. And internal SD card is a default external storage.         *
     *                                                                                              *
     * ******************************************************************************************** */

    // Checks if external storage is available for read and write.
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // Checks if external storage is available to at least read.
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean is_external_storage_available() {
        if (!isExternalStorageWritable() || !isExternalStorageReadable()) {
            return false;
        }

        return true;
    }

    /**
     * Write to a public external directory.
     *
     * @param mode:      Context.MODE_APPEND     --> write more to exist file.
     *                   Context.MODE_PRIVATE    --> only available within app.
     *                   FileProvider with the FLAG_GRANT_READ_URI_PERMISSION    --> share file.
     * @param mainDir:   representing the appropriate directory on the external storage ( Environment.DIRECTORY_MUSIC, ...)
     * @param subFolder: usually an app name to distinguish with another app.
     * @param fileName:  ".nomedia" + fileName to hide it from MediaStore scanning.
     */
    public static void write_file_external_public(String mainDir, String subFolder, int mode, String fileName, String data) {

        if (!is_external_storage_available()) {
            Log.e(TAG, "External storage is not available.");
            return;
        }

        // Get the directory for the user's public mainDir directory.
        String directory = get_external_public_directory(mainDir, subFolder);
        File folder = new File(directory);

        // If directory doesn't exist, create it.
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, fileName);

        Log.d(TAG, "File directory: " + file.getAbsolutePath());

        try {
            FileOutputStream fos;
            if (mode == Context.MODE_APPEND) {
                fos = new FileOutputStream(file, true);
            } else {
                fos = new FileOutputStream(file);
            }

            // Instantiate a stream writer.
            OutputStreamWriter out = new OutputStreamWriter(fos);

            // Add data.
            if (mode == Context.MODE_APPEND) {
                out.append(data + "\n");
            } else {
                out.write(data);
            }

            // Close stream writer.
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get_external_public_directory(String mainDir, String subFolder) {

        File root;
        if (mainDir.isEmpty()) {
            root = Environment.getExternalStorageDirectory();
        } else {
            root = Environment.getExternalStoragePublicDirectory(mainDir);
        }

        return root + File.separator + subFolder;
    }

    public String read_file_external_public(String mainDir, String subFolder, String fileName) {

        try {

            String directory = get_external_public_directory(mainDir, subFolder);
            File folder = new File(directory);

            File file = new File(folder, fileName);

            // If file doesn't exist.
            if (!file.exists()) {
                Log.e(TAG, "File doesn't exist.");
                return null;
            }

            FileInputStream fis = new FileInputStream(file);

            // Instantiate a buffer reader. (Buffer )
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(fis));

            String s;
            StringBuilder fileContentStrBuilder = new StringBuilder();

            // Read every lines in file.
            while ((s = bufferedReader.readLine()) != null) {
                fileContentStrBuilder.append(s);
            }

            // Close buffer reader.
            bufferedReader.close();

            return fileContentStrBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public boolean delete_file_external_public(String mainDir, String subFolder, String fileName) {

        String directory = get_external_public_directory(mainDir, subFolder);
        File folder = new File(directory);

        File file = new File(folder, fileName);

        // If file doesn't exist.
        if (!file.exists()) {
            Log.e(TAG, "File doesn't exist.");
            return true;
        }
        return file.delete();
    }

    /**
     * Write to a public external directory.
     *
     * @param mode:      Context.MODE_APPEND     --> write more to exist file.
     *                   Context.MODE_PRIVATE    --> only available within app.
     *                   FileProvider with the FLAG_GRANT_READ_URI_PERMISSION    --> share file.
     * @param mainDir:   - Representing the appropriate directory on the external storage ( Environment.DIRECTORY_MUSIC, ...)
     *                   - It can be null --> represent that directory as a parent file of private external storage in the app.
     * @param subFolder: usually an app name to distinguish with another app.
     */
    public static void write_file_external_private(Context context, String mainDir, String subFolder, int mode, String fileName, String data) {

        // Get the directory for the user's private mainDir directory.
        String directory = context.getExternalFilesDir(mainDir) + File.separator + subFolder;
        File folder = new File(directory);

        // If directory doesn't exist, create it.
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, fileName);

        try {
            FileOutputStream fos;
            if (mode == Context.MODE_APPEND) {
                fos = new FileOutputStream(file, true);
            } else {
                fos = new FileOutputStream(file);
            }

            // Instantiate a stream writer.
            OutputStreamWriter out = new OutputStreamWriter(fos);

            // Add data.
            if (mode == Context.MODE_APPEND) {
                out.append(data + "\n");
            } else {
                out.write(data);
            }

            // Close stream writer.
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get_external_private_directory(Context context, String mainDir, String subFolder) {
        return context.getExternalFilesDir(mainDir) + File.separator + subFolder;
    }

    public String read_file_external_private(Context context, String mainDir, String subFolder, String fileName) {

        try {

            String directory = get_external_private_directory(context, mainDir, subFolder);
            File folder = new File(directory);

            File file = new File(folder, fileName);

            // If file doesn't exist.
            if (!file.exists()) {
                Log.e(TAG, "File doesn't exist.");
                return null;
            }

            FileInputStream fis = new FileInputStream(file);

            // Instantiate a buffer reader. (Buffer )
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(fis));

            String s;
            StringBuilder fileContentStrBuilder = new StringBuilder();

            // Read every lines in file.
            while ((s = bufferedReader.readLine()) != null) {
                fileContentStrBuilder.append(s);
            }

            // Close buffer reader.
            bufferedReader.close();

            return fileContentStrBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public boolean delete_file_external_private(Context context, String mainDir, String subFolder, String fileName) {

        String directory = get_external_private_directory(context, mainDir, subFolder);
        File folder = new File(directory);

        File file = new File(folder, fileName);

        // If file doesn't exist.
        if (!file.exists()) {
            Log.e(TAG, "File doesn't exist.");
            return true;
        }
        return file.delete();
    }

    /* ******************************************************************************************** */

    // Looking for File directory of all external cards (including onboard sd card).
  /*  public static ArrayList<File> get_external_sd_card_directory() {

        // Retrieve the primary External Storage (usually onboard sd card, it's based on user setting).
        final File primaryExternalStorage = Environment.getExternalStorageDirectory();

        // Primary external storage (onboard sd card) usually has path: [storage]/emulated/0
        if(primaryExternalStorage.getParentFile()!=null)
        {
            File externalStorageRoot = primaryExternalStorage.getParentFile().getParentFile();

            // Get list folders under externalStorageRoot (which includes primaryExternalStorage folder).
            File[] files = externalStorageRoot.listFiles();

            ArrayList<File> listStorage = new ArrayList<>();

            for (File file : files) {

                // it is a real directory (not a USB drive)...
                if (file.isDirectory() && file.canRead() && (file.listFiles().length > 0)) {
                    listStorage.add(file);
                }
            }

            return listStorage;
        }

    }*/

    // Base on the list of file directory gotten from get_external_sd_card_directory() method,
    // you can choose what file directory/ file to read or write.
    public static void write_file(File directory, String fileName, int mode, String data) {
        try {

            // Create file.
            File file = new File(directory, fileName);

            FileOutputStream fos;
            if (mode == Context.MODE_APPEND) {
                fos = new FileOutputStream(file, true);
            } else {
                fos = new FileOutputStream(file);
            }

            // Instantiate a stream writer.
            OutputStreamWriter out = new OutputStreamWriter(fos);

            // Add data.
            if (mode == Context.MODE_APPEND) {
                out.append(data + "\n");
            } else {
                out.write(data);
            }

            // Close stream writer.
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String read_file(File directory, String fileName, int mode, String data) {
        try {

            // Create file.
            File file = new File(directory, fileName);

            FileInputStream fis = new FileInputStream(file);

            // Instantiate a buffer reader. (Buffer )
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(fis));

            String s;
            StringBuilder fileContentStrBuilder = new StringBuilder();

            // Read every lines in file.
            while ((s = bufferedReader.readLine()) != null) {
                fileContentStrBuilder.append(s);
            }

            // Close buffer reader.
            bufferedReader.close();

            return fileContentStrBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String read_file(File file, int mode, String data) {
        try {

            FileInputStream fis = new FileInputStream(file);

            // Instantiate a buffer reader. (Buffer )
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(fis));

            String s;
            StringBuilder fileContentStrBuilder = new StringBuilder();

            // Read every lines in file.
            while ((s = bufferedReader.readLine()) != null) {
                fileContentStrBuilder.append(s);
            }

            // Close buffer reader.
            bufferedReader.close();

            return fileContentStrBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void delete_file(File file) {

        if (file.exists()) {
            file.delete();
        }
    }


    public static void mkfile(final OpenMode openMode, final String path, final String name, final Context ma) {
        mkFile(new HybridFile(openMode, path + "/" + name), ma, name);
    }

    public static void mkFile(final HybridFile path, final Context ma, String name) {

        Operations.mkfile(path, ma, false, new Operations.ErrorCallBack() {
            @Override
            public void exists(final HybridFile file) {
                ((Activity) ma).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, " mkFile exists run: ");
                        if (ma != null) {
                            // retry with dialog prompted again
                            mkfile(file.getMode(), file.getParent(), name, ma);
                        }
                    }
                });

            }

            @Override
            public void launchSAF(HybridFile file) {
                ((Activity) ma).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, " mkFile launchSAF run: ");
                        // ma.oppathe = path.getPath();
                        guideDialogForLEXA(ma);
                        //  triggerStorageAccessFramework(ma);

                    }
                });


            }

            @Override
            public void launchSAF(HybridFile file, HybridFile file1) {
                Log.e(TAG, " mkFile launchSAF1 run: ");
            }

            @Override
            public void done(HybridFile hFile, final boolean b) {
                ((Activity) ma).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, " mkFile done run: ");
                    }
                });

            }

            @Override
            public void invalidName(final HybridFile file) {
                Log.e(TAG, " mkFile invalidName run: ");
            }
        });
    }

    static Dialog dialog;

    public static void guideDialogForLEXA(Context path) {
        dialog = new Dialog(path, R.style.Theme_D1NoTitleDim) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                ((Activity) path).finish();
            }
        };
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.setCancelable(true);
        TextView textView = dialog.findViewById(R.id.description);
        LinearLayout ll = dialog.findViewById(R.id.ll);
        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel);
        TextView btn_ok = dialog.findViewById(R.id.btn_ok);
        ((ImageView) dialog.findViewById(R.id.icon)).setImageResource(R.drawable.sd_operate_step);
        textView.setText(String.format("%s%s", path.getString(R.string.needs_access_summary), path.getString(R.string.needs_access_summary1)));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = -2;
        layoutParams.gravity = 17;
        ColorModel colorModel = new Gson().fromJson(MyApplication.getInstance().getPrefManager().getColormodel(), ColorModel.class);
        ll.setBackground(path.getResources().getDrawable(R.drawable.custom_dialog_back1));
        btn_cancel.setBackground(getSelectorDrawablewithbottomleftradious(path.getResources().getColor(R.color.black), colorModel.getSelected_color()));
        btn_ok.setBackground(getSelectorDrawablewithbottomrightradious(path.getResources().getColor(R.color.black), colorModel.getSelected_color()));

        btn_ok.requestFocus();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                triggerStorageAccessFramework(path);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(path, R.string.error, Toast.LENGTH_SHORT).show();
                ((Activity) path).finish();
            }
        });
        // dialog.requestWindowFeature(1);
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
//        final MaterialDialog.Builder x = new MaterialDialog.Builder(path);
//        x.theme(Theme.LIGHT);
//        x.title(R.string.needs_access);
//        LayoutInflater layoutInflater = (LayoutInflater) path.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.lexadrawer, null);
//        x.customView(view, true);
//        // textView
//
//        ((ImageView) view.findViewById(R.id.icon)).setImageResource(R.drawable.sd_operate_step);
//        x.positiveText(R.string.open).positiveFocus(true)
//                .negativeText(R.string.dialog_cancel)
//                .positiveColor(Color.parseColor("#000000"))
//                .negativeColor(Color.parseColor("#000000"))
//                .onPositive((dialog, which)->triggerStorageAccessFramework(path))
//                .onNegative((dialog, which)->Toast.makeText(path, R.string.error, Toast.LENGTH_SHORT).show());
//        final MaterialDialog y = x.build();
//        y.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void triggerStorageAccessFramework(Context ma) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        if (ma instanceof MainActivity) {
            ((MainActivity) ma).startActivityForResult(intent, 3);

        } else {
            ((Activity) ma).startActivityForResult(intent, 3);
        }

    }

    public static String[] getPermissionList(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return new String[]{Manifest.permission.READ_MEDIA_VIDEO};
        }
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    public static String getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.READ_MEDIA_VIDEO;
        }
        return Manifest.permission.READ_EXTERNAL_STORAGE;
    }
}