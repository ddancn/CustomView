package com.ddancn.view.timeline

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

/**
 * @author ddan.zhuang
 * @date 2020/2/27
 * 时间线
 */
abstract class BaseTimelineDecoration<T> : RecyclerView.ItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var offset = 15
    var itemMargin = 15
    var paddingLeft = 15
    var paddingRight = 15
    var direction: Direction = Direction.LEFT

    var data: List<T> = ArrayList()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val count = parent.childCount
        for (i in 0 until count) {
            val item = parent.getChildAt(i)
            val xPosition = getLineX(item)
            val adapterPosition = parent.getChildAdapterPosition(item)
            // 如果是第一个item，不画顶部的竖线
            if (adapterPosition != 0) {
                // 设置上一个item颜色
                paint.color = getColor(data[adapterPosition - 1], parent)
                c.drawLine(
                    xPosition,
                    item.top.toFloat(),
                    xPosition,
                    item.top + getNodeHeight() / 2 + offset.toFloat(),
                    paint
                )
            }
            // 设置这一个item颜色
            paint.color = getColor(data[adapterPosition], parent)
            // 如果是最后一个item，不画底部的竖线
            if (adapterPosition != data.size - 1) {
                c.drawLine(
                    xPosition,
                    item.top + getNodeHeight() / 2 + offset.toFloat(),
                    xPosition,
                    item.bottom + itemMargin.toFloat(),
                    paint
                )
            }
            drawNode(c, parent, state)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        // 设置整个decor的宽度
        val width = paddingLeft + getNodeWidth() + paddingRight
        if (direction == Direction.LEFT) {
            outRect.left = width
        } else {
            outRect.right = width
        }
        // 设置bottom间距，最后一个item不设置
        val cur = parent.getChildAdapterPosition(view)
        val count = state.itemCount
        if (cur != count - 1) {
            outRect.bottom = itemMargin
        }
    }

    protected fun getLineX(view: View): Float =
        if (direction == Direction.LEFT) {
            (getNodeWidth() / 2 + paddingLeft).toFloat()
        } else {
            view.right.toFloat() + paddingLeft+getNodeWidth() / 2
        }

    /**
     * 子类设置在数据不同状态下返回什么颜色
     *
     * @param item   列表项
     * @param parent RecyclerView
     * @return 相应颜色
     */
    @ColorInt
    protected abstract fun getColor(item: T, @NonNull parent: RecyclerView): Int

    /**
     * 子类负责绘制结点
     */
    protected abstract fun drawNode(c: Canvas, parent: RecyclerView, state: RecyclerView.State)

    /**
     * 结点宽度
     */
    protected abstract fun getNodeWidth(): Int

    /**
     * 结点高度
     */
    protected abstract fun getNodeHeight(): Int

    enum class Direction {
        LEFT,
        RIGHT
    }

}