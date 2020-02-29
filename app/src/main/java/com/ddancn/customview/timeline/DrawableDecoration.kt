package com.ddancn.customview.timeline

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.ddancn.customview.R
import com.ddancn.view.timeline.DrawableTimelineDecoration

/**
 * @author ddan.zhuang
 * @date 2020/2/28
 *
 */
class DrawableDecoration : DrawableTimelineDecoration<Record>() {
    override fun getDrawable(item: Record, parent: RecyclerView): Int {
        return when (item.status) {
            1 -> R.drawable.ic_checked
            else -> R.drawable.ic_uncheck
        }
    }

    override fun getColor(item: Record, parent: RecyclerView): Int {
        return when (item.status) {
            1 -> Color.parseColor("#2A9115")
            else -> Color.GRAY
        }
    }

    override fun getNodeWidth(): Int = 30

    override fun getNodeHeight(): Int = 30

}