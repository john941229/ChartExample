package com.example.chartexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.fastjson.JSON
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var dataList: ArrayList<TrendListDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DensityUtils.init(this)
    }

    override fun onResume() {
        super.onResume()
        initData()

        var data =ArrayList<Entry>()
        var i =0
        for(item in dataList) {
            data.add(TrendDataChatEntry(item, i))
            i++
            if(i == 7) {
                break
            }
        }

        if (data.isNotEmpty()) {
            chatView.loadWith(dataList = data)
        }
    }


    private fun initData() {
        val jsonString = getJson()
        dataList = ArrayList(JSON.parseArray(jsonString, TrendListDataModel::class.java))
    }

    private fun getJson(): String {
        val sb = StringBuilder()
        val am = this.assets
        try{
            val br = BufferedReader(InputStreamReader(am.open("list.json")))
            var next: String? = null
            do {
                next = br.readLine()
                if(next != null) {
                    sb.append(next)
                }
            } while (next != null)
        } catch (e: IOException) {
            e.printStackTrace()
            sb.delete(0, sb.length)
        }
        return sb.toString().trim()
    }
}