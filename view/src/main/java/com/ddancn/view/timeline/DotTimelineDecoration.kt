package com.ddancn.view.timeline

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
    var color = Color.RED
    var strokeColor = Color.WHITE

    var nodeType = NodeType.FILL_AND_STROKE

    init {
        paint.color = color
        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = strokeColor
        strokePaint.strokeWidth = strokeWidth
    }

    override fun drawNode(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val count = parent.childCount
        val xPosition = getLineX(parent)
        for (i in 0 until count) {
            val item = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(item)
            paint.color = getColor(data[adapterPosition], parent)
            // 画圆
            if (nodeType == NodeType.FILL || nodeType == NodeType.FILL_AND_STROKE) {
                c.drawCircle(
                    xPosition,
                    item.top + radius + offset.toFloat(),
                    radius.toFloat(),
                    paint
                )
            }
            // 画圆的边框
            if (nodeType == NodeType.STROKE || nodeType == NodeType.FILL_AND_STROKE) {
                c.drawCircle(
                    xPosition,
                    item.top + radius + offset.toFloat(),
                    radius.toFloat(),
                    strokePaint
                )
            }
        }
    }

    override fun getNodeWidth(): Int = radius * 2

    override fun getNodeHeight(): Int = radius * 2

    enum class NodeType {
        FILL,
        STROKE,
        FILL_AND_STROKE,
    }
}