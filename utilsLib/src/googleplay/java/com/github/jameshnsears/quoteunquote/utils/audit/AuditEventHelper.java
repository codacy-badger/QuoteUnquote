package com.github.jameshnsears.quoteunquote.utils.audit;

import android.app.Application;

import androidx.annotation.NonNull;

import com.github.jameshnsears.quoteunquote.utils.BuildConfig;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.Flags;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import java.util.concurrent.ConcurrentHashMap;

public final class AuditEventHelper {
    private static AuditEventHelper auditEventHelperSingleton;

    private AuditEventHelper(final Application application) {
        AppCenter.start(application, BuildConfig.APPCENTER_KEY, Analytics.class, Crashes.class);
    }

    public static synchronized void createInstance(@NonNull final Application application) {
        if (auditEventHelperSingleton == null) {
            auditEventHelperSingleton = new AuditEventHelper(application);
        }
    }

    public static void auditEvent(@NonNull final String auditEvent, @NonNull final ConcurrentHashMap<String, String> properties) {
        Analytics.trackEvent(auditEvent, properties, Flags.NORMAL);
    }
}
