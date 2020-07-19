package com.example.chartexample

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.view_frame_chart_view.view.*


class FrameChartView : RelativeLayout {

    private var mDataList: ArrayList<Entry>?= null
    lateinit var selectorView: ChartDragSelectorView
    private var set: LineDataSet? = null
    constructor(context: Context?) : super(context) {initView(context)}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        initView(context)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context?) {
        LayoutInflater.from(context).inflate(R.layout.view_frame_chart_view, this, true)
        selectorView = ChartDragSelectorView(context)

        initChartView()
        initAxis()
        initSelector()
    }

    private fun initChartView() {

        chartView.setBackgroundColor(Color.WHITE)
        chartView.setTouchEnabled(true)
        chartView.isDragEnabled = false
        chartView.setScaleEnabled(false)
        chartView.setPinchZoom(false)
        chartView.description.isEnabled =false
        chartView.legend.isEnabled = false

        chartView.setViewPortOffsets(60f,60f,60f,150f)
        chartView.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val entry = e as TrendDataChatEntry
                val index = entry.index
                selectorView.moveTo(index)
                entry.trendDataEntity?.let {
                    selectorView.setContent(it.week, it.date)
                }
            }
        })

        val mv = ChartMarkerView(context, R.layout.item_chart_marker)
        mv.chartView = chartView
        chartView.marker = mv
    }

    private fun initAxis() {
        val xAxis = chartView.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.isEnabled = true
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.yOffset = 10f
        xAxis.mAxisRange = 7f
        xAxis.valueFormatter = ChartXAxisWeekdayFormatter()
        xAxis.textColor = Color.parseColor("#c7c7d7")
        xAxis.textSize = 12f
        xAxis.setDrawLabels(true)

        val yAxis = chartView.axisLeft
        chartView.axisRight.isEnabled = false
        yAxis.setDrawAxisLine(false)
        yAxis.enableGridDashedLine(10f,10f, 20f)
        yAxis.gridColor = Color.parseColor("#c7c7d7")
        yAxis.gridLineWidth = 0.5f
        yAxis.axisMaximum = 100f
        yAxis.axisMinimum = 0f
        yAxis.setLabelCount(2,false)
        yAxis.granularity = 1f
        yAxis.textColor = Color.parseColor("#c7c7d7")
        yAxis.textSize = 10f
        yAxis.setDrawZeroLine(false)

        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
    }

    private fun initSelector() {
        val params = FrameLayout.LayoutParams(
            ((DensityUtils.getScreenWidth() * 1.0) / 7 - 10).toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.leftMargin = 5
        selectorView.setRoot(this)
        selectorView.setOnMoveToIndexListener(object : ChartDragSelectorView.OnMoveToIndexListener{
            override fun onMove(index: Int) {
                chartView.highlightValue(index.toFloat(), 0)
                val entry = set?.values?.get(index) as TrendDataChatEntry
                entry.trendDataEntity?.let {
                    selectorView.setContent(it.week, it.date)
                }
            }
        })
        selectorView.setContent("二","12/22")
        fl_selector_container.addView(selectorView, params)
    }

    fun loadWith(dataList: ArrayList<Entry>) {
        mDataList = dataList

        if (chartView.data != null && chartView.data.dataSetCount > 0) {
            set = chartView.data.getDataSetByIndex(0) as LineDataSet
            set?.values = mDataList
            chartView.data.notifyDataChanged()
            chartView.notifyDataSetChanged()
        } else {
            set = LineDataSet(mDataList, "")
        }
        set?.let { set ->
            set.setDrawIcons(false)
            set.color = Color.parseColor("#01c2c3")
            set.lineWidth = 3f
            set.mode = LineDataSet.Mode.CUBIC_BEZIER
            set.cubicIntensity = 0.17f
            set.setDrawCircles(false)
            set.valueFormatter
            set.setDrawValues(false)
            set.setDrawHorizontalHighlightIndicator(false)
            set.setDrawVerticalHighlightIndicator(false)

            // set color of filled area
            set.setDrawFilled(true)
            set.fillFormatter = IFillFormatter { _, _ ->
                chartView.axisLeft.axisMinimum
            }
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(context, R.drawable.chart_fill_gradient)
                set.fillDrawable = drawable
            } else {
                set.fillColor = Color.parseColor("#4001c2c3")
            }
        }


        val lineData = LineData(set)
        chartView.data = lineData
    }
}