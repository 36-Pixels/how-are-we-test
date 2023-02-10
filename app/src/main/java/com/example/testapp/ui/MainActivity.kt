package com.example.testapp.ui

import android.os.Bundle
import android.text.InputFilter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.testapp.R
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.model.InputData
import com.example.testapp.ui.utils.EditTextMinMaxFilter
import com.example.testapp.ui.utils.SnackBar
import com.example.testapp.ui.utils.showSnackbar
import com.example.testapp.viewmodels.ActivityMainViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel: ActivityMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // set input filter (number 0-100)
        mBinding.inputValue.filters = arrayOf<InputFilter>(EditTextMinMaxFilter(0, 100))

        // button listener
        mBinding.inputEnter.setOnClickListener {
            val value = mBinding.inputValue.text.toString()

            if (value.isEmpty()) {
                showSnackbar(getString(R.string.input_err), SnackBar.ERROR)
                return@setOnClickListener
            }

            // get number
            val nr = value.toInt()

            // add number
            mViewModel.onAddValue(nr)

            // clear input
            mBinding.inputValue.text.clear()

            // show success
            showSnackbar(getString(R.string.input_success, nr), SnackBar.SUCCESS)
        }

        // observe values (redraw graph every time values change)
        mViewModel.values.observe(this) {
            mBinding.output.text = it.toString()

            setUpLineChart()
            setDataToLineChart(it)
        }
    }

    /**
     * Persist data, so they won't get lost when user leaves an app
     */

    override fun onStop() {
        super.onStop()

        mViewModel.persistData()
    }

    private fun setUpLineChart() {
        with(mBinding.chart) {
            animateX(150, Easing.EaseInSine)
            description.isEnabled = false

            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1F
            xAxis.valueFormatter = MyAxisFormatter()

            axisRight.isEnabled = false
            extraRightOffset = 30f

            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.textSize = 15F
            legend.form = Legend.LegendForm.LINE
        }
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {
        // just a formatter of X-axis legend
        // just showing an index
        override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
            return value.toInt().toString()
        }
    }

    private fun setDataToLineChart(input: List<InputData>) {
        // map raw data into graph entries
        val entries = input.mapIndexed { index, inputData ->
            Entry(index.toFloat(), inputData.value.toFloat())
        }

        // create a data set from entries
        val dataSet = LineDataSet(entries, "Numbers")
        dataSet.lineWidth = 1f
        dataSet.valueTextSize = 12f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.red)
        dataSet.color = ContextCompat.getColor(this, R.color.red)

        val lineData = LineData(dataSet)
        mBinding.chart.data = lineData

        mBinding.chart.invalidate()
    }
}