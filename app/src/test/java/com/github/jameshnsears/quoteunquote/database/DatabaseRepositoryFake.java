package com.github.jameshnsears.quoteunquote.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.github.jameshnsears.quoteunquote.database.history.AbstractHistoryDatabase;
import com.github.jameshnsears.quoteunquote.database.quotation.AbstractQuotationDatabase;
import com.github.jameshnsears.quoteunquote.database.quotation.QuotationEntity;
import com.guness.robolectric.sqlite.library.SQLiteLibraryLoader;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Single;

public final class DatabaseRepositoryFake extends DatabaseRepository {
    private static final String LOG_TAG = DatabaseRepositoryFake.class.getSimpleName();

    public DatabaseRepositoryFake() {
        SQLiteLibraryLoader.load();

        abstractQuotationDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AbstractQuotationDatabase.class)
                .allowMainThreadQueries()
                .build();

        quotationDAO = abstractQuotationDatabase.quotationsDAO();

        abstractHistoryDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AbstractHistoryDatabase.class)
                .allowMainThreadQueries()
                .build();

        previousDAO = abstractHistoryDatabase.contentDAO();
        favouriteDAO = abstractHistoryDatabase.favouritesDAO();
        reportedDAO = abstractHistoryDatabase.reportedDAO();
    }

    public void insertQuotations(final List<QuotationEntity> quotationEntityList) {
        final ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (final QuotationEntity quotationEntity : quotationEntityList) {
            Log.d(LOG_TAG, "insertQuotation: " + quotationEntity.toString());

            final Future future = executorService.submit(() -> quotationDAO.insertQuotation(quotationEntity));

            try {
                future.get();
            } catch (ExecutionException | InterruptedException e) {
                Log.e(LOG_TAG, e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();
    }

    @Override
    @NonNull
    public Single<Integer> countAll() {
        return quotationDAO.countAll();
    }

    public Integer countReported() {
        return reportedDAO.countReported();
    }

    @NonNull
    public QuotationEntity getQuotation(@NonNull final String digest) {
        return quotationDAO.getQuotation(digest);
    }

    public void close() {
        abstractQuotationDatabase.close();
        abstractHistoryDatabase.close();
    }
}
