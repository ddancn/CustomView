# TagTextView

 * 样式为常见tag的TextView，基础样式为圆角边框
 * 支持设置边框颜色和宽度、圆角大小、背景颜色、填充模式（边框，背景，全部）
 * 默认边框颜色为文字颜色，背景颜色为边框颜色，模式为边框，自带左右padding

## 实现效果
![demo](https://github.com/ddancn/CustomView/blob/master/view/readme/tag_tv/demo.jpg)

## 使用方式
```xml
    <com.ddancn.view.tag.TagTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!" />
```

### 常见用法
* 透明+边框：无需设置或仅设置`textColor`
* 只填充背景：设置`textColor + backgroundColor或borderColor`
* 边框+背景不同色：设置`textColor + backgroundColor + borderColor`

### 更多属性
* `borderColor`：边框颜色，默认与文字颜色相同
* `backgroundColor`：背景颜色，默认与`borderColor`相同，只有`fillMode`为`fill`或`all`时生效
* `fillMode`：填充模式，枚举值
    * `border`：只画边框，不填充背景
    * `fill`：填充背景，不设置`backgroundColor`时默认用边框颜色
    * `all`：边框+背景
* `borderWidth`：边框宽度，默认为3px
* `borderRadius`：边框圆角，默认为8px

>注：如果设置了`fillMode`为`fill`或`all`，却没指定与文字颜色不同的边框/背景颜色，则fill不生效。
这是为了防止出现`文字颜色==边框颜色==背景颜色`，也就是全部糊成一坨的尴尬场面。

## 如何实现
* 在TextView中onDraw的super调用之前，画上边框和（或）背景
* 注意边框和背景的绘制顺序，不要让背景盖住边框
* 还有如果画边框的时候起点是0，终点是宽高值，则边缘其实有一半宽的线被画到view外面了。所以让四个坐标分别加/减上`halfBorderWidth`

## 致命弱点
**由于用到了drawRoundRect，所以TargetAPI >= 21。** ~~哈哈哈哈，我还是回去用drawable吧。。~~
~~打算自己实现一个~~

drawRoundRect是native实现的，所以没法照抄。本来以为用drawArc或者drawOval什么的计算一下就可以了，没想到它俩也是target21的，哈哈哈，我还是回去用drawable吧

![呆住](https://github.com/ddancn/CustomView/blob/master/view/readme/tag_tv/meme.png)
