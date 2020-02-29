package com.ddancn.customview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddancn.customview.timeline.*
import com.ddancn.view.timeline.BaseTimelineDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initColorGroup()

        initTimelineList()
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
        records.add(Record(1, "time", "content"))
        records.add(Record(2, "time", "content"))
        records.add(Record(3, "time", "content"))
        val adapter = RecordAdapter()
        adapter.data = records

        val decor1 = DotDecoration()
        decor1.data = records
        rv_timeline1.layoutManager = LinearLayoutManager(this)
        rv_timeline1.adapter = adapter
        rv_timeline1.addItemDecoration(decor1)

        val decor2 = DrawableDecoration()
        decor2.data = records
        decor2.direction = BaseTimelineDecoration.Direction.RIGHT
        rv_timeline2.layoutManager = LinearLayoutManager(this)
        rv_timeline2.adapter = adapter
        rv_timeline2.addItemDecoration(decor2)

        val decor3 = CustomDecoration()
        decor3.data = records
        decor3.direction = BaseTimelineDecoration.Direction.RIGHT
        rv_timeline3.layoutManager = LinearLayoutManager(this)
        rv_timeline3.adapter = adapter
        rv_timeline3.addItemDecoration(decor3)
    }
}
