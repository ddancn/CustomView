# CircleColorGroup

* 一个颜色是圆形的颜色选择器。被选中时的圆自带边框，边框颜色（一般）和圆一样
* 对于Group，可以设置圆之间的边距、列数；对于每个Color，可以设置大小、边框宽度、边框距离、选中时的动画（类型、时长、缩放比例）
* 对于浅颜色，会把边框颜色变成灰色，同时给圆一个灰色的描边，防止看不清楚

## 实现效果
![bounce](https://github.com/ddancn/CustomView/blob/master/view/readme/color_group/bounce.gif)
![shrink](https://github.com/ddancn/CustomView/blob/master/view/readme/color_group/shrink.gif)

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
* `itemMargin`或`itemMarginHorizontal`或`itemMarginVertical`：设置每个圆的边距。注意当圆在边缘行/列时，竖直/水平且方向朝外的边距不会被设置
* `strokeWidth`：设置被选中时边框的宽度
* `strokeGap`：设置被选中时边框和圆之间的距离
* `animType`：设置动画，枚举值
    * `none`：无动画，默认
    * `shrink`：选中项变小，选中其他项时前选中项复原（但是由于边框的存在，可能看上去还是比其他项大/差不多大）（由于画边框和动画几乎同时进行，还可能有先变大再变小的错觉）
    * `bounce`：选中项变小后立刻复原
* `animDuration`：动画时长，默认200L。type是bounce的话该值为变小+变大的总时长，而不是分别的时长
* `animScale`：动画缩放比，默认0.9f。最好在0到1之间，超过1就会画到view的范围外面了

## 如何实现
* RecyclerView + GridLayoutManager
* CircleColorGroup的重点在选中时的变化和监听。选中新项时，如果要处理先前选中项，可以不用遍历整个列表，只要用一个变量保存之前的选中项就可以了
* 当圆在边缘行/列时，不设置相应朝外方向的边距，比如在第一行的圆不被设置上边距。这是为了防止它和父结点的边距重复设置，导致可能的布局偏差
* 画圆时，设置measure宽高比实际radius大一圈（一圈与边框宽度、边框距离有关），给画选中时的边框留出位置
* 如何判断颜色深浅：将RGB转化为YUV，值越小则颜色越深
    ``` 
    r * 0.299 + g * 0.587 + b * 0.114 >= 192
    ```
* 动画效果bounce只是对当前项作用，故在CircleColor中处理即可。shrink还要把先前选中项复原，所以在CircleColorGroup中处理（画边框也是类似的道理）
