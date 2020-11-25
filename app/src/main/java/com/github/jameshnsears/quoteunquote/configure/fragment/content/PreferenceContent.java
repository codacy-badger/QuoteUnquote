package com.github.jameshnsears.quoteunquote.configure.fragment.content;

import android.content.Context;

import com.github.jameshnsears.quoteunquote.preference.PreferenceFacade;
import com.github.jameshnsears.quoteunquote.utils.ContentSelection;

import timber.log.Timber;

public class PreferenceContent extends PreferenceFacade {
    public PreferenceContent(final int widgetId, final Context applicationContext) {
        super(widgetId, applicationContext);
    }

    public String getContentSelectionAuthorName() {
        return preferenceHelper.getPreferenceString("CONTENT_AUTHOR_NAME");
    }

    public void setContentSelectionAuthorName(final String value) {
        preferenceHelper.setPreference("CONTENT_AUTHOR_NAME", value);
    }

    public String getContentFavouritesLocalCode() {
        return preferenceHelper.getPreferenceString(getPreferenceKey(0, "CONTENT_FAVOURITES_LOCAL_CODE"));
    }

    public void setContentFavouritesLocalCode(final String value) {
        preferenceHelper.setPreference(getPreferenceKey(0, "CONTENT_FAVOURITES_LOCAL_CODE"), value);
    }

    public String getContentSelectionSearchText() {
        return preferenceHelper.getPreferenceString(getPreferenceKey("CONTENT_SEARCH_TEXT"));
    }

    public void setContentSelectionSearchText(final String value) {
        preferenceHelper.setPreference(getPreferenceKey("CONTENT_SEARCH_TEXT"), value);
    }

    public ContentSelection getContentSelection() {
        if (preferenceHelper.getPreferenceBoolean(getPreferenceKey("CONTENT_AUTHOR"), false)) {
            return ContentSelection.AUTHOR;
        }

        if (preferenceHelper.getPreferenceBoolean(getPreferenceKey("CONTENT_FAVOURITES"), false)) {
            return ContentSelection.FAVOURITES;
        }

        if (preferenceHelper.getPreferenceBoolean(getPreferenceKey("CONTENT_SEARCH"), false)) {
            return ContentSelection.SEARCH;
        }

        return ContentSelection.ALL;
    }

    public void setContentSelection(final ContentSelection contentSelection) {
        switch (contentSelection) {
            case ALL:
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_ALL"), true);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_AUTHOR"), false);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_FAVOURITES"), false);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_SEARCH"), false);
                break;

            case AUTHOR:
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_ALL"), false);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_AUTHOR"), true);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_FAVOURITES"), false);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_SEARCH"), false);
                break;

            case FAVOURITES:
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_ALL"), false);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_AUTHOR"), false);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_FAVOURITES"), true);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_SEARCH"), false);
                break;

            case SEARCH:
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_ALL"), false);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_AUTHOR"), false);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_FAVOURITES"), false);
                preferenceHelper.setPreference(getPreferenceKey("CONTENT_SEARCH"), true);
                break;
        }
    }
}
