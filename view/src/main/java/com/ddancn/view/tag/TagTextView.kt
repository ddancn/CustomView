package com.ddancn.view.tag

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.IntRange
import com.ddancn.view.R

/**
 * @author ddan.zhuang
 * @date 2020/1/9
 * 样式为常见tag的TextView，基础样式为圆角边框
 * 支持设置边框颜色和宽度、圆角大小、背景颜色、填充模式（无、边框颜色、指定颜色）
 * 默认边框颜色为文字颜色，背景颜色为边框颜色，无填充，自带左右padding
 */
class TagTextView(context: Context, attrs: AttributeSet?) : TextView(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderColor: Int
    private val backgroundColor: Int
    private val fillMode: FillMode
    private val borderWidth: Float
    private val radius: Float

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TagTextView)
        // 边框颜色，默认为文字颜色
        borderColor = array.getColor(R.styleable.TagTextView_borderColor, currentTextColor)
        // 背景颜色，默认为边框颜色
        backgroundColor = array.getColor(R.styleable.TagTextView_backgroundColor, borderColor)
        // 填充模式
        fillMode = FillMode.getMode(array.getColor(R.styleable.TagTextView_fillMode, 0))
        // 边框宽度
        borderWidth = array.getDimension(R.styleable.TagTextView_borderWidth, 3f)
        // 圆角大小
        radius = array.getDimension(R.styleable.TagTextView_radius, 8f)
        // 设置边距，防止线画到字上
        setPadding(10, 0, 10, 0)
        array.recycle()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        // 防止有半条线在view外面的情况
        val halfBorderWidth = borderWidth / 2
        // 画边框
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        canvas?.drawRoundRect(
            halfBorderWidth,
            halfBorderWidth,
            width - halfBorderWidth,
            height - halfBorderWidth,
            radius, radius,
            paint
        )
        // 如果需要，填充背景颜色
        if (fillMode != FillMode.NONE) {
            paint.color = backgroundColor
            paint.style = Paint.Style.FILL
            canvas?.drawRoundRect(
                halfBorderWidth,
                halfBorderWidth,
                width - halfBorderWidth,
                height - halfBorderWidth,
                radius, radius, paint
            )
        }
        // 最后再写上文字
        super.onDraw(canvas)
    }

    enum class FillMode(val value: Int) {
        // 不填充，即透明
        NONE(0),
        // 用边框颜色填充
        FILL(1),
        // 用指定颜色填充
        COLOR(2);

        companion object {
            fun getMode(@IntRange(from = 0, to = 2) value: Int): FillMode = when (value) {
                0 -> NONE
                1 -> FILL
                2 -> COLOR
                else -> NONE
            }
        }
    }
}