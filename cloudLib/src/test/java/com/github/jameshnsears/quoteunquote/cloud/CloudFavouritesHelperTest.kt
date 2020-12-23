package com.github.jameshnsears.quoteunquote.cloud

import org.junit.Assert.*
import org.junit.Test

class CloudFavouritesHelperTest {
    @Test
    fun `that the local & remote codes are valid`() {
        assertTrue(CloudFavouritesHelper.isRemoteCodeValid(CloudFavouritesHelper.getLocalCode()))

        val localCode = CloudFavouritesHelper.getLocalCode()

        assertEquals("", 10, localCode.length.toLong())

        assertTrue("", CloudFavouritesHelper.isRemoteCodeValid("dcb9pNXX9e"))
        assertTrue("", CloudFavouritesHelper.isRemoteCodeValid("p6veqZY633"))
        assertFalse("", CloudFavouritesHelper.isRemoteCodeValid("dcb9pNXX9a"))
    }

    @Test
    fun `send a save request and then a receive request`() {
        CloudFavouritesHelper.sendRequest(RequestHelper.sendRequest())

        assertTrue(CloudFavouritesHelper.receiveRequest(RequestHelper.code).contains(RequestHelper.code))
    }
}