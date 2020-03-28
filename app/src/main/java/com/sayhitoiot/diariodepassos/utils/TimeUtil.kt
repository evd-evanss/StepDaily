package com.sayhitoiot.piechart.util

import java.util.*


object TimeUtil {

    fun isTheSameDay(milliSeconds1: Long, milliSeconds2: Long): Boolean {
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        calendar1.timeInMillis = milliSeconds1
        calendar2.timeInMillis = milliSeconds2
        return calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR] && calendar1[Calendar.DAY_OF_YEAR] == calendar2[Calendar.DAY_OF_YEAR]
    }
}