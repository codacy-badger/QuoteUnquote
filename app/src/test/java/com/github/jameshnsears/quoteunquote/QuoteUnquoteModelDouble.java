package com.github.jameshnsears.quoteunquote;

import android.content.Context;

import com.github.jameshnsears.quoteunquote.database.DatabaseRepositoryDouble;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import org.mockito.Mockito;

import java.util.List;

import io.reactivex.Single;

public class QuoteUnquoteModelDouble extends QuoteUnquoteModel {
    public QuoteUnquoteModelDouble() {
        super();
        this.context = Mockito.mock(Context.class);
        databaseRepository = new DatabaseRepositoryDouble();
    }

    public DatabaseRepositoryDouble getDatabaseRepositoryFake() {
        return (DatabaseRepositoryDouble) databaseRepository;
    }

    public void shutdown() {
        super.shutdown();
        getDatabaseRepositoryFake().close();
    }

    public Single<Integer> countFavourites() {
        return databaseRepository.countFavourites();
    }

    public Integer countReported() {
        return getDatabaseRepositoryFake().countReported();
    }

    public List<String> getPrevious(final int widgetId, final ContentSelection contentSelection) {
        return databaseRepository.getPrevious(widgetId, contentSelection);
    }

    public List<String> getFavourites() {
        return databaseRepository.getFavourites();
    }

    @Override
    public String getPreferencesTextSearch(final int widgetId) {
        return "q1";
    }

    @Override
    public ContentSelection getSelectedContentType(final int widgetId) {
        return ContentSelection.ALL;
    }
}
