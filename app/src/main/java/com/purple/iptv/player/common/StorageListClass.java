package com.purple.iptv.player.common;

import android.content.Context;
import android.os.Environment;

import com.purple.iptv.player.utils.UtilMethods;

import java.io.File;
import java.util.ArrayList;

public class StorageListClass {

    private Context mContext;
    private ArrayList<String> rootPaths;

    public StorageListClass(Context mContext) {
        this.mContext = mContext;
        rootPaths = new ArrayList<>();
    }

    public ArrayList<StorageFileModel> findFileList(String path, boolean showFiles) {
        ArrayList<StorageFileModel> mList = new ArrayList<>();
        File rootFile = new File(path);
        if (rootFile.exists()) {
            StorageFileModel fileModel = new StorageFileModel();
            String backPath;
            if (rootPaths.contains(path)) {
                backPath = "root";
            } else {
                backPath = path.substring(0, path.lastIndexOf("/"));
            }
            UtilMethods.LogMethod("back_path1234", String.valueOf(backPath));
            fileModel.setFile_name("..");
            fileModel.setNick_name("..");
            fileModel.setFile_path(backPath);
            fileModel.setFolder(true);
            mList.add(fileModel);
            File[] files = rootFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    StorageFileModel fileModel1 = new StorageFileModel();
                    fileModel1.setFile_name(file.getName());
                    fileModel1.setNick_name(file.getName());
                    fileModel1.setFile_path(file.getPath());
                    fileModel1.setFolder(file.isDirectory());
                    if (showFiles) {
                        mList.add(fileModel1);
                    } else {
                        if (file.isDirectory()) {
                            mList.add(fileModel1);
                        }

                    }
                    UtilMethods.LogMethod("storage123_fileModel1", String.valueOf(fileModel1));
                }
            }
        }
        return mList;
    }

    public ArrayList<StorageFileModel> getRootStorageList() {
        ArrayList<StorageFileModel> mList = new ArrayList<>();
        StorageFileModel fileModel = new StorageFileModel();
        fileModel.setFile_name("Internal Storage");
        fileModel.setNick_name("Internal Storage");
        fileModel.setFile_path(Environment.getExternalStorageDirectory().getAbsolutePath());
        fileModel.setFolder(true);
        fileModel.setRemovable(false);
        mList.add(fileModel);
        rootPaths.add(Environment.getExternalStorageDirectory().getAbsolutePath());

        File rootFile = new File("//storage");
        UtilMethods.LogMethod("storage123_rootFile", String.valueOf(rootFile));
        if (rootFile.exists()) {
            File[] files = rootFile.listFiles();
            if (files != null) {
                UtilMethods.LogMethod("storage123_files", String.valueOf(files.length));
                for (File file : files) {
                    UtilMethods.LogMethod("storage123_file.getName()", String.valueOf(file.getName()));
                    if (file.getName().equals("self") || file.getName().equals("emulated")) {
                        continue;
                    }
                    StorageFileModel fileModel1 = new StorageFileModel();
                    fileModel1.setFile_name(file.getName());
                    fileModel1.setFile_path(file.getPath());
                    fileModel1.setFolder(file.isDirectory());
                    fileModel1.setNick_name("External (" + file.getName() + ") ");
//                    fileModel1.setRemovable(Environment.isExternalStorageRemovable(file));
                    if (Environment.getStorageState(new File(file.getAbsolutePath())).equals("mounted")) {
                        rootPaths.add(file.getPath());
                        UtilMethods.LogMethod("storage123_fileModel1", String.valueOf(fileModel1));
                        mList.add(fileModel1);
                    }
                }
            }
        }

        return mList;
    }

    public boolean makeNewFolder(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }

    public class StorageFileModel {

        String file_name;
        String file_path;
        String nick_name;
        boolean folder;
        boolean removable;

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public boolean isRemovable() {
            return removable;
        }

        public void setRemovable(boolean removable) {
            this.removable = removable;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getFile_path() {
            return file_path;
        }

        public void setFile_path(String file_path) {
            this.file_path = file_path;
        }

        public boolean isFolder() {
            return folder;
        }

        public void setFolder(boolean folder) {
            this.folder = folder;
        }

        @Override
        public String toString() {
            return "StorageFileModel{" +
                    "file_name='" + file_name + '\'' +
                    ", file_path='" + file_path + '\'' +
                    ", folder=" + folder +
                    '}';
        }
    }


}
