package com.sayhitoiot.diariodepassos.steps.contract

interface ViewToPresenter {
    fun requestInitializeViews()
    fun requestRenderGraphDefault()
    fun requestRenderViewsDefault()
    fun setActionViews()
    fun requestPermissions()
    fun updateGraph(stepCount: Int)
    fun refreshGraph(stepCount: Int)
    fun updateCalcViews(cal: String, km: String)
    fun updateRangeRed(stepCount: Int)
    fun updateRangeBlue(stepCount: Int)
    fun updateRangeGreen(stepCount: Int)
    fun renderButton(running: Boolean)

}