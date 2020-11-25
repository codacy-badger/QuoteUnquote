package com.github.jameshnsears.quoteunquote.cloud;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CloudFavouritesTest {
    private static final String LOG_TAG = CloudFavouritesTest.class.getSimpleName();
    private final Gson gson = new Gson();
    private CloudFavourites cloudFavourites;

    private static String getRequestReceive() {
        final RequestReceive requestReceive = new RequestReceive();
        requestReceive.code = "4EXRqu8N68";
        final Gson gson = new Gson();
        return gson.toJson(requestReceive);
    }

    @Before
    public void setUp() {
        cloudFavourites = new CloudFavourites();
    }

    @After
    public void shutdown() {
        cloudFavourites.shutdown();
    }

    @Test
    public void assert01Save() {
        assertTrue("", cloudFavourites.save(getRequestSave()));
    }

    @Test
    public void assert02Crc() {
        final String localCode = CloudFavouritesHelper.getLocalCode();
        Log.d(LOG_TAG, localCode);

        assertEquals("", 10, localCode.length());

        assertTrue("", CloudFavouritesHelper.isRemoteCodeValid("dcb9pNXX9e"));
        assertTrue("", CloudFavouritesHelper.isRemoteCodeValid("p6veqZY633"));

        assertFalse("", CloudFavouritesHelper.isRemoteCodeValid("dcb9pNXX9a"));
    }

    @Test
    public void assert03IsInternetAvailable() {
        assertTrue("", cloudFavourites.isInternetAvailable());
    }

    @Test
    public void assert04ReceiveUnknownCode() {
        final RequestReceive requestReceive = new RequestReceive();
        requestReceive.code = "dcb9pNXX9e";

        final List<String> digests = cloudFavourites.receive(
                CloudFavourites.TIMEOUT,
                gson.toJson(requestReceive));
        assertEquals("", 0, digests.size());
    }

    @Test
    public void assert05ReceiveKnownCode() {
        // give gcloud time to publish - as it varies
        final List<String> actual = cloudFavourites.receive(
                30,
                getRequestReceive());
        assertEquals("", 2, actual.size());

        final ArrayList<String> expected = new ArrayList<>();
        expected.add("d0");
        expected.add("d1");

        assertEquals("", expected, actual);
    }

    private String getRequestSave() {
        final RequestSave requestSave = new RequestSave();

        requestSave.code = "4EXRqu8N68";

        final ArrayList<String> digests = new ArrayList<>();
        digests.add("d0");
        digests.add("d1");
        requestSave.digests = digests;

        return gson.toJson(requestSave);
    }
}
