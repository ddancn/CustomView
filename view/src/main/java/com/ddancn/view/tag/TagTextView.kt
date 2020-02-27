package com.ddancn.view.tag

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.IntRange
import com.ddancn.view.R
import kotlin.math.max

/**
 * @author ddan.zhuang
 * @date 2020/1/9
 * 样式为常见tag的TextView，基础样式为圆角边框
 * 支持设置边框颜色和宽度、圆角大小、背景颜色、填充模式（边框，背景，全部）
 * 默认边框颜色为文字颜色，背景颜色为边框颜色，模式为边框，自带左右padding
 */
class TagTextView(context: Context, attrs: AttributeSet?) : TextView(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderColor: Int
    private val backgroundColor: Int
    private val fillMode: FillMode
    private val borderWidth: Float
    private val radius: Float
    private val rectF = RectF()

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TagTextView)
        // 边框颜色，默认为文字颜色
        borderColor = array.getColor(R.styleable.TagTextView_ttv_borderColor, currentTextColor)
        // 背景颜色，默认为边框颜色
        backgroundColor = array.getColor(R.styleable.TagTextView_ttv_backgroundColor, borderColor)
        // 填充模式
        fillMode = FillMode.getMode(array.getInt(R.styleable.TagTextView_ttv_fillMode, 0))
        // 边框宽度
        borderWidth = array.getDimension(R.styleable.TagTextView_ttv_borderWidth, 3f)
        // 圆角大小
        radius = array.getDimension(R.styleable.TagTextView_ttv_borderRadius, 8f)
        // 设置边距，防止线画到字上
        setPadding(max(paddingLeft, 10), 0, max(paddingRight, 10), 0)
        array.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        // 防止有半条边框在view外面的情况
        val halfBorderWidth = borderWidth / 2
        rectF.set(
            halfBorderWidth,
            halfBorderWidth,
            width - halfBorderWidth,
            height - halfBorderWidth
        )
        when (fillMode) {
            FillMode.BORDER -> drawBorder(canvas, rectF)
            FillMode.FILL -> fillBackground(canvas, rectF)
            FillMode.ALL -> {
                // 先画背景再画边框，防止边框被背景遮挡
                fillBackground(canvas, rectF)
                drawBorder(canvas, rectF)
            }
        }
        // 最后再写上文字
        super.onDraw(canvas)
    }

    /**
     * 画边框
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawBorder(canvas: Canvas?, rectF: RectF) {
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        canvas?.drawRoundRect(rectF, radius, radius, paint)
    }

    /**
     * 画背景
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun fillBackground(canvas: Canvas?, rectF: RectF) {
        if (currentTextColor != backgroundColor) {
            paint.color = backgroundColor
            paint.style = Paint.Style.FILL
            canvas?.drawRoundRect(rectF, radius, radius, paint)
        }
    }

    enum class FillMode(val value: Int) {
        // 只有边框，默认
        BORDER(0),
        // 只有背景
        FILL(1),
        // 边框和背景都有
        ALL(2);

        companion object {
            fun getMode(@IntRange(from = 0, to = 2) value: Int): FillMode = when (value) {
                0 -> BORDER
                1 -> FILL
                2 -> ALL
                else -> BORDER
            }
        }
    }
}