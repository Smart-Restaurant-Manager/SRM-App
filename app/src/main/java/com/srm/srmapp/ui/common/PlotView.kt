package com.srm.srmapp.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import timber.log.Timber


/**
* View used to plot data 
*/
class PlotView(context: Context, attributes: AttributeSet) : View(context, attributes), GestureDetector.OnGestureListener {
    private val gesture = GestureDetectorCompat(context, this)
    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 3.0f
        isAntiAlias = true
    }
    private var yData: List<Float> = listOf()
    private var xLabel: List<String> = listOf()
    private var rectfList: List<RectF> = listOf()
    private var ww = width.toFloat()
    private var hh = height.toFloat()
    private val textPaint = Paint().apply {
        textSize = 40f
    }
    private val textPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val xpad = (paddingLeft + paddingRight).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()
        ww = w.toFloat() - xpad
        hh = h.toFloat() - ypad
        Timber.d("Size $ww $hh $w $h")
    }


    fun setDate(Y: List<Float>, label: List<String>) {
        assert(Y.size == label.size)
        yData = Y
        xLabel = label
        postInvalidate()
    }

    private fun calcRectangles() {
        val maxY = yData.maxOfOrNull { it } ?: return
        val barBottomPadding = 10f
        val barTopPadding = 10f
        val barWidht = ww / yData.size
        var xOffset = 0f
        val rectfList = mutableListOf<RectF>()
        for (y in yData) {
            val barHeight = (y / maxY) * hh - barTopPadding
            val rect = RectF(xOffset, hh - barHeight, xOffset + barWidht, hh - barBottomPadding)
            rectfList.add(rect)
            xOffset += barWidht
        }
        this.rectfList = rectfList
    }

    override fun invalidate() {
        super.invalidate()
        calcRectangles()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for ((rect, label) in rectfList.zip(xLabel)) {
            canvas.drawRoundRect(rect, 20f, 20f, paint)
            canvas.drawTextOnPath(label, textPath.apply {
                reset()
                val center = rect.centerX() + textPaint.textSize / 2
                moveTo(center, rect.bottom - 20)
                lineTo(center, rect.top)
            }, 0f, 0f, textPaint)
        }
        invalidate()
    }


    private var downListener: (Int) -> Unit = {}
    fun setOnDownListener(downListener: (Int) -> Unit) {
        this.downListener = downListener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gesture.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onDown(p0: MotionEvent): Boolean {
        Timber.d("Tocuh ${p0.action} ${p0.x} ${p0.y}")
        for (i in rectfList.indices) {
            val rec = rectfList[i]
            if (rec.contains(p0.x, p0.y)) {
                downListener.invoke(i)
                return true
            }
        }
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {

    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }
}