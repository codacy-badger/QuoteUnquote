package com.github.jameshnsears.quoteunquote.cloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;


public class CloudFavouritesHelper {
    @Nullable
    private static String localCode;

    @NonNull
    public static synchronized String getLocalCode() {
        if (localCode == null) {
            final String rootCode = RandomStringUtils.randomAlphanumeric(8);
            final String crc = new String(Hex.encodeHex(DigestUtils.md5(rootCode)));
            localCode = rootCode + crc.substring(0, 2);
        }
        return localCode;
    }

    public static boolean isRemoteCodeValid(@NonNull final String remoteCode) {
        final String rootCode = remoteCode.substring(0, 8);
        final String expectedCRC = new String(Hex.encodeHex(DigestUtils.md5(rootCode)));
        return remoteCode.endsWith(expectedCRC.substring(0, 2));
    }

    @NonNull
    public static String receiveRequest(@NonNull final String remoteCode) {
        final RequestReceive requestReceive = new RequestReceive();
        requestReceive.code = remoteCode;
        return new Gson().toJson(requestReceive);
    }

    @NonNull
    public static String sendRequest(@NonNull final RequestSave requestSave) {
        Gson gson = new Gson();
        return gson.toJson(requestSave);
    }
}
