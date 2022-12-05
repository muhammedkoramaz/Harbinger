package com.sn.harbinger.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtil {

    fun getYesterday(): Date {
        return Date(Date().time - (1000 * 60 * 60 * 24))
    }

    fun getToday(): Date {
        return Date()
    }

    fun remainingDay(endDate: String): Long {
        val end = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(endDate)
        val millionSeconds = end!!.time - Calendar.getInstance().timeInMillis
        return TimeUnit.MILLISECONDS.toDays(millionSeconds)
    }

}