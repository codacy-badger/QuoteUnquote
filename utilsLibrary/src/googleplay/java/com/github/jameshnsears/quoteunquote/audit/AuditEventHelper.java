package com.github.jameshnsears.quoteunquote.audit;

import android.app.Application;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.Flags;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import java.util.concurrent.ConcurrentHashMap;

public final class AuditEventHelper {
    private static AuditEventHelper auditEventHelperSingleton;

    private AuditEventHelper(final Application application, final String appCenterKey) {
        AppCenter.start(application, appCenterKey, Analytics.class, Crashes.class);
    }

    public static synchronized void createInstance(final Application application, final String appCenterKey) {
        if (auditEventHelperSingleton == null) {
            auditEventHelperSingleton = new AuditEventHelper(application, appCenterKey);
        }
    }

    public static void auditEvent(final String auditEvent, final ConcurrentHashMap<String, String> properties) {
        Analytics.trackEvent(auditEvent, properties, Flags.NORMAL);
    }
}
