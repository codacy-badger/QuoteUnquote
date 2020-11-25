package com.github.jameshnsears.quoteunquote.configure.fragment.event;

import android.content.Context;

import com.github.jameshnsears.quoteunquote.preference.PreferenceFacade;

public class PreferenceEvent extends PreferenceFacade {
    public PreferenceEvent(final int widgetId, final Context applicationContext) {
        super(widgetId, applicationContext);
    }

    public boolean getEventDaily() {
        return preferenceHelper.getPreferenceBoolean(getPreferenceKey("EVENT_DAILY"), false);
    }

    public void setEventDaily(final boolean value) {
        preferenceHelper.setPreference(getPreferenceKey("EVENT_DAILY"), value);
    }

    public boolean getEventDeviceUnlock() {
        return preferenceHelper.getPreferenceBoolean(getPreferenceKey("EVENT_DEVICE_UNLOCK"), false);
    }

    public void setEventDeviceUnlock(final boolean value) {
        preferenceHelper.setPreference(getPreferenceKey("EVENT_DEVICE_UNLOCK"), value);
    }

    public int getEventDailyTimeMinute() {
        return preferenceHelper.getPreferenceInt(getPreferenceKey("EVENT_DAILY_MINUTE"));
    }

    public int getEventDailyTimeHour() {
        return preferenceHelper.getPreferenceInt(getPreferenceKey("EVENT_DAILY_HOUR"));
    }

    public void setEventDailyTimeHour(final int value) {
        preferenceHelper.setPreference(getPreferenceKey("EVENT_DAILY_HOUR"), value);
    }

    public void setEventDailyTimeMinute(final int value) {
        preferenceHelper.setPreference(getPreferenceKey("EVENT_DAILY_MINUTE"), value);
    }
}
