package com.github.jameshnsears.quoteunquote.database

import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Test
import org.slf4j.LoggerFactory

@Ignore

class RandomQuotationsTest {
    private val logger = LoggerFactory.getLogger(RandomQuotationsTest::class.java)

    @Test
    fun getRandomIndex() {
        val databaseRepository = DatabaseRepository()
        val stringList: MutableList<String> = ArrayList()

        stringList.add("1")
        stringList.add("2")
        stringList.add("3")
        stringList.add("4")
        stringList.add("5")

        for (i in stringList.indices) {
            val rndIndex = databaseRepository.getRandomIndex(stringList)
            logger.debug("" + rndIndex)
            if (rndIndex < 0 || rndIndex > stringList.size) {
                fail("")
            }
        }
    }
}
