package com.example.chartexample

import com.github.mikephil.charting.data.Entry

class TrendDataChatEntry(trendListDataModel: TrendListDataModel, val index: Int) : Entry() {
    var trendDataEntity: TrendListDataModel? = trendListDataModel

    init {
        x = index * 1.0f
        y = trendListDataModel.score?.times(1.0f) ?: 1f
    }
}