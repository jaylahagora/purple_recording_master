package org.videolan.libvlc;

import android.content.Context;

import org.videolan.libvlc.interfaces.ILibVLC;
import org.videolan.libvlc.interfaces.ILibVLCFactory;

public class LibVLCFactory implements ILibVLCFactory {
    static {
        FactoryManager.registerFactory(ILibVLCFactory.factoryId, new LibVLCFactory());
    }

    @Override
    public ILibVLC getFromOptions(Context context, java.util.List<String> options) {
        return new LibVLC(context, options);
    }

    @Override
    public ILibVLC getFromContext(Context context) {
        return new LibVLC(context, null);
    }
}
