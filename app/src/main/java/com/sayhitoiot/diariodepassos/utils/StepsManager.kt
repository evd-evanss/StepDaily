package com.sayhitoiot.piechart.util

import android.content.Context
import android.content.SharedPreferences

object StepsManager {
    private var sp: SharedPreferences? = null
    private const val PEDOMETER_SENSOR_COUNT = "pedometer_sensor_count" // Número de passos lidos pelo pedômetro
    private const val PEDOMETER_STEP_COUNT = "pedometer_step_count" // Passos
    private const val PEDOMETER_STEP_ANCHOR = "pedometer_step_anchor" // Ponto de partida
    private const val PEDOMETER_UPDATE_TIME = "pedometer_update_time" // Hora da última gravação

    private fun getSharedPreferences(context: Context): SharedPreferences? {
        if (sp == null) {
            sp = context.getSharedPreferences("Pedometer", Context.MODE_PRIVATE)
        }
        return sp
    }

    private fun putLong(
        context: Context,
        key: String?,
        value: Long
    ) {
        sp = getSharedPreferences(context)
        sp!!.edit().putLong(key, value).apply()
    }

    private fun getLong(
        context: Context,
        key: String?,
        defValue: Long
    ): Long {
        sp = getSharedPreferences(context)
        return sp!!.getLong(key, defValue)
    }

    private fun putInt(context: Context, key: String?, value: Int) {
        sp = getSharedPreferences(context)
        sp!!.edit().putInt(key, value).apply()
    }

    private fun getInt(context: Context, key: String?, defValue: Int): Int {
        sp = getSharedPreferences(context)
        return sp!!.getInt(key, defValue)
    }

    fun putSensorCount(context: Context, sensorCount: Int) {
        putInt(context, PEDOMETER_SENSOR_COUNT, sensorCount)
    }

    fun getSensorCount(context: Context): Int {
        return getInt(context, PEDOMETER_SENSOR_COUNT, 0)
    }

    fun putStepCount(context: Context, stepCount: Int) {
        putInt(context, PEDOMETER_STEP_COUNT, stepCount)
    }

    fun getStepCount(context: Context): Int {
        return getInt(context, PEDOMETER_STEP_COUNT, 0)
    }

    fun putStepAnchor(context: Context, stepAnchor: Int) {
        putInt(context, PEDOMETER_STEP_ANCHOR, stepAnchor)
    }

    fun getStepAnchor(context: Context): Int {
        return getInt(context, PEDOMETER_STEP_ANCHOR, 0)
    }

    fun putUpdateTime(context: Context, updateTime: Long) {
        putLong(context, PEDOMETER_UPDATE_TIME, updateTime)
    }

    fun getUpdateTime(context: Context): Long {
        return getLong(context, PEDOMETER_UPDATE_TIME, 0)
    }
}