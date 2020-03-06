package com.ddancn.view.timeline

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author ddan.zhuang
 * @date 2020/2/29
 *
 */
class CustomDecoration<T> : BaseTimelineDecoration<T>() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var radius = 8f
    lateinit var drawable :Drawable

    override fun drawNode(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
        xPosition: Float,
        item: T,
        itemView: View,
        adapterPosition: Int
    ) {
        paint.color = color(item, adapterPosition)
        // 画图
        if (adapterPosition == 0) {
            val drawable = drawable
            c.drawBitmap(
                drawableToBitmap(drawable, item, adapterPosition),
                xPosition - nodeWidth(item, adapterPosition) / 2,
                (itemView.top + offset).toFloat(),
                Paint()
            )
        } else {
            c.drawCircle(
                xPosition,
                itemView.top + radius + offset.toFloat(),
                radius,
                paint
            )

        }
    }

    private fun drawableToBitmap(drawable: Drawable, item: T, adapterPosition: Int): Bitmap {
        // 取 drawable 的长宽
        val w = nodeWidth(item, adapterPosition)
        val h = nodeHeight(item, adapterPosition)
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
}