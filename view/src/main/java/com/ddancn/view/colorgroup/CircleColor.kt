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

/**
 * @author ddan.zhuang
 * @date 2019/12/24
 * 一个圆形的颜色选择项，选中时有额外边框
 */
class CircleColor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var radius = 0f
    var strokeWidth = 0f
    var strokeGap = 0f
    var anim = AnimParam()

    private var isChosen = false
    private var isLight: Boolean

    init {
        // 设置选中时边框的paint
        strokePaint.style = Paint.Style.STROKE
        // 如果是浅色，把边框设成灰色
        isLight = isLight(paint.color)
        if (isLight) {
            strokePaint.color = Color.LTGRAY
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 设置宽高为比radius更大，给画被选中时的边框留出位置
        val length = ((radius + strokeGap + strokeWidth) * 2).toInt() + 10
        setMeasuredDimension(length, length)
    }

    override fun onDraw(canvas: Canvas?) {
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()
        // 画圆
        canvas?.drawCircle(width / 2, height / 2, radius, paint)
        // 如果是浅色，给一个灰色的描边，不然看不清楚
        if (isLight) {
            strokePaint.strokeWidth = 3f
            canvas?.drawCircle(width / 2, height / 2, radius - 1, strokePaint)
        }
        // 如果被选中，画边框
        if (isChosen) {
            strokePaint.strokeWidth = strokeWidth
            canvas?.drawCircle(
                width / 2,
                height / 2,
                radius + strokeGap + strokeWidth / 2,
                strokePaint
            )
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
        // 选中项缩小动画
        if (chosen && anim.animType == AnimParam.AnimType.BOUNCE) {
            animate().scaleX(anim.animScale).scaleY(ANIM_SCALE).duration = anim.animDuration / 2
            postDelayed({ animate().scaleX(1f).scaleY(1f).duration = anim.animDuration / 2 }, anim.animDuration / 2)
        }
    }

    /**
     * 将RGB转化为YUV，判断颜色的深浅
     */
    private fun isLight(@ColorInt color: Int): Boolean {
        return color.red * 0.299 + color.green * 0.587 + color.blue * 0.114 >= 192
    }


}