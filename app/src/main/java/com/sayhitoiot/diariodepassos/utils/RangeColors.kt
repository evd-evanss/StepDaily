package com.sayhitoiot.diariodepassos.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.sayhitoiot.diariodepassos.R

class RangeColors() {

    private val redInitValue = 0f
    private val redFinalValue = 999f
    private val blueInitValue = 1000f
    private val blueFinalValue = 1999f
    private val greenInitValue = 2000f
    private val greenFinalValue = 3000f
    private val maxValue = 3000f

    internal fun redColor(context: Context) : SeriesItem {
        return SeriesItem.Builder(
            ContextCompat.getColor(context,
                R.color.colorPrimaryDark
            ))
            .setRange(
                redInitValue,
                maxValue,
                redInitValue
            )
            .setLineWidth(50f)
            .build()
    }

     fun blueColor(context: Context) : SeriesItem {
        return SeriesItem.Builder(
            ContextCompat.getColor(context,
                R.color.colorBlue
            ))
            .setRange(
                0f,
                maxValue,
                0f
            )
            .setLineWidth(50f)
            .build()
    }

    fun greenColor(context: Context) : SeriesItem {
        return SeriesItem.Builder(
            ContextCompat.getColor(context,
                R.color.colorGreen
            ))
            .setRange(
                0f,
                maxValue,
                0f
            )
            .setLineWidth(50f)
            .build()
    }

}