package com.example.chartexample

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.PathInterpolator
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_chart_drag_selector.view.*
import kotlin.math.abs


class ChartDragSelectorView : RelativeLayout {

    private lateinit var root: ViewGroup
    private var lastX = 0f
    private var limitLeft = 0
    private var limitRight = 0
    private var padding = 0

    private var onMoveToIndexListener: OnMoveToIndexListener? = null

    companion object {
        const val DATA_COUNT = 7
    }

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
        LayoutInflater.from(context).inflate(R.layout.view_chart_drag_selector, this, true)
        isClickable = true

        post {
            padding = (root.width - width * DATA_COUNT) / (DATA_COUNT - 1)
        }
    }

    fun setContent(weekday: String?, date: String?) {
        tv_date.text = date?: ""
        tv_weekday.text = weekday?: ""
    }

    fun setRoot(viewRoot: ViewGroup) {
        root = viewRoot
    }
    fun moveTo(index: Int) {
        if(index >= DATA_COUNT) {
            return
        }

        val offset = index * (padding + width)
        animationToIndex(index)
    }

    fun setOnMoveToIndexListener(l: OnMoveToIndexListener) {
        onMoveToIndexListener = l
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event == null) {
            return super.onTouchEvent(event)
        }
        limitLeft = root.left
        limitRight = root.right

        val x= event.x
        val offsetX: Int
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = x.minus(lastX).toInt()
                if(left + offsetX >= limitLeft && right + offsetX <= limitRight) {
                    layout((left + offsetX), top, (right + offsetX), bottom)
                }
                Log.e("AAAAA" ,
                        "left = " + left
                                + "\tx = " + x
                                + "\toffsetX = " + offsetX
                                + "\tlastX = " + lastX)
            }
            MotionEvent.ACTION_UP -> {
                val targetIndex = calculateClosestX(left)
                animationToIndex(targetIndex)
                onMoveToIndexListener?.onMove(targetIndex)
            }
        }
        return super.onTouchEvent(event)
    }

    //计算距离 @left 最近的index
    private fun calculateClosestX(left: Int): Int {
        var minDistance: Int = Int.MAX_VALUE
        var minIndex  = 0

        for(i in 0..DATA_COUNT) {
            if(abs(i * (padding + width) - left) < minDistance) {
                minDistance = abs(i * (padding + width) - left)
                minIndex = i
            }
        }
        return minIndex
    }

    private fun animationToIndex(index: Int) {
//        ObjectAnimator.ofFloat(this, "translationX", (index * (padding + width)).toFloat())
//            .apply {
//                duration = 200
//                start()
//            }

        layout(index * (padding + width), top, index * (padding + width) + width, bottom)


//        animate()
//            .setInterpolator(LinearInterpolator())
//            .setDuration(200)
//            .translationX((index * (padding + width)).toFloat())
//            .start()

    }

    interface OnMoveToIndexListener {
        fun onMove(index: Int)
    }
}