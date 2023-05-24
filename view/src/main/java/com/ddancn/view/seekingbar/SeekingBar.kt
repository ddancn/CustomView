package com.ddancn.view.seekingbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ddancn.view.R

/**
 * @author ddan.zhuang
 * @date 2023/5/15
 * @description 就是一个SeekBar
 */
class SeekingBar(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val thumbColor = Color.parseColor("#008577")
    private val thumbR = 15f

    private val barColor = Color.LTGRAY
    private val barHeight = 8f
    private val barWidth
        get() = width - 2 * thumbR

    /**
     * 进度条最大值
     */
    val max: Int

    /**
     * 是否有刻度
     */
    var tickMark = false
        set(value) {
            invalidate()
            field = value
        }

    /**
     * 一格刻度的值
     */
    var tickMarkStep: Int

    /**
     * 是否吸附刻度
     */
    var tickMarkAbsorb = false

    /**
     * 进度 in 0..max
     */
    var progress = 0
        set(value) {
            val limitValue = value.coerceIn(0, max)
            field = limitValue
            thumbX = barWidth / max * limitValue + thumbR
            onProgressChange?.invoke(progress)
            invalidate()
        }

    /**
     * 滑块所在的x坐标，随progress而变动
     */
    private var thumbX = 0f

    /**
     * 按下时判断是否按住了滑块（是否要拖动）
     */
    private var isDragging = false

    /**
     * 滑动时的监听
     */
    var onProgressChange: ((Int) -> Unit)? = null

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SeekingBar)
        max = array.getInt(R.styleable.SeekingBar_sb_max, 100)
        tickMark = array.getBoolean(R.styleable.SeekingBar_sb_tickMark, false)
        tickMarkStep = array.getInt(R.styleable.SeekingBar_sb_tickMark_step, max / 10)
        array.recycle()

        thumbX = thumbR

        thumbPaint.strokeCap = Paint.Cap.ROUND
        thumbPaint.strokeWidth = thumbR * 2
        thumbPaint.color = thumbColor

        barPaint.strokeCap = Paint.Cap.ROUND
        barPaint.strokeWidth = barHeight
        barPaint.color = barColor

        tickPaint.strokeWidth = 3f
        tickPaint.color = barColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = 50
        val desiredHeight = (thumbR * 2).toInt()
        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val w = width.toFloat()
        val h = (height / 2).toFloat()

        // 画进度条
        canvas?.drawLine(thumbR, h, w - thumbR, h, barPaint)

        // 画刻度
        if (tickMark) {
            for (i in 0..max / tickMarkStep) {
                val x = barWidth / max * i * tickMarkStep + thumbR
                canvas?.drawLine(x, h - 6, x, h + 6, tickPaint)
            }
        }

        // 画滑块
        canvas?.drawPoint(thumbX, h, thumbPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                val h = (height / 2).toFloat()
                // 点滑块位置，准备进行拖动。范围稍微扩大，使用更加友好
                if (x in around(thumbX, thumbR * 1.2f) && y in around(h, thumbR * 1.2f)) {
                    isDragging = true
                }
                // 点进度条位置，直接定位过去
                else if (y in h - thumbR..h + thumbR) {
                    progress = ((x - thumbR) / barWidth * max).toInt()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                if (isDragging) {
                    progress = ((x - thumbR) / barWidth * max).toInt()
                }
            }
            MotionEvent.ACTION_UP -> {
                isDragging = false
                // 吸附到最近的刻度
                if (tickMark && tickMarkAbsorb) {
                    val left = progress / tickMarkStep * tickMarkStep
                    val right = (progress / tickMarkStep + 1) * tickMarkStep
                    progress = if (progress - left < right - progress) left else right
                }
            }
        }
        return true
    }

    private fun around(x: Float, y: Float) = x - y..x + y

}
