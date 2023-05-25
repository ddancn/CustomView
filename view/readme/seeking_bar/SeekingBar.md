# SeekingBar

* 就是SeekBar
* 支持 滑动滑块 或 点击进度条 来改变进度
* 支持显示和吸附刻度（默认不）

## 实现效果
![demo](https://github.com/ddancn/CustomView/blob/master/view/readme/seeking_bar/demo.jpg)

## 使用方式
```xml
    <com.ddancn.view.seekingbar.SeekingBar
        android:id="@+id/seeking_bar"
        android:layout_width="250dp"
        android:layout_height="30dp"
        app:sb_max="100"
        app:sb_tickMark="true"
        app:sb_tickMark_step="10"
        app:sb_tickMark_absorb="true"/>
```
* `max`：最大值
* `sb_tickMark`：是否显示刻度
* `sb_tickMark_step`：一格刻度的值（和max一个单位，不是显示长度
* `sb_tickMark_absorb`：是否吸附刻度

监听进度变化
```kotlin
        seeking_bar.onProgressChange = { progress ->
            tv_seeking_bar.text = format(progress)
        }
```
改变进度
```kotlin
        bt_seeking_bar_minus.setOnClickListener { seeking_bar.progress -= 5 }
        bt_seeking_bar_add.setOnClickListener { seeking_bar.progress += 5 }
```

## 实现原理
主要是重写`onTouchEvent`
* DOWN 时有两种情况
    * 按住了滑块，准备开始滑动，`isDragging = true`
    * 按到了进度条上，直接定位过去
* MOVE 时 `isDragging`则根据`event.x`计算并更新progress
* UP 时 `isDragging = false`。如果需要吸附，则计算左右刻度哪个更近，更新progress