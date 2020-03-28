package com.sayhitoiot.diariodepassos.utils
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import com.sayhitoiot.diariodepassos.services.StepCountService
import com.sayhitoiot.piechart.util.StepsManager
import com.sayhitoiot.piechart.util.TimeUtil

object ServiceManager {

    fun isSupportStepCounter(context: Context): Boolean {
        if (!context.packageManager
                .hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
        ) {
            return false
        }
        val sensorManager =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        return stepCounterSensor != null
    }

    fun startPedometerService(context: Context) {
        val intent = Intent(context, StepCountService::class.java)
        context.startService(intent)
    }

    fun stopPedometerService(context: Context) {
        val intent = Intent(context, StepCountService::class.java)
        context.stopService(intent)
    }

    fun handleStepCount(context: Context, currentSteps: Int): Boolean {
        val updateTime: Long = StepsManager.getUpdateTime(context)
        val currentTime = System.currentTimeMillis()
        if (updateTime == 0L) {
            val stepCount = 0
            StepsManager.putSensorCount(context, currentSteps)
            StepsManager.putStepCount(context, stepCount)
            StepsManager.putStepAnchor(context, currentSteps)
            StepsManager.putUpdateTime(context, currentTime)
            return true
        }
        val sensorCount: Int = StepsManager.getSensorCount(context)
        if (currentSteps == sensorCount) {
            return false
        }
        var stepAnchor: Int = StepsManager.getStepAnchor(context)
        var stepCount: Int = StepsManager.getStepCount(context)

        if (TimeUtil.isTheSameDay(updateTime, currentTime)) {
            if (currentSteps < stepAnchor || currentSteps < stepCount) {
                if (currentSteps < stepAnchor) {
                    stepCount += currentSteps
                    stepAnchor = currentSteps
                } else {
                    stepCount += currentSteps - stepAnchor
                    stepAnchor = currentSteps
                }
            } else {
                val stepOffset = currentSteps - stepAnchor
                if (stepOffset < stepCount) {
                    stepCount += stepOffset
                    stepAnchor = currentSteps
                } else {
                    stepCount = stepOffset
                }
            }
        } else {

            stepAnchor = currentSteps
            stepCount = 0
        }
        StepsManager.putSensorCount(context, currentSteps)
        StepsManager.putStepCount(context, stepCount)
        StepsManager.putStepAnchor(context, stepAnchor)
        StepsManager.putUpdateTime(context, currentTime)
        return true
    }

    /**
     * Obtem passos gravados
     */
    fun getStepCount(context: Context): Int {
        return StepsManager.getStepCount(context)
    }

    fun getUpdateTime(context: Context): Long {
        return StepsManager.getUpdateTime(context)
    }

    /**
     * Redefinir dados da contagem de passos
     */
    fun resetStepCount(context: Context) {
        StepsManager.putSensorCount(context, 0)
        StepsManager.putStepCount(context, 0)
        StepsManager.putStepAnchor(context, 0)
        StepsManager.putUpdateTime(context, 0)
    }

}
