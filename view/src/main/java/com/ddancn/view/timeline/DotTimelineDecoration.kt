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
class DotTimelineDecoration<T> : BaseTimelineDecoration<T>() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var radius = 15f
    var strokeWidth = 2f
        set(value) {
            field = value
            paint.strokeWidth = value
        }

    var strokeColor : (item: T, position: Int) -> Int = { _, _ -> Color.GRAY }

    var nodeType = NodeType.FILL_AND_STROKE

    init {
        paint.color = Color.GRAY
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth

        nodeWidth = { _, _ -> radius.toInt() * 2 }
        nodeHeight = { _, _ -> radius.toInt() * 2 }
        maxWidth = radius.toInt() * 2
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
            paint.color = color(item, adapterPosition)
            c.drawCircle(
                xPosition,
                itemView.top + radius + offset.toFloat(),
                radius,
                paint
            )
        }
        // 画圆的边框
        if (nodeType == NodeType.STROKE || nodeType == NodeType.FILL_AND_STROKE) {
            strokePaint.color = strokeColor(item, adapterPosition)
            c.drawCircle(
                xPosition,
                itemView.top + radius + offset.toFloat(),
                radius,
                strokePaint
            )
        }
    }

    enum class NodeType {
        FILL,
        STROKE,
        FILL_AND_STROKE,
    }
}