package com.github.jameshnsears.quoteunquote;

import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.jameshnsears.quoteunquote.database.DatabaseHelper;
import com.github.jameshnsears.quoteunquote.database.NoNextQuotationAvailableException;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@Ignore

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class QuoteUnquoteWidgetToolbarNextTest extends DatabaseHelper {
    @Test
    public void missingQuotationDigestProvidedByFavouritesReceive() {
        assertNull("", quoteUnquoteModel.getDatabaseRepositoryFake().getQuotation("blah"));
    }

    @Test
    public void contentTypeAll() throws NoNextQuotationAvailableException {
        insertTestDataSet01();

        setDefaultQuotation();

        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);

        assertEquals(
                "check that we're not using on DEFAULT_QUOTATION_DIGEST",
                "d1",
                quoteUnquoteModel.getNext(
                        WIDGET_ID, ContentSelection.ALL).digest);

        assertEquals(
                "make sure history contains correct ContentType",
                2,
                quoteUnquoteModel
                        .countPrevious(WIDGET_ID, ContentSelection.ALL));
    }

    @Test(expected = NoNextQuotationAvailableException.class)
    public void noMoreQuotations() throws NoNextQuotationAvailableException {
        insertTestDataSet01();

        setDefaultQuotation();

        try {
            quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        } catch (NoNextQuotationAvailableException e) {
            fail(e.getMessage());
        }


        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        fail("");
    }

    @Test
    public void contentTypeFavourites() throws NoNextQuotationAvailableException {
        insertTestDataSet01();
        insertTestDataSet02();

        quoteUnquoteModel.removeDatabaseEntriesForInstance(WIDGET_ID);

        final List<String> expectedFavouritesDigestList = new ArrayList<>();

        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        quoteUnquoteModel.toggleFavourite(
                WIDGET_ID,
                quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);
        expectedFavouritesDigestList.add(quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);

        assertEquals(
                "confirm correct # of favourites",
                Integer.valueOf(1), quoteUnquoteModel.countFavourites().blockingGet());
        assertEquals("", 0, quoteUnquoteModel.countPrevious(WIDGET_ID, ContentSelection.FAVOURITES));

        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);

        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        quoteUnquoteModel.toggleFavourite(
                WIDGET_ID,
                quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);
        expectedFavouritesDigestList.add(quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);

        assertEquals(
                "confirm correct # of favourites",
                Integer.valueOf(2), quoteUnquoteModel.countFavourites().blockingGet());

        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);

        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        quoteUnquoteModel.toggleFavourite(
                WIDGET_ID,
                quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);
        expectedFavouritesDigestList.add(quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);

        assertEquals(
                "confirm correct # of favourites",
                Integer.valueOf(3), quoteUnquoteModel.countFavourites().blockingGet());

        Collections.reverse(expectedFavouritesDigestList);

        assertEquals(
                "",
                expectedFavouritesDigestList,
                quoteUnquoteModel.getFavourites());

        // switch into ContentType.Favourites and move through them until we run out of favourite quotations
        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.FAVOURITES, false);
        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.FAVOURITES, false);
        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.FAVOURITES, false);
        try {
            quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.FAVOURITES, false);
            fail("");
        } catch (NoNextQuotationAvailableException e) {
            assertSame("", ContentSelection.FAVOURITES, e.contentSelection);
        }

        // make sure previous quotations same as available favourites
        final List<String> previousQuotations
                = quoteUnquoteModel.getPrevious(WIDGET_ID, ContentSelection.FAVOURITES);
        Collections.sort(previousQuotations);

        final List<String> favouriteQuotations
                = quoteUnquoteModel.getFavourites();
        Collections.sort(favouriteQuotations);

        assertEquals("", favouriteQuotations, previousQuotations);
    }

    @Test
    public void contentTypeAuthor() {
        insertTestDataSet01();
        insertTestDataSet02();
        insertTestDataSet03();

        setDefaultQuotation();

        final QuoteUnquoteModelDouble quoteUnquoteModelSpy = spy(quoteUnquoteModel);
        doReturn("a2").when(quoteUnquoteModelSpy).getPreferencesAuthorSearch(eq(WIDGET_ID));


        // user chooses a2 as author and keeps pressing new quotation

        // each time user selects a new author then the prior history is deleted
        quoteUnquoteModelSpy.deletePrevious(WIDGET_ID, ContentSelection.AUTHOR);

        // the default quotation should still be in the history
        assertEquals(
                "",
                1,
                quoteUnquoteModel.countPrevious(WIDGET_ID, ContentSelection.ALL));

        assertEquals(
                "",
                0,
                quoteUnquoteModel.countPrevious(WIDGET_ID, ContentSelection.AUTHOR));

        try {
            quoteUnquoteModelSpy.setNext(WIDGET_ID, ContentSelection.AUTHOR, false);
            assertEquals(
                    "",
                    1,
                    quoteUnquoteModel.countPrevious(WIDGET_ID, ContentSelection.AUTHOR));
        } catch (NoNextQuotationAvailableException e) {
            fail("");
        }

        try {
            quoteUnquoteModelSpy.setNext(WIDGET_ID, ContentSelection.AUTHOR, false);
            assertEquals(
                    "",
                    2,
                    quoteUnquoteModel.countPrevious(WIDGET_ID, ContentSelection.AUTHOR));
        } catch (NoNextQuotationAvailableException e) {
            fail("");
        }

        try {
            quoteUnquoteModelSpy.setNext(WIDGET_ID, ContentSelection.AUTHOR, false);
            assertEquals(
                    "",
                    3,
                    quoteUnquoteModel.countPrevious(WIDGET_ID, ContentSelection.AUTHOR));
        } catch (NoNextQuotationAvailableException e) {
            fail("");
        }

        try {
            quoteUnquoteModelSpy.setNext(WIDGET_ID, ContentSelection.AUTHOR, false);
            fail("");
        } catch (NoNextQuotationAvailableException e) {
            // user would see a Toast
            assertSame("", ContentSelection.AUTHOR, e.contentSelection);
        }

        assertEquals("", 3, quoteUnquoteModelSpy.countPreviousAuthor(WIDGET_ID));
    }

    @Test
    public void contentTypeQuotationText() {
        insertTestDataSet01();
        insertTestDataSet02();
        insertTestDataSet03();

        // using "q1" as the keyword from test data

        try {
            quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.SEARCH, false);
            quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.SEARCH, false);
            quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.SEARCH, false);
            quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.SEARCH, false);

            quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.SEARCH, false);
            fail("");
        } catch (NoNextQuotationAvailableException e) {
            assertSame("", ContentSelection.SEARCH, e.contentSelection);
        }

        assertEquals(
                "",
                4,
                quoteUnquoteModel.countPrevious(WIDGET_ID, ContentSelection.SEARCH));
    }
}
