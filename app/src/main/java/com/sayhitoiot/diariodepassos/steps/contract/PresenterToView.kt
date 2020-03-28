package com.sayhitoiot.diariodepassos.steps.contract

interface PresenterToView {

    fun onCreate()
    fun didFinishInitializeViews()
    fun requestStartCounter()
    fun onResume()
    fun setPermission(status: Boolean)
}