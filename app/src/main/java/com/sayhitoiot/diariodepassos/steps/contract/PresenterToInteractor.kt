package com.sayhitoiot.diariodepassos.steps.contract

interface PresenterToInteractor {
    fun didFetchCalcs(cal: String, km: String)
    fun didFetchSteps(stepCount: Int)
}