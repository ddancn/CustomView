package com.ddancn.customview.timeline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddancn.customview.R
import kotlinx.android.synthetic.main.activity_timeline.*

class TimelineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        val records = ArrayList<Record>()
        records.add(Record(1,"time", "content"))
        records.add(Record(2,"time", "content"))
        records.add(Record(3,"time", "content"))

        val adapter = RecordAdapter()
        adapter.data = records
        val decor1 = DotDecoration()
        decor1.data = records

        rv_timeline1.layoutManager = LinearLayoutManager(this)
        rv_timeline1.adapter = adapter
        rv_timeline1.addItemDecoration(decor1)

        val decor2 = DrawableDecoration()
        decor2.data = records

        rv_timeline2.layoutManager = LinearLayoutManager(this)
        rv_timeline2.adapter = adapter
        rv_timeline2.addItemDecoration(decor2)

    }
}
