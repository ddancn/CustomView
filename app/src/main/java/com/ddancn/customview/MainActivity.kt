package com.ddancn.customview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorChoseListener = { color: Int, position: Int ->
            Toast.makeText(this, "选择了第${position+1}个颜色${color.toColor()}", Toast.LENGTH_SHORT).show()
        }
        color_group_bounce.setChosenListener(colorChoseListener)
        color_group_shrink.setChosenListener(colorChoseListener)
    }
}
