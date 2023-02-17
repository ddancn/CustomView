package com.ddancn.customview

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddancn.customview.timeline.Record
import com.ddancn.customview.timeline.RecordAdapter
import com.ddancn.view.timeline.BaseTimelineDecoration
import com.ddancn.view.timeline.CustomDecoration
import com.ddancn.view.timeline.DotTimelineDecoration
import com.ddancn.view.timeline.DrawableTimelineDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initColorGroup()

        initTimelineList()

        initCircularProgress()
    }

    private fun initColorGroup() {
        val colorChoseListener = { color: Int, position: Int ->
            Toast.makeText(this, "选择了第${position + 1}个颜色${color.toColor()}", Toast.LENGTH_SHORT)
                .show()
        }
        color_group_bounce.setChosenListener(colorChoseListener)
        color_group_shrink.setChosenListener(colorChoseListener)
    }

    private fun initTimelineList() {
        val records = ArrayList<Record>()
        val adapter = RecordAdapter()
        records.add(Record(1, "time", "content"))
        records.add(Record(2, "time", "content"))
        records.add(Record(3, "time", "content"))
        adapter.data = records

        // 设置列表
        fun setList(recyclerView: RecyclerView, decoration: BaseTimelineDecoration<Record>) {
            decoration.data = records
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(decoration)
        }

        // 返回颜色
        fun setColor(item: Record): Int {
            return when (item.status) {
                1 -> resources.getColor(R.color.colorPrimaryDark)
                2 -> resources.getColor(R.color.colorPrimary)
                3 -> resources.getColor(R.color.colorAccent)
                else -> resources.getColor(R.color.colorPrimaryDark)
            }
        }
        // 第一种，实心圆形，仅设置颜色
        val decor1 = DotTimelineDecoration<Record>()
        decor1.color = { item, _ -> setColor(item) }
        setList(rv_timeline1, decor1)

        // 第二种，空心圆形，设置颜色、结点类型、轴线宽度
        val decor2 = DotTimelineDecoration<Record>()
        decor2.nodeType = DotTimelineDecoration.NodeType.STROKE
        decor2.color = { item, _ -> setColor(item) }
        setList(rv_timeline2, decor2)

        // 第三种，drawable，设置drawable资源、居右、轴线颜色、结点大小
        val decor3 = DrawableTimelineDecoration<Record>(this)
        decor3.direction = BaseTimelineDecoration.Direction.RIGHT
        decor3.drawableRes = { item, _ ->
            when (item.status) {
                1 -> R.drawable.ic_checked
                else -> R.drawable.ic_uncheck
            }
        }
        decor3.color = { item, _ ->
            when (item.status) {
                1 -> Color.parseColor("#2A9115")
                else -> Color.GRAY
            }
        }
        decor3.nodeWidth = { _, _ -> 30 }
        decor3.nodeHeight = { _, _ -> 30 }
        decor3.maxWidth = 30
        setList(rv_timeline3, decor3)

        // 第四种，drawable+小圆点，设置drawable资源、居右、轴线结点颜色、结点大小
        val decor4 = CustomDecoration<Record>(this)
        decor4.drawableRes = R.drawable.ic_uncheck
        decor4.color = { _, _ -> resources.getColor(R.color.colorPrimary) }
        decor4.direction = BaseTimelineDecoration.Direction.RIGHT
        decor4.radius = 8f
        decor4.nodeWidth = { _, adapterPosition -> if (adapterPosition == 0) 30 else 16 }
        decor4.nodeHeight = { _, adapterPosition -> if (adapterPosition == 0) 30 else 16 }
        decor4.maxWidth = 30
        setList(rv_timeline4, decor4)
    }


    private fun initCircularProgress() {
        fun startProgress(progress: Float) {
            circular_progress.start(progress).addUpdateListener { animation ->
                val progressNow = animation.getAnimatedValue("progress") as Float
                tv_progress.text = String.format("%.1f%%", progressNow)
            }
        }

        startProgress(65f)
        bt_progress.setOnClickListener { startProgress(Random.nextFloat() * 100) }
    }
}
