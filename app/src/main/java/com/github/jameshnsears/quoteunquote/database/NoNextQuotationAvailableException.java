package com.github.jameshnsears.quoteunquote.database;

import androidx.annotation.NonNull;

import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

public class NoNextQuotationAvailableException extends Exception {
    @NonNull
    public final ContentSelection contentSelection;

    public NoNextQuotationAvailableException(@NonNull final ContentSelection theContentSelection) {
        super("NoNextQuotationAvailableException: " + theContentSelection.toString());
        this.contentSelection = theContentSelection;
    }
}
