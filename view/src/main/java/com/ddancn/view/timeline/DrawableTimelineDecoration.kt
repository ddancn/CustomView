package com.ddancn.view.timeline

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
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

    override fun drawNode(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val count = parent.childCount
        for (i in 0 until count) {
            val item = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(item)
            drawable = parent.resources.getDrawable(getDrawable(data[adapterPosition], parent))
            c.drawBitmap(
                drawableToBitmap(drawable),
                getLineX(item) - getNodeWidth() / 2,
                (item.top + offset).toFloat(),
                Paint()
            )
        }
    }

    @DrawableRes
    abstract fun getDrawable(item: T, @NonNull parent: RecyclerView): Int

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        // 取 drawable 的长宽
        val w = getNodeWidth()
        val h = getNodeHeight()
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