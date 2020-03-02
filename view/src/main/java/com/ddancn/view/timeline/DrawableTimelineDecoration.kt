package com.ddancn.view.timeline

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView


/**
 * @author ddan.zhuang
 * @date 2020/2/28
 *
 */
abstract class DrawableTimelineDecoration<T> : BaseTimelineDecoration<T>() {

    lateinit var drawable: Drawable

    override fun drawNode(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
        xPosition: Float,
        item: T,
        itemView: View,
        adapterPosition: Int
    ) {
        drawable = parent.resources.getDrawable(getDrawable(item, adapterPosition, parent))
        c.drawBitmap(
            drawableToBitmap(drawable, item, adapterPosition),
            xPosition - getNodeWidth(item, adapterPosition) / 2,
            (itemView.top + offset).toFloat(),
            Paint()
        )

    }

    @DrawableRes
    abstract fun getDrawable(item: T, adapterPosition: Int, @NonNull parent: RecyclerView): Int

    private fun drawableToBitmap(drawable: Drawable, item: T, adapterPosition: Int): Bitmap {
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
}