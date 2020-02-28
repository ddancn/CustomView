package com.ddancn.customview.timeline

import androidx.recyclerview.widget.RecyclerView
import com.ddancn.customview.R
import com.ddancn.view.timeline.DotTimelineDecoration

/**
 * @author ddan.zhuang
 * @date 2020/2/27
 *
 */
class DotDecoration : DotTimelineDecoration<Record>() {
    override fun getColor(item: Record, parent: RecyclerView): Int {
        return when (item.status) {
            1 -> parent.resources.getColor(R.color.colorPrimaryDark)
            2 -> parent.resources.getColor(R.color.colorPrimary)
            3 -> parent.resources.getColor(R.color.colorAccent)
            else -> parent.resources.getColor(R.color.colorPrimaryDark)
        }
    }

}