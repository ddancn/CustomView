package com.ddancn.view.circularprogress

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import com.ddancn.view.R
import kotlin.math.min


/**
 * @author ddan.zhuang
 * @date 2023/2/16
 */
class CircularProgress(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var strokeWidth = 0f
    private var color = 0

    private var progress = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CircularProgress)
        strokeWidth = array.getDimension(R.styleable.CircularProgress_cg_strokeWidth, 10f)
        color = array.getColor(R.styleable.CircularProgress_cg_color, Color.parseColor("#008577"))
        array.recycle()

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        paint.color = color
        paint.strokeCap = Paint.Cap.ROUND

        bgPaint.style = Paint.Style.STROKE
        bgPaint.strokeWidth = strokeWidth
        bgPaint.color = Color.LTGRAY
    }

    fun start(progress: Float, duration: Long = 0): ObjectAnimator =
        ObjectAnimator.ofFloat(this, "progress", 0f, progress).apply {
            if (duration == 0L)
                this.duration = (progress * 10).toLong() + 100
            this.interpolator = OvershootInterpolator(1f)
            this.start()
        }

    override fun onDraw(canvas: Canvas?) {
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()
        // 留出边框范围
        val spare = strokeWidth / 2

        // 画背景
        canvas?.drawArc(
            spare, spare, width - spare, height - spare,
            0f, 360f, false, bgPaint
        )

        // 画进度条
        canvas?.drawArc(
            spare, spare, width - spare, height - spare,
            90f, (progress * 3.6).toFloat(), false, paint
        )
    }

}