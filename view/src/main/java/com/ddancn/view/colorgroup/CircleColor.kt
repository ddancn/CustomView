package com.ddancn.view.colorgroup

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.ddancn.view.R

/**
 * @author ddan.zhuang
 * @date 2019/12/24
 * 一个圆形的颜色选择项，选中时有额外边框
 */
class CircleColor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint: Paint

    var radius = 0f

    private var isChosen = false
    private var isLight: Boolean

    init {
        // 从xml中获取属性
        val array = context.obtainStyledAttributes(attrs, R.styleable.CircleColor)
        paint.color = array.getColor(R.styleable.CircleColor_color, 0xff0000)
        array.recycle()
        // 设置选中时边框的paint
        strokePaint = Paint(paint)
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 3f
        // 如果是浅色，把边框设成灰色
        isLight = isLight(paint.color)
        if (isLight) {
            strokePaint.color = Color.LTGRAY
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 设置宽高为比radius更大，给画被选中时的边框留出位置
        setMeasuredDimension(radius.toInt() * 2 + 20, radius.toInt() * 2 + 20)
    }

    override fun onDraw(canvas: Canvas?) {
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()
        // 画圆
        canvas?.drawCircle(width / 2, height / 2, radius, paint)
        // 如果是浅色，给一个灰色的描边，不然看不清楚
        if (isLight) {
            canvas?.drawCircle(width / 2, height / 2, radius - 1, strokePaint)
        }
        // 如果被选中，画边框，不然看不清楚
        if (isChosen) {
            canvas?.drawCircle(width / 2, height / 2, radius + 2 + 6, strokePaint)
        }
    }

    /**
     * 设置圆和选中时边框颜色
     */
    fun setColor(@ColorInt c: Int) {
        paint.color = c
        strokePaint.color = c
        // 如果是浅色，边框设成灰色
        isLight = isLight(paint.color)
        if (isLight) {
            strokePaint.color = Color.LTGRAY
        }
        invalidate()
    }

    /**
     * 设置选中状态
     */
    fun setChosen(chosen: Boolean) {
        isChosen = chosen
        invalidate()
    }

    /**
     * 将RGB转化为YUV，判断颜色的深浅
     */
    private fun isLight(@ColorInt color: Int): Boolean {
        return color.red * 0.299 + color.green * 0.587 + color.blue * 0.114 >= 192
    }
}