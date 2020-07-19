package com.example.chartexample

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class ChartXAxisWeekdayFormatter: ValueFormatter() {
    private val days = arrayOf("一", "二", "三", "四", "五", "六", "日")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}