package com.ddancn.view.timeline

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author ddan.zhuang
 * @date 2020/2/28
 *
 */
abstract class DotTimelineDecoration<T> : BaseTimelineDecoration<T>() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var radius = 15
    var strokeWidth = 2f
    var strokeColor = Color.WHITE

    var nodeType = NodeType.FILL_AND_STROKE

    init {
        paint.color = Color.RED
        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = strokeColor
        strokePaint.strokeWidth = strokeWidth
    }

    override fun drawNode(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
        xPosition: Float,
        item: T,
        itemView: View,
        adapterPosition: Int
    ) {
        // 画圆
        if (nodeType == NodeType.FILL || nodeType == NodeType.FILL_AND_STROKE) {
            paint.color = getColor(item, parent)
            c.drawCircle(
                xPosition,
                itemView.top + radius + offset.toFloat(),
                radius.toFloat(),
                paint
            )
        }
        // 画圆的边框
        if (nodeType == NodeType.STROKE || nodeType == NodeType.FILL_AND_STROKE) {
            strokePaint.color = getColor(item, parent)
            c.drawCircle(
                xPosition,
                itemView.top + radius + offset.toFloat(),
                radius.toFloat(),
                strokePaint
            )
        }
    }

    override fun getNodeWidth(item: T, adapterPosition: Int): Int = radius * 2

    override fun getNodeHeight(item: T, adapterPosition: Int): Int = radius * 2

    override fun getMaxWidth(): Int = radius * 2

    enum class NodeType {
        FILL,
        STROKE,
        FILL_AND_STROKE,
    }
}