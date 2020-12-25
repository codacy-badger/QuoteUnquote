package com.github.jameshnsears.quoteunquote.configure.fragment.content;

import android.app.Application;

import com.github.jameshnsears.quoteunquote.database.DatabaseRepository;

import static org.mockito.Mockito.mock;

public class ContentViewModelDouble extends ContentViewModel {
    public ContentViewModelDouble(final DatabaseRepository databaseRepository) {
        super(mock(Application.class));
        this.databaseRepository = databaseRepository;
    }
}
