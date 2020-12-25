package com.github.jameshnsears.quoteunquote.database;


import androidx.arch.core.executor.testing.CountingTaskExecutorRule;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;

import com.github.jameshnsears.quoteunquote.QuoteUnquoteModelDouble;
import com.github.jameshnsears.quoteunquote.database.history.AbstractDatabaseHistory;
import com.github.jameshnsears.quoteunquote.database.quotation.QuotationEntity;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.robolectric.shadows.util.SQLiteLibraryLoader;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DatabaseHelper {
    protected static final int WIDGET_ID = 1;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule
    public CountingTaskExecutorRule countingTaskExecutorRule = new CountingTaskExecutorRule();
    protected QuoteUnquoteModelDouble quoteUnquoteModel;

    @Before
    public void setUp() {
        SQLiteLibraryLoader.load();

        ApplicationProvider.getApplicationContext().deleteDatabase(AbstractDatabaseHistory.DATABASE_NAME);
        quoteUnquoteModel = new QuoteUnquoteModelDouble();
    }

    @After
    public void tearDown() {
        quoteUnquoteModel.shutdown();
    }

    protected void insertTestDataSet01() {
        final List<QuotationEntity> quotationEntityList = new ArrayList<>();
        quotationEntityList.add(
                new QuotationEntity(
                        DatabaseRepository.DEFAULT_QUOTATION_DIGEST, "a0", "q0"
                ));
        quotationEntityList.add(
                new QuotationEntity(
                        "d1", "a1", "q1"
                ));
        quoteUnquoteModel.getDatabaseRepositoryFake().insertQuotations(quotationEntityList);
    }

    protected void insertTestDataSet02() {
        final List<QuotationEntity> quotationEntityList = new ArrayList<>();
        quotationEntityList.add(
                new QuotationEntity(
                        "d2", "a2", "q1"
                ));
        quotationEntityList.add(
                new QuotationEntity(
                        "d3", "a2", "q3"
                ));
        quotationEntityList.add(
                new QuotationEntity(
                        "d4", "a4", "q1"
                ));
        quoteUnquoteModel.getDatabaseRepositoryFake().insertQuotations(quotationEntityList);
    }

    protected void insertTestDataSet03() {
        final List<QuotationEntity> quotationEntityList = new ArrayList<>();
        quotationEntityList.add(
                new QuotationEntity(
                        "d5", "a5", "q1"
                ));
        quotationEntityList.add(
                new QuotationEntity(
                        "d6", "a2", "q6"
                ));
        quoteUnquoteModel.getDatabaseRepositoryFake().insertQuotations(quotationEntityList);
    }

    protected void setDefaultQuotation() {
        quoteUnquoteModel.setDefaultQuotation(WIDGET_ID);

        assertEquals(DatabaseRepository.DEFAULT_QUOTATION_DIGEST,
                quoteUnquoteModel.getNext(
                        WIDGET_ID, ContentSelection.ALL).digest);

        assertEquals(
                1,
                quoteUnquoteModel.countPrevious(WIDGET_ID, ContentSelection.ALL));
    }
}
