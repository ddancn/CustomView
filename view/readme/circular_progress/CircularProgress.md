# CircularProgress

* 一个圆形的进度条哒
* 支持设置条的颜色和宽度。不用设置半径，这可以由宽高决定
* 文字部分留给外部实现，有着更高的可扩展性。
给出了动画的`ObjectAnimator`对象，对其设置监听，可以在进度条动画的同时改变文字

## 实现效果
![demo](https://github.com/ddancn/CustomView/blob/master/view/readme/circular_progress/demo.jpg)

## 使用方式
```xml
    <com.ddancn.view.circularprogress.CircularProgress
        android:layout_width="100dp"
        android:layout_height="100dp"
        app:cg_strokeWidth="10dp"
        app:cg_color="#008577"/>
```

用`start()`方法开始动画，传入进度和动画时长（可选）

`start()`方法返回了`ObjectAnimator`对象，进行监听，改变文字：
```kotlin
    circular_progress.start(progress).addUpdateListener { animation ->
        val progressNow = animation.getAnimatedValue("progress") as Float
        tv_progress.text = String.format("%.1f%%", progressNow)
    }
```

## 实现原理
`canvas?.drawArc + ObjectAnimator.ofFloat` 呀