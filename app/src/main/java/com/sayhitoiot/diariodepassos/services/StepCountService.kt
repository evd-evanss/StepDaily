package com.sayhitoiot.diariodepassos.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.sayhitoiot.diariodepassos.R
import com.sayhitoiot.diariodepassos.steps.view.StepsActivity
import com.sayhitoiot.diariodepassos.utils.ServiceManager
import com.sayhitoiot.piechart.util.StepsManager

class StepCountService : Service(), SensorEventListener {

    companion object {

        private const val NOTIFICATION_ID: Int = com.sayhitoiot.diariodepassos.R.string.app_name
        private const val CHANNEL_ID: String = "StepCountService"
        private const val CHANNEL_NAME: String = "Step Count background service"

        const val ACTION_STEP_CHANGE = "com.sayhitoiot.diariodepassos.ACTION_STEP_CHANGE"

    }

    private var mStepCounterSensor: Sensor? = null
    private var mSensorManager: SensorManager? = null
    private var mNotificationManager: NotificationManager? = null
    private var mSteps = 0


    override fun onCreate() {
        super.onCreate()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mStepCounterSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mSensorManager != null) {
            mSensorManager?.unregisterListener(this)
        }
        stopForeground(true)
        Log.d(
            "EXIT",
            "onDestroy()"
        )
    }

    @ExperimentalUnsignedTypes
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        registerSensor()
        startForegroundNotification()
        return START_STICKY
    }

    private fun registerSensor() {

        if (ServiceManager.isSupportStepCounter(this)) {
            mSensorManager?.registerListener (
                this,
                mStepCounterSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val stepCounterChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE)
        stepCounterChannel.lightColor = Color.BLUE
        stepCounterChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(stepCounterChannel)
    }

    @ExperimentalUnsignedTypes
    private fun getNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID )
        val intent = Intent(this, StepsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val contentIntent =
            PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val steps = StepsManager.getStepCount(this)
        return notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_steps)
            .setPriority(PRIORITY_MIN)
            .setContentTitle("$steps passos")
            .setContentText("3000 passos a percorrer")
            .setProgress(3000, steps, false)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(contentIntent)
            .build()
    }

    @ExperimentalUnsignedTypes
    private fun startForegroundNotification() {
        startForeground(NOTIFICATION_ID, getNotification())
    }

    @ExperimentalUnsignedTypes
    private fun updateNotification() {
        mNotificationManager?.notify(
            NOTIFICATION_ID,
            getNotification()
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    @ExperimentalUnsignedTypes
    override fun onSensorChanged(event: SensorEvent) {
        mSteps = event.values[0].toInt()
        Log.i("steps", "total de passos= $mSteps")

        if (ServiceManager.handleStepCount(this, mSteps)) {
            updateNotification()
        }
        sendBroadcast(Intent(ACTION_STEP_CHANGE))

    }

}