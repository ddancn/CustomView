package com.ddancn.customview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddancn.customview.timeline.*
import com.ddancn.view.timeline.BaseTimelineDecoration
import com.ddancn.view.timeline.DotTimelineDecoration
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

    val records = ArrayList<Record>()
    val adapter = RecordAdapter()

    private fun initTimelineList() {
        records.add(Record(1, "time", "content"))
        records.add(Record(2, "time", "content"))
        records.add(Record(3, "time", "content"))
        adapter.data = records

        val decor1 = DotDecoration()
        setList(rv_timeline1, decor1)

        val decor2 = DotDecoration()
        decor2.nodeType = DotTimelineDecoration.NodeType.STROKE
        setList(rv_timeline2, decor2)

        val decor3 = DrawableDecoration()
        decor3.direction = BaseTimelineDecoration.Direction.RIGHT
        setList(rv_timeline3, decor3)

        val decor4 = CustomDecoration()
        decor4.direction = BaseTimelineDecoration.Direction.RIGHT
        setList(rv_timeline4, decor4)

    }

    private fun setList(recyclerView: RecyclerView, decoration: BaseTimelineDecoration<Record>){
        decoration.data = records
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(decoration)
    }
}
