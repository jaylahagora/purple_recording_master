package com.smartrecording.recordingplugin.utils;

import android.content.Context;

import androidx.documentfile.provider.DocumentFile;


import java.io.File;
import java.net.MalformedURLException;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * Created by Arpit on 07-07-2015.
 */
//Hybrid file for handeling all types of files
public class HybridFile {

    private static final String TAG = "HFile";

    String path;
    //public static final int ROOT_MODE=3,LOCAL_MODE=0,SMB_MODE=1,UNKNOWN=-1;
    OpenMode mode = OpenMode.FILE;


    public HybridFile(OpenMode mode, String path) {
        this.path = path;
        this.mode = mode;
    }


    public void setMode(OpenMode mode) {
        this.mode = mode;
    }

    public OpenMode getMode() {
        return mode;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLocal() {
        return mode == OpenMode.FILE;
    }

    public boolean isRoot() {
        return mode == OpenMode.ROOT;
    }

    public boolean isSmb() {
        return mode == OpenMode.SMB;
    }

    public boolean isSftp() {
        return mode == OpenMode.SFTP;
    }

    public boolean isOtgFile() {
        return mode == OpenMode.OTG;
    }

    public boolean isBoxFile() {
        return mode == OpenMode.BOX;
    }

    public boolean isDropBoxFile() {
        return mode == OpenMode.DROPBOX;
    }

    public boolean isOneDriveFile() {
        return mode == OpenMode.ONEDRIVE;
    }

    public boolean isGoogleDriveFile() {
        return mode == OpenMode.GDRIVE;
    }

    public File getFile() {
        return new File(path);
    }


    /**
     * Helper method to find length
     */


    public String getPath() {
        return path;
    }

    /**
     * @deprecated use {@link #getName(Context)}
     */
    public String getName() {
        String name = null;
        switch (mode) {
            case SMB:
                SmbFile smbFile = getSmbFile();
                if (smbFile != null)
                    return smbFile.getName();
                break;
            case FILE:
                return new File(path).getName();
            case ROOT:
                return new File(path).getName();
            default:
                StringBuilder builder = new StringBuilder(path);
                name = builder.substring(builder.lastIndexOf("/") + 1, builder.length());
        }
        return name;
    }

    public String getName(Context context) {
        String name = null;
        switch (mode) {
            case SMB:
                SmbFile smbFile = getSmbFile();
                if (smbFile != null)
                    return smbFile.getName();
                break;
            case FILE:
                return new File(path).getName();
            case ROOT:
                return new File(path).getName();
            case OTG:
                return OTGUtil.getDocumentFile(path, context, false).getName();
            default:
                StringBuilder builder = new StringBuilder(path);
                name = builder.substring(builder.lastIndexOf("/") + 1, builder.length());
        }
        return name;
    }

    public SmbFile getSmbFile(int timeout) {
        try {
            SmbFile smbFile = new SmbFile(path);
            smbFile.setConnectTimeout(timeout);
            return smbFile;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public SmbFile getSmbFile() {
        try {
            return new SmbFile(path);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public boolean isCustomPath() {
        return path.equals("0") ||
                path.equals("1") ||
                path.equals("2") ||
                path.equals("3") ||
                path.equals("4") ||
                path.equals("5") ||
                path.equals("6");
    }

    /**
     * Returns a path to parent for various {@link #mode}
     *
     * @deprecated use {@link #getParent(Context)} to handle content resolvers
     */
    public String getParent() {
        String parentPath = "";
        switch (mode) {
            case SMB:
                try {
                    parentPath = new SmbFile(path).getParent();
                } catch (MalformedURLException e) {
                    parentPath = "";
                    e.printStackTrace();
                }
                break;
            case FILE:
            case ROOT:
                parentPath = new File(path).getParent();
                break;
            default:
                StringBuilder builder = new StringBuilder(path);
                return builder.substring(0, builder.length() - (getName().length() + 1));
        }
        return parentPath;
    }

    /**
     * Helper method to get parent path
     */
    public String getParent(Context context) {

        String parentPath = "";
        switch (mode) {
            case SMB:
                try {
                    parentPath = new SmbFile(path).getParent();
                } catch (MalformedURLException e) {
                    parentPath = "";
                    e.printStackTrace();
                }
                break;
            case FILE:
            case ROOT:
                parentPath = new File(path).getParent();
                break;
            case OTG:
            default:
                StringBuilder builder = new StringBuilder(path);
                StringBuilder parentPathBuilder = new StringBuilder(builder.substring(0,
                        builder.length() - (getName(context).length() + 1)));
                return parentPathBuilder.toString();
        }
        return parentPath;
    }



    public boolean exists() {
        boolean exists = false;
        if (isSmb()) {
            try {
                SmbFile smbFile = getSmbFile(2000);
                exists = smbFile != null && smbFile.exists();
            } catch (SmbException e) {
                exists = false;
            }
        } else if (isLocal()) {
            exists = new File(path).exists();
        }

        return exists;
    }

    /**
     * Helper method to check file existence in otg
     */
    public boolean exists(Context context) {
        if (isOtgFile()) {
            DocumentFile fileToCheck = OTGUtil.getDocumentFile(path, context, false);
            return fileToCheck != null;
        } else return (exists());
    }

    /**
     * Whether file is a simple file (i.e. not a directory/smb/otg/other)
     *
     * @return true if file; other wise false
     */





}
