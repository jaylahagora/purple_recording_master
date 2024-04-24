package com.smartrecording.recordingplugin.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.xunison.recordingplugin.R;
import com.purple.iptv.player.models.ConnectionInfoModel;
import com.smartrecording.recordingplugin.model.LiveChannelModelforsc;
import com.smartrecording.recordingplugin.model.NotificationidstoreModel;
import com.smartrecording.recordingplugin.model.RecordingScheduleModel;


@Database(entities = {ConnectionInfoModel.class,
        RecordingScheduleModel.class,
         LiveChannelModelforsc.class, NotificationidstoreModel.class},
        version = 7)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;



    public abstract Daos.ConnectionInfoDao ConnectionInfoDao();



    public abstract Daos.RecordingScheduleDao recordingScheduleDao();



    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, context.getString(R.string.app_name))
                            .fallbackToDestructiveMigration()
//                            // allow queries on the main thread.
//                            // Don't do this on a real app! See PersistenceBasicSample for an example.
//                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            // Your migration strategy here
//            database.execSQL("ALTER TABLE LiveChannelModel  ADD COLUMN archive boolean default 0 NOT NULL");
//        }
//    };
}
