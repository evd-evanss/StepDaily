package com.sayhitoiot.diariodepassos.steps.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import com.sayhitoiot.diariodepassos.R
import com.sayhitoiot.diariodepassos.R.color.colorAccent
import com.sayhitoiot.diariodepassos.R.color.colorGray
import com.sayhitoiot.diariodepassos.steps.contract.PresenterToView
import com.sayhitoiot.diariodepassos.steps.contract.ViewToPresenter
import com.sayhitoiot.diariodepassos.steps.presenter.PresenterSteps
import com.sayhitoiot.diariodepassos.utils.RangeColors
import kotlinx.android.synthetic.main.activity_main.*

class StepsActivity : AppCompatActivity() , ViewToPresenter {

    companion object {
        private const val TARGET = 3000F
        private const val MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 100

    }
    private val presenter: PresenterToView by lazy {
        PresenterSteps(
            this,
            this
        )
    }

    private var textTitle: TextView? = null
    private var textCount: TextView? = null
    private var textCal: TextView? = null
    private var textKm: TextView? = null

    private var buttonStart: Button? = null

    private var graph: DecoView? = null

    lateinit var backgroundGraph: SeriesItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onCreate()
    }

    override fun requestPermissions() {
        this.runOnUiThread {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                        MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION
                    )
                }

            } else {
                presenter.setPermission(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun setActionViews() {
        this.runOnUiThread {
            buttonStart?.setOnClickListener { presenter.requestStartCounter() }
        }
    }

    override fun requestInitializeViews() {
        this.runOnUiThread{
            textTitle = stepCount_textView_goal
            textCount = stepCount_textView_count
            textCal = stepCount_textView_cal
            textKm = stepCount_textView_km
            buttonStart = stepCount_button_btn_start
            graph = stepCount_dynamicArcView_graph
            presenter.didFinishInitializeViews()
            buttonStart?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen))
            buttonStart?.text = "Iniciar"
        }
    }

    override fun renderButton(running: Boolean) {
        this.runOnUiThread {
            if(running){
                buttonStart?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                buttonStart?.text = "Parar"
            }

            if(!running){
                buttonStart?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen))
                buttonStart?.text = "Iniciar"
            }
        }
    }

    override fun requestRenderViewsDefault() {
        this.runOnUiThread {
            textCount?.text = "0"
            textCal?.text = "0 cal"
            textKm?.text = "0 km"
            textKm?.text = "0 km"
        }
    }

    override fun requestRenderGraphDefault() {
        this.runOnUiThread {
            backgroundGraph = SeriesItem.Builder(ContextCompat.getColor(this,
                R.color.colorAccent))
                .setRange(
                    0f,
                    TARGET,
                    TARGET
                )
                .setInitialVisibility(true)
                .setLineWidth(50f)
                .build()
            graph?.addSeries(backgroundGraph)
        }
    }

    override fun updateGraph(stepCount: Int) {
        this.runOnUiThread {
            textCount?.text = stepCount.toString()

            graph?.addSeries(RangeColors().redColor(this))
            graph?.addSeries(RangeColors().blueColor(this))
            graph?.addSeries(RangeColors().greenColor(this))

            //if(stepCount in 0..999) {
                val indexOne: Int? = graph?.addSeries(RangeColors().redColor(this))
                if(indexOne!=null) {
                    graph?.addEvent(
                        DecoEvent.Builder(stepCount.toFloat()).setIndex(indexOne).setDelay(1000).build()
                    )
                }
            //}
            //if(stepCount in 1000..1999) {
                val indexTwo: Int? = graph?.addSeries(RangeColors().blueColor(this))
                if(indexTwo!=null) {
                    graph?.addEvent(
                        DecoEvent.Builder(stepCount.toFloat()).setIndex(indexTwo).setDelay(1000).build()
                    )
                }
            //}
            //if(stepCount in 2000..3000) {
                val indexThree: Int? = graph?.addSeries(RangeColors().greenColor(this))
                if(indexThree!=null) {
                    graph?.addEvent(
                        DecoEvent.Builder(stepCount.toFloat()).setIndex(indexThree).setDelay(1000).build()
                    )
                }
            //}

        }
    }

    override fun updateRangeRed(stepCount: Int) {
        this.runOnUiThread {
            textCount?.text = stepCount.toString()

            graph?.addSeries(RangeColors().redColor(this))
            val indexOne: Int? = graph?.addSeries(RangeColors().redColor(this))
            if(indexOne!=null) {
                graph?.addEvent(
                    DecoEvent.Builder(stepCount.toFloat()).setIndex(indexOne).setDelay(1000).build()
                )
            }
        }

    }

    override fun updateRangeBlue(stepCount: Int) {
        this.runOnUiThread {
            textCount?.text = stepCount.toString()

            val indexTwo: Int? = graph?.addSeries(RangeColors().blueColor(this))
            if(indexTwo!=null) {
                graph?.addEvent(
                    DecoEvent.Builder(stepCount.toFloat()).setIndex(indexTwo).setDelay(1000).build()
                )
            }

        }
    }

    override fun updateRangeGreen(stepCount: Int) {
        this.runOnUiThread {
            textCount?.text = stepCount.toString()

            graph?.addSeries(RangeColors().greenColor(this))

            val indexThree: Int? = graph?.addSeries(RangeColors().greenColor(this))
            if(indexThree!=null) {
                graph?.addEvent(
                    DecoEvent.Builder(stepCount.toFloat()).setIndex(indexThree).setDelay(1000).build()
                )
            }
        }
    }

    override fun refreshGraph(stepCount: Int) {
        this.runOnUiThread {
            textCount?.text = stepCount.toString()

            graph?.addSeries(RangeColors().redColor(this))
            graph?.addSeries(RangeColors().blueColor(this))
            graph?.addSeries(RangeColors().greenColor(this))

            //if(stepCount in 0..999) {
            val indexOne: Int? = graph?.addSeries(RangeColors().redColor(this))
            if(indexOne!=null) {
                graph?.addEvent(
                    DecoEvent.Builder(stepCount.toFloat()).setIndex(indexOne).setDelay(1000).build()
                )
            }
            //}
            //if(stepCount in 1000..1999) {
            val indexTwo: Int? = graph?.addSeries(RangeColors().blueColor(this))
            if(indexTwo!=null) {
                graph?.addEvent(
                    DecoEvent.Builder(stepCount.toFloat()).setIndex(indexTwo).setDelay(1000).build()
                )
            }
            //}
            //if(stepCount in 2000..3000) {
            val indexThree: Int? = graph?.addSeries(RangeColors().greenColor(this))
            if(indexThree!=null) {
                graph?.addEvent(
                    DecoEvent.Builder(stepCount.toFloat()).setIndex(indexThree).setDelay(1000).build()
                )
            }
            //}

        }
    }

    override fun updateCalcViews(cal: String, km: String) {
        this.runOnUiThread {
            textCal?.text = "$cal cal"
            textKm?.text = "$km km"
        }
    }
}
