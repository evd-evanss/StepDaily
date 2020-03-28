package com.sayhitoiot.diariodepassos.steps.presenter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.sayhitoiot.diariodepassos.services.StepCountService
import com.sayhitoiot.diariodepassos.steps.contract.InteractorToPresenter
import com.sayhitoiot.diariodepassos.steps.contract.PresenterToInteractor
import com.sayhitoiot.diariodepassos.steps.contract.PresenterToView
import com.sayhitoiot.diariodepassos.steps.contract.ViewToPresenter
import com.sayhitoiot.diariodepassos.steps.interactor.InteractorSteps
import com.sayhitoiot.diariodepassos.utils.ServiceManager
import com.sayhitoiot.diariodepassos.utils.ServiceManager.startPedometerService
import com.sayhitoiot.diariodepassos.utils.ServiceManager.stopPedometerService

class PresenterSteps(
    private val view: ViewToPresenter,
    private val context: Context
) : PresenterToView, PresenterToInteractor {

    private var mSteps = 0
    private var isRunning = false

    private val interactor: InteractorToPresenter by lazy {
        InteractorSteps(context, this)
    }

    private var permission = false

    override fun onCreate() {
        view.requestInitializeViews()
        view.requestPermissions()
    }

    override fun setPermission(status: Boolean) {
        permission = true
    }

    override fun onResume() {
        if(permission) {
            interactor.fetchSteps()
            interactor.fetchCalcs()
        }
    }

    override fun didFinishInitializeViews() {
        view.setActionViews()
        view.requestRenderViewsDefault()
        view.requestRenderGraphDefault()
    }

    override fun requestStartCounter() {
        if(permission) {
            Log.d("permission_activity", "permitido")
            if(isRunning) {
                stopService()
            } else {
                startService()
            }
        }
    }

    private fun startService() {
        isRunning = true
        Log.d("service_running", "start service")
        val intentFilter = IntentFilter(StepCountService.ACTION_STEP_CHANGE)
        context.registerReceiver(stepsChangeReceiver, intentFilter)
        startPedometerService(context)
        view.renderButton(isRunning)
    }
    private fun stopService() {
        isRunning = false
        context.unregisterReceiver(stepsChangeReceiver)
        stopPedometerService(context)
        view.renderButton(isRunning)
    }

    private var stepsChangeReceiver: BroadcastReceiver? = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            if (StepCountService.ACTION_STEP_CHANGE == intent.action) {
                if (ServiceManager.handleStepCount(context, mSteps)) {
                    interactor.fetchSteps()
                    interactor.fetchCalcs()
                }
            }

        }

    }

    override fun didFetchSteps(stepCount: Int) {
        requestUpdateGraph(stepCount)
    }

    private fun requestUpdateGraph(steps: Int) {
        view.updateRangeRed(steps)
    }

    override fun didFetchCalcs(cal: String, km: String) {
        view.updateCalcViews(cal, km)
    }
}