package com.btc.wallet.utils

import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DateTimeUtilsTest {

    @Test
    fun `getHourIntervalsOfPeriod should generate correct list`() {
        val start = DateTime.parse("2019-10-05T22:45:11+00:00")
        val end = DateTime.parse("2019-10-06T03:05:11+00:00")
        val result = DateTimeUtils.getHourIntervalsOfPeriod(start, end)

        val expectedList = mutableListOf<DateTime>(
            DateTime.parse("2019-10-05T23:00:00.000+00:00"),
            DateTime.parse("2019-10-06T00:00:00.000+00:00"),
            DateTime.parse("2019-10-06T01:00:00.000+00:00"),
            DateTime.parse("2019-10-06T02:00:00.000+00:00"),
            DateTime.parse("2019-10-06T03:00:00.000+00:00")
        )
        assertEquals(expectedList, result)
    }

    @Test
    fun `getHourIntervalsOfPeriod should generate correct list when period is within an hour`() {
        val start = DateTime.parse("2019-10-05T21:45:11+00:00")
        val end = DateTime.parse("2019-10-05T22:10:11+00:00")
        val result = DateTimeUtils.getHourIntervalsOfPeriod(start, end)

        val expectedList = mutableListOf<DateTime>(
            DateTime.parse("2019-10-05T22:00:00.000+00:00")
        )
        assertEquals(expectedList, result)
    }
}