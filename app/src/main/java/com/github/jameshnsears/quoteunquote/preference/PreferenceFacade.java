package com.github.jameshnsears.quoteunquote.preference;

import android.content.Context;

import java.util.Locale;

public class PreferenceFacade {
    public static final String PREFERENCES_FILENAME = "QuoteUnquote";
    private final int widgetId;
    private final Context context;
    protected PreferenceHelper preferenceHelper;

    public PreferenceFacade(final int theWidgetId, final Context applicationContext) {
        this.widgetId = theWidgetId;
        this.context = applicationContext;
        this.preferenceHelper = new PreferenceHelper(PREFERENCES_FILENAME, applicationContext);
    }

    public static void empty(final Context context) {
        PreferenceHelper.empty(PREFERENCES_FILENAME, context);
    }

    public static void empty(final Context context, final int widgetId) {
        PreferenceHelper.empty(PREFERENCES_FILENAME, context, widgetId);
    }

    protected String getPreferenceKey(final String key) {
        return String.format(Locale.ENGLISH, "%d:%s", widgetId, key);
    }

    protected String getPreferenceKey(final int widgetId, final String key) {
        return String.format(Locale.ENGLISH, "%d:%s", widgetId, key);
    }
}
