package com.github.jameshnsears.quoteunquote.cloud;

import android.content.Context;

import com.github.jameshnsears.quoteunquote.audit.AuditEventHelper;
import com.github.jameshnsears.quoteunquote.configure.fragment.content.PreferenceContent;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ConcurrentHashMap;


public class CloudFavouritesHelper {
    private static String localCode;

    public static String getLocalCode(final Context context) {
        final PreferenceContent preferenceContent = new PreferenceContent(0, context);

        if ("".equals(preferenceContent.getContentFavouritesLocalCode())) {
            preferenceContent.setContentFavouritesLocalCode(CloudFavouritesHelper.getLocalCode());
            localCode = preferenceContent.getContentFavouritesLocalCode();
        }

        return localCode;
    }

    public static void auditFavourites(final String auditEvent, final String code) {
        final ConcurrentHashMap<String, String> properties = new ConcurrentHashMap<>();
        properties.put("code", code);
        AuditEventHelper.auditEvent(auditEvent, properties);
    }

    public static synchronized String getLocalCode() {
        if (localCode == null) {
            final String rootCode = RandomStringUtils.randomAlphanumeric(8);
            final String crc = new String(Hex.encodeHex(DigestUtils.md5(rootCode)));
            localCode = rootCode + crc.substring(0, 2);
        }
        return localCode;
    }

    public static void setLocalCode(final Context context) {
        final PreferenceContent preferenceContent = new PreferenceContent(0, context);
        preferenceContent.setContentFavouritesLocalCode(CloudFavouritesHelper.getLocalCode());
    }

    public static boolean isRemoteCodeValid(final String remoteCode) {
        final String rootCode = remoteCode.substring(0, 8);
        final String expectedCRC = new String(Hex.encodeHex(DigestUtils.md5(rootCode)));
        return remoteCode.endsWith(expectedCRC.substring(0, 2));
    }

    public static String receiveRequest(final String remoteCode) {
        final RequestReceive requestReceive = new RequestReceive();
        requestReceive.code = remoteCode;
        final Gson gson = new Gson();
        return gson.toJson(requestReceive);
    }
}
