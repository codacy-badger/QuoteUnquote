package com.github.jameshnsears.quoteunquote.database.history;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {PreviousEntity.class, FavouriteEntity.class, ReportedEntity.class},
        version = 1)
public abstract class AbstractHistoryDatabase extends RoomDatabase {
    @NonNull
    public static final String DATABASE_NAME = "history.db";
    @Nullable
    private static AbstractHistoryDatabase historyDatabase;

    @NonNull
    public static AbstractHistoryDatabase getDatabase(@NonNull final Context context) {
        synchronized (AbstractHistoryDatabase.class) {
            if (historyDatabase == null) {
                historyDatabase = Room.databaseBuilder(context,
                        AbstractHistoryDatabase.class, DATABASE_NAME)
                        .build();
            }
            return historyDatabase;
        }
    }

    @NonNull
    public abstract PreviousDAO contentDAO();

    @NonNull
    public abstract FavouriteDAO favouritesDAO();

    @NonNull
    public abstract ReportedDAO reportedDAO();
}
