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
        item: View,
        adapterPosition: Int
    ) {
        paint.color = getColor(data[adapterPosition], parent)
        // 画圆
        if (adapterPosition == 0) {
            val drawable = parent.resources.getDrawable(R.drawable.ic_uncheck)
            c.drawBitmap(
                drawableToBitmap(drawable),
                xPosition - getNodeWidth() / 2,
                (item.top + offset).toFloat(),
                Paint()
            )
        } else {
            c.drawCircle(
                xPosition,
                item.top + 8 + offset.toFloat(),
                8f,
                paint
            )

        }
    }

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

    override fun getNodeWidth(): Int = 30

    override fun getNodeHeight(): Int = 30
}