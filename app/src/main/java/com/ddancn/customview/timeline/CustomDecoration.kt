package com.ddancn.customview.timeline

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ddancn.customview.R
import com.ddancn.view.timeline.BaseTimelineDecoration

/**
 * @author ddan.zhuang
 * @date 2020/2/29
 *
 */
class CustomDecoration : BaseTimelineDecoration<Record>() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var color = Color.parseColor("#2A9115")

    init {
        paint.color = color
    }

    override fun getColor(item: Record, parent: RecyclerView): Int {
        return color
    }

    override fun drawNode(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
        xPosition: Float,
        item: Record,
        itemView: View,
        adapterPosition: Int
    ) {
        paint.color = getColor(item, parent)
        // 画图
        if (adapterPosition == 0) {
            val drawable = parent.resources.getDrawable(R.drawable.ic_uncheck)
            c.drawBitmap(
                drawableToBitmap(drawable, item, adapterPosition),
                xPosition - getNodeWidth(item, adapterPosition) / 2,
                (itemView.top + offset).toFloat(),
                Paint()
            )
        } else {
            c.drawCircle(
                xPosition,
                itemView.top + 8 + offset.toFloat(),
                8f,
                paint
            )

        }
    }

    private fun drawableToBitmap(drawable: Drawable, item: Record, adapterPosition: Int): Bitmap {
        // 取 drawable 的长宽
        val w = getNodeWidth(item, adapterPosition)
        val h = getNodeHeight(item, adapterPosition)
        // 取 drawable 的颜色格式
        val config =
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        // 建立对应 bitmap
        val bitmap = Bitmap.createBitmap(w, h, config)
        // 建立对应 bitmap 的画布
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        // 把 drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }

    override fun getNodeWidth(item: Record, adapterPosition: Int): Int = if (adapterPosition==0) 30 else 16

    override fun getNodeHeight(item: Record, adapterPosition: Int): Int = if (adapterPosition==0) 30 else 16

    override fun getMaxWidth(): Int = 30
}