package com.github.jameshnsears.quoteunquote.database.quotation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.github.jameshnsears.quoteunquote.BuildConfig;

@Database(
        entities = {QuotationEntity.class},
        version = 1)
public abstract class AbstractQuotationDatabase extends RoomDatabase {
    @Nullable
    private static AbstractQuotationDatabase quotationDatabase;

    @NonNull
    public static AbstractQuotationDatabase getDatabase(@NonNull final Context context) {
        String dbName = "quotations.db.dev";

        if (BuildConfig.USE_PROD_DB) {
            dbName = "quotations.db.prod";
        }

        synchronized (AbstractQuotationDatabase.class) {
            if (quotationDatabase == null) {
                quotationDatabase = Room.databaseBuilder(context,
                        AbstractQuotationDatabase.class, dbName)
                        .createFromAsset(dbName)
                        .build();
            }
            return quotationDatabase;
        }
    }

    public abstract QuotationDAO quotationsDAO();
}
