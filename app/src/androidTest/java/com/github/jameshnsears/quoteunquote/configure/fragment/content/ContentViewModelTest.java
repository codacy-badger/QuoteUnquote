package com.github.jameshnsears.quoteunquote.configure.fragment.content;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.jameshnsears.quoteunquote.DatabaseTestHelper;
import com.github.jameshnsears.quoteunquote.QuoteUnquoteModelFake;
import com.github.jameshnsears.quoteunquote.database.NoNextQuotationAvailableException;
import com.github.jameshnsears.quoteunquote.database.history.AbstractHistoryDatabase;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ContentViewModelTest extends DatabaseTestHelper {

    private ContentViewModel contentViewModel;

    @Before
    public void setUpDatabases() {
        ApplicationProvider.getApplicationContext().deleteDatabase(AbstractHistoryDatabase.DATABASE_NAME);

        // insert's against in memory database defined in QuoteUnquoteModelFake
        quoteUnquoteModel = new QuoteUnquoteModelFake();
        contentViewModel = new ContentViewModelFake(quoteUnquoteModel.getDatabaseRepositoryFake());
    }

    @After
    public void shutdown() {
        contentViewModel.shutdown();
    }

    @Test
    public void countAll() {
        insertTestDataSet01();
        assertEquals(
                "",
                2,
                contentViewModel.countAll().blockingGet().intValue());

        insertTestDataSet02();
        assertEquals(
                "",
                5,
                contentViewModel.countAll().blockingGet().intValue());
    }

    @Test
    public void authors() {
        insertTestDataSet01();
        insertTestDataSet02();
        insertTestDataSet03();

        assertEquals(
                "",
                5,
                contentViewModel.authors().blockingGet().size());
    }

    @Test
    public void authorsSorted() {
        insertTestDataSet01();
        insertTestDataSet02();
        insertTestDataSet03();

        assertEquals(
                "",
                "a0",
                contentViewModel.authorsSorted(contentViewModel.authors().blockingGet()).get(0));

        assertEquals(
                "",
                "a5",
                contentViewModel.authorsSorted(contentViewModel.authors().blockingGet()).get(4));
    }

    @Test
    public void countAuthorQuotations() {
        insertTestDataSet01();
        insertTestDataSet02();
        insertTestDataSet03();

        contentViewModel.authorPOJOList = contentViewModel.authors().blockingGet();

        assertEquals(
                "",
                3,
                contentViewModel.countAuthorQuotations("a2"));
    }

    @Test
    public void countQuotationWithText() {
        assertEquals("", 0, contentViewModel.countQuotationWithText("q1").intValue());

        insertTestDataSet01();
        insertTestDataSet02();
        insertTestDataSet03();

        assertEquals("", 4, contentViewModel.countQuotationWithText("q1").intValue());

    }

    @Test
    public void countFavouriteQuotations() {
        assertEquals("", 0, contentViewModel.countFavourites().blockingGet().intValue());
    }

    @Test
    public void savePayload() throws NoNextQuotationAvailableException {
        insertTestDataSet01();
        insertTestDataSet02();

        // 1624c314
        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        quoteUnquoteModel.toggleFavourite(
                WIDGET_ID,
                quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);

        // d1
        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        quoteUnquoteModel.toggleFavourite(
                WIDGET_ID,
                quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);

        assertTrue("", contentViewModel.getFavouritesToSend().contains("\"digests\":[\"d1\",\"1624c314\"]"));
    }
}
