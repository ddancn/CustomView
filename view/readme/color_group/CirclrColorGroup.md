# CircleColorGroup

* 一个颜色是圆形的颜色选择器，可以设置圆的大小、边距、列数
* 被选中时自带边框，边框颜色和圆一样。还有个小动画
* 对于浅颜色，会把边框颜色变成灰色，同时给圆一个灰色的描边，防止看不清楚

## 实现效果
![demo](https://github.com/ddancn/CustomView/blob/master/view/readme/color_group/demo.gif)

## 使用方式
```xml
    <com.ddancn.view.colorgroup.CircleColorGroup
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:radius="12dp"
        app:column="5"/>
```
## 常见用法
* `radius`：设置圆的大小
* `column`：设置分几列排

## 更多属性
* `itemMargin`或`itemMarginHorizontal`或`itemMarginVertical`设置每个圆的边距。注意当圆在边缘行/列时，竖直/水平边距不会被设置
* strokeWidth：设置被选中时边框的宽度，TODO
## 如何实现
* GridLayout风格的RecyclerView
* group的重点在选中时的变化和监听
* 当圆在边缘行/列时，最好不要设置相应方向的边距，比如在第一行的圆不被设置上边距。这是为了防止它和父结点的边距重复设置，导致可能的布局偏差
* 画圆时，设置measure宽高比实际radius大一圈（一圈可以与边框宽度有关，TODO），给画选中时的边框留出位置
* 如何判断颜色深浅：将RGB转化为YUV，值越小则颜色越深
    ``` 
    r * 0.299 + g * 0.587 + b * 0.114 >= 192
    ```
