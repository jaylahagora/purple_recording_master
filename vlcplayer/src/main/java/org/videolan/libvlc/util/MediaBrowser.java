/*****************************************************************************
 * MediaBrowser.java
 *****************************************************************************
 * Copyright © 2015 VLC authors, VideoLAN and VideoLabs
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package org.videolan.libvlc.util;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.MainThread;

import org.videolan.libvlc.FactoryManager;
import org.videolan.libvlc.MediaDiscoverer;
import org.videolan.libvlc.MediaList;
import org.videolan.libvlc.interfaces.ILibVLC;
import org.videolan.libvlc.interfaces.IMedia;
import org.videolan.libvlc.interfaces.IMediaFactory;
import org.videolan.libvlc.interfaces.IMediaList;

import java.util.ArrayList;

public class MediaBrowser {
    private static final String TAG = "MediaBrowser";

    private final ILibVLC mILibVlc;
    private final ArrayList<MediaDiscoverer> mMediaDiscoverers = new ArrayList<MediaDiscoverer>();
    private final ArrayList<IMedia> mDiscovererMediaArray = new ArrayList<IMedia>();
    private IMediaList mBrowserMediaList;
    private IMedia mMedia;
    private EventListener mEventListener;
    private Handler mHandler;
    private boolean mAlive;
    private IMediaFactory mFactory;

    private static final String IGNORE_LIST_OPTION =  ":ignore-filetypes=";
    private String mIgnoreList = "db,nfo,ini,jpg,jpeg,ljpg,gif,png,pgm,pgmyuv,pbm,pam,tga,bmp,pnm,xpm,xcf,pcx,tif,tiff,lbm,sfv,txt,sub,idx,srt,ssa,ass,smi,utf,utf-8,rt,aqt,txt,usf,jss,cdg,psb,mpsub,mpl2,pjs,dks,stl,vtt,ttml";

    public static class Flag {
        /** If this flag is set, browse() could fire up dialogs */
        public final static int Interact = 1;
        /** If this flag is set, slaves won't be attached to medias but will be added as a media. */
        public final static int NoSlavesAutodetect = 1 << 1;
        /** If this flag is set, hidden fils won't be ignored */
        public final static int ShowHiddenFiles = 1 << 2;
    }

    /**
     * Listener called when medias are added or removed.
     */
    public interface EventListener {
        /**
         * Received when a new media is added.
         * @param index
         * @param media
         */
        void onMediaAdded(int index, IMedia media);
        /**
         * Received when a media is removed (Happens only when you discover networks)
         * @param index
         * @param media Released media, but cached attributes are still
         * available (like media.getMrl())
         */
        void onMediaRemoved(int index, IMedia media);
        /**
         * Called when browse ended.
         * It won't be called when you discover networks
         */
        void onBrowseEnd();
    }

     /**
     *
     * @param libvlc The LibVLC instance to use
     * @param listener The Listener which will receive callbacks
     *
     * With this constructor, callbacks will be executed in the main thread
     */
    public MediaBrowser(ILibVLC libvlc, EventListener listener) {
        mFactory = ((IMediaFactory) FactoryManager.getFactory(IMediaFactory.factoryId));
        mILibVlc = libvlc;
        mILibVlc.retain();
        mEventListener = listener;
        mAlive = true;
    }

    /**
     *
     * @param libvlc The LibVLC instance to use
     * @param listener The Listener which will receive callbacks
     * @param handler Optional Handler in which callbacks will be posted. If set to null, a Handler will be created running on the main thread
     */
    public MediaBrowser(ILibVLC libvlc, EventListener listener, Handler handler) {
        this(libvlc, listener);
        mHandler = handler;
    }

    private void reset() {
        for (MediaDiscoverer md : mMediaDiscoverers)
            md.release();
        mMediaDiscoverers.clear();
        mDiscovererMediaArray.clear();
        if (mMedia != null) {
            mMedia.release();
            mMedia = null;
        }

        if (mBrowserMediaList != null) {
            mBrowserMediaList.release();
            mBrowserMediaList = null;
        }
    }

    /**
     * Release the MediaBrowser.
     */
    @MainThread
    public void release() {
        reset();
        if (!mAlive)
            throw new IllegalStateException("MediaBrowser released more than one time");
        mILibVlc.release();
        mAlive = false;
    }

    /**
     * Reset this media browser and register a new EventListener
     * @param eventListener new EventListener for this browser
     */
    @MainThread
    public void changeEventListener(EventListener eventListener){
        reset();
        mEventListener = eventListener;
    }

    private void startMediaDiscoverer(String discovererName) {
        MediaDiscoverer md = new MediaDiscoverer(mILibVlc, discovererName);
        mMediaDiscoverers.add(md);
        final MediaList ml = md.getMediaList();
        ml.setEventListener(mDiscovererMediaListEventListener, mHandler);
        ml.release();
        md.start();
    }

    /**
     * Discover all networks shares
     */
    @MainThread
    public void discoverNetworkShares() {
        reset();

        final MediaDiscoverer.Description descriptions[] =
                MediaDiscoverer.list(mILibVlc, MediaDiscoverer.Description.Category.Lan);
        if (descriptions == null)
            return;
        for (MediaDiscoverer.Description description : descriptions) {
            Log.i(TAG, "starting " + description.name + " discover (" + description.longName + ")");
            startMediaDiscoverer(description.name);
        }
    }

    /**
     * Discover networks shares using a specified Discoverer
     * @param serviceName see {@link MediaDiscoverer.Description.Category#name}
     */
    @MainThread
    public void discoverNetworkShares(String serviceName) {
        reset();
        startMediaDiscoverer(serviceName);
    }

    /**
     * Browse to the specified local path starting with '/'.
     *
     * @param path
     * @param flags see {@link Flag}
     */
    @MainThread
    public void browse(String path, int flags) {
        final IMedia media = mFactory.getFromLocalPath(mILibVlc, path);
        browse(media, flags);
        media.release();
    }

    /**
     * Browse to the specified uri.
     *
     * @param uri
     * @param flags see {@link Flag}
     */
    @MainThread
    public void browse(Uri uri, int flags) {
        final IMedia media = mFactory.getFromUri(mILibVlc, uri);
        browse(media, flags);
        media.release();
    }

    /**
     * Browse to the specified media.
     *
     * @param media Can be a media returned by MediaBrowser.
     * @param flags see {@link Flag}
     */
    @MainThread
    public void browse(IMedia media, int flags) {
        /* media can be associated with a medialist,
         * so increment ref count in order to don't clean it with the medialist
         */
        media.retain();
        media.addOption(IGNORE_LIST_OPTION + mIgnoreList);
        if ((flags & Flag.NoSlavesAutodetect) != 0)
            media.addOption(":no-sub-autodetect-file");
        if ((flags & Flag.ShowHiddenFiles) != 0)
            media.addOption(":show-hiddenfiles");
        int mediaFlags = IMedia.Parse.ParseNetwork;
        if ((flags & Flag.Interact) != 0)
            mediaFlags |= IMedia.Parse.DoInteract;
        reset();
        mBrowserMediaList = media.subItems();
        mBrowserMediaList.setEventListener(mBrowserMediaListEventListener, mHandler);
        media.parseAsync(mediaFlags, 0);
        mMedia = media;
    }

    /**
     * Get the number or media.
     */
    @MainThread
    public int getMediaCount() {
        return mBrowserMediaList != null ? mBrowserMediaList.getCount() : mDiscovererMediaArray.size();
    }

    /**
     * Get a media at a specified index. Should be released with {@link #release()}.
     */
    @MainThread
    public IMedia getMediaAt(int index) {
        if (index < 0 || index >= getMediaCount())
            throw new IndexOutOfBoundsException();
        final IMedia media = mBrowserMediaList != null ? mBrowserMediaList.getMediaAt(index) :
                mDiscovererMediaArray.get(index);
        media.retain();
        return media;
    }

    /**
     * Override the extensions list to be ignored in browsing
     * default is "db,nfo,ini,jpg,jpeg,ljpg,gif,png,pgm,pgmyuv,pbm,pam,tga,bmp,pnm,xpm,xcf,pcx,tif,tiff,lbm,sfv,txt,sub,idx,srt,cue,ssa"
     *
     * @param list files extensions to be ignored by browser
     */
    @MainThread
    public void setIgnoreFileTypes(String list) {
        mIgnoreList = list;
    }

    private final MediaList.EventListener mBrowserMediaListEventListener = new MediaList.EventListener() {
        @Override
        public void onEvent(MediaList.Event event) {
            if (mEventListener == null)
                return;
            final MediaList.Event mlEvent = event;

            switch (mlEvent.type) {
            case MediaList.Event.ItemAdded:
                mEventListener.onMediaAdded(mlEvent.index, mlEvent.media);
                break;
            case MediaList.Event.ItemDeleted:
                mEventListener.onMediaRemoved(mlEvent.index, mlEvent.media);
                break;
            case MediaList.Event.EndReached:
                mEventListener.onBrowseEnd();
            }
        }
    };

    private final MediaList.EventListener mDiscovererMediaListEventListener = new MediaList.EventListener() {
        @Override
        public void onEvent(MediaList.Event event) {
            if (mEventListener == null)
                return;
            final MediaList.Event mlEvent = event;
            int index = -1;

            /*
             * We use an intermediate array here since more than one MediaDiscoverer can be used
             */
            switch (mlEvent.type) {
            case MediaList.Event.ItemAdded:
                mDiscovererMediaArray.add(mlEvent.media);
                mEventListener.onMediaAdded(index, mlEvent.media);
                break;
            case MediaList.Event.ItemDeleted:
                index = mDiscovererMediaArray.indexOf(mlEvent.media);
                if (index != -1)
                    mDiscovererMediaArray.remove(index);
                if (index != -1)
                    mEventListener.onMediaRemoved(index, mlEvent.media);
                break;
            case MediaList.Event.EndReached:
                mEventListener.onBrowseEnd();
            }
        }
    };
}