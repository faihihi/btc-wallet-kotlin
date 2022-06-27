package com.btc.wallet.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

object DateTimeUtils {
    val dateFormat = "yyyy-MM-dd"
    val dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ssZ"

    val formatter = DateTimeFormat.forPattern(dateTimeFormat)
    val dateFormatter = DateTimeFormat.forPattern(dateFormat)

    fun parseToUTCDateTime(dateTime: String): DateTime = formatter.parseDateTime(dateTime).withZone(DateTimeZone.UTC)
    fun parseToString(dateTime: DateTime): String = formatter.print(dateTime)

    fun getCurrentDateTime(): String = DateTime.now().toString(formatter)

    fun getHourIntervalsOfPeriod(startUTC: DateTime, endUTC: DateTime): MutableList<DateTime> {
        var initialHour = startUTC.hourOfDay().roundCeilingCopy()
        val untilHour = endUTC.hourOfDay().roundCeilingCopy()
        var intervalList = mutableListOf<DateTime>()

        if(initialHour.isEqual(untilHour)) intervalList.add(initialHour)
        else {
            while (initialHour.isBefore(untilHour)) {
                intervalList.add(initialHour)
                initialHour = initialHour.plusHours(1)
            }
        }
        return intervalList
    }
}