package com.github.jameshnsears.quoteunquote.database;

import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

public class NoNextQuotationAvailableException extends Exception {
    public final ContentSelection contentSelection;

    public NoNextQuotationAvailableException(final ContentSelection theContentSelection) {
        super("NoNextQuotationAvailableException: " + theContentSelection.toString());
        this.contentSelection = theContentSelection;
    }
}
