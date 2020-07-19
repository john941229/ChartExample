package com.example.chartexample

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.item_chart_marker.view.*

class ChartMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    var tvContent: TextView = tv_content

    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        if(e is TrendDataChatEntry) {
            tvContent.text = e.trendDataEntity?.score.toString()
        } else {
            tvContent.text = e?.y.toString()
        }

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(((-(width.toFloat()) / 2)), (-height + 24).toFloat())
    }
}