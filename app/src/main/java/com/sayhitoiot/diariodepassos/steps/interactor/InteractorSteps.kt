package com.sayhitoiot.diariodepassos.steps.interactor

import android.content.Context
import com.sayhitoiot.diariodepassos.steps.contract.InteractorToPresenter
import com.sayhitoiot.diariodepassos.steps.contract.PresenterToInteractor
import com.sayhitoiot.piechart.util.StepsManager
import java.text.DecimalFormat

class InteractorSteps(
    private val context: Context,
    private val presenter: PresenterToInteractor
) : InteractorToPresenter {

    companion object {
        private const val THOUSAND = 1000
        private const val RELATION_CAL = 40F
        private const val RELATION_KM = 800F
    }

    override fun fetchSteps() {
        presenter.didFetchSteps(StepsManager.getStepCount(context))
    }

    override fun fetchCalcs() {
        presenter.didFetchCalcs(getCal(), getKm())
    }

    private fun getCal(): String{
        val x = (((StepsManager.getStepCount(context)) * RELATION_CAL)) / THOUSAND
        return DecimalFormat("0.#").format(x).toString()
    }

    private fun getKm() : String{
        val x = (((StepsManager.getStepCount(context)) * RELATION_KM) / THOUSAND) / THOUSAND
        return DecimalFormat("0.#").format(x).toString()
    }

}