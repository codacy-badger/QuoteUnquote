package com.github.jameshnsears.quoteunquote;

import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.jameshnsears.quoteunquote.database.NoNextQuotationAvailableException;
import com.github.jameshnsears.quoteunquote.database.quotation.QuotationEntity;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.spy;

@Ignore

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class FavouriteButtonPressedTest extends DatabaseTestHelper {
    @Test
    public void isNextQuotationFavourite() {
        insertTestDataSet01();

        setDefaultQuotation();

        final QuoteUnquoteModelFake quoteUnquoteModelSpy = spy(quoteUnquoteModel);
        doReturn(false).when(quoteUnquoteModelSpy).isRadioButtonFavouriteSelected(eq(WIDGET_ID));

        assertEquals(
                "",
                0,
                quoteUnquoteModelSpy.countPrevious(WIDGET_ID, ContentSelection.FAVOURITES));

        assertEquals(
                "",
                Integer.valueOf(0),
                quoteUnquoteModelSpy.countFavourites().blockingGet());

        final QuotationEntity nextQuotation = quoteUnquoteModelSpy.getNext(WIDGET_ID, ContentSelection.ALL);

        assertFalse("", quoteUnquoteModelSpy.isFavourite(WIDGET_ID, nextQuotation.digest));

        // make a favourite
        quoteUnquoteModelSpy.toggleFavourite(WIDGET_ID, nextQuotation.digest);

        assertEquals(
                "",
                Integer.valueOf(1),
                quoteUnquoteModelSpy.countFavourites().blockingGet());

        assertTrue(
                "",
                quoteUnquoteModelSpy.isFavourite(WIDGET_ID,
                        quoteUnquoteModelSpy.getNext(WIDGET_ID, ContentSelection.ALL).digest));

        // remove the favourite, but mark as a previous
        quoteUnquoteModelSpy.toggleFavourite(WIDGET_ID, nextQuotation.digest);

        assertEquals(
                "",
                Integer.valueOf(0),
                quoteUnquoteModelSpy.countFavourites().blockingGet());

        assertEquals(
                "",
                0,
                quoteUnquoteModelSpy.countPrevious(WIDGET_ID, ContentSelection.FAVOURITES));

        assertEquals(
                "",
                1,
                quoteUnquoteModelSpy.countPrevious(WIDGET_ID, ContentSelection.ALL));
    }

    @Test
    public void makeSomeFavouritesFromContentAll() throws NoNextQuotationAvailableException {
        insertTestDataSet01();
        insertTestDataSet02();

        final List<String> expectedDigestsList = new ArrayList<>();

        // make a favourite
        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        quoteUnquoteModel.toggleFavourite(
                WIDGET_ID,
                quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);
        expectedDigestsList.add(quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);

        assertEquals(
                "",
                Integer.valueOf(1),
                quoteUnquoteModel.countFavourites().blockingGet());

        // make a favourite
        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        quoteUnquoteModel.toggleFavourite(
                WIDGET_ID,
                quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);
        expectedDigestsList.add(quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);

        // don't make a favourite
        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);

        // make a favourite
        quoteUnquoteModel.setNext(WIDGET_ID, ContentSelection.ALL, false);
        quoteUnquoteModel.toggleFavourite(
                WIDGET_ID,
                quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);
        expectedDigestsList.add(quoteUnquoteModel.getNext(WIDGET_ID, ContentSelection.ALL).digest);

        assertEquals(
                "",
                Integer.valueOf(3),
                quoteUnquoteModel.countFavourites().blockingGet());

        // the database returns in prior order
        Collections.reverse(expectedDigestsList);

        assertEquals(
                "",
                expectedDigestsList,
                quoteUnquoteModel.getFavourites());
    }
}
