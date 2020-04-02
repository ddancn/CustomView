# TimelineDecoration

* 继承自`RecyclerView.ItemDecoration`，实现竖直时间线效果
* 支持根据数据状态区分颜色和结点样式（可以是圆点、图片或自定义等）
* 支持更改左右方向，顺便可以设置item之间的间距，还有一大堆可设置的属性

## 实现效果
![demo](demo.png)

## 使用方式
根据具体要的效果选择
1. 继承`BaseTimelineDecoration`，实现`drawNode`方法，设置合适的属性
1. 选择已经实现了该抽象方法的`DotTimelineDecoration / DrawableTimelineDecoration / CustomTimelineDecoration`一种，设置合适的属性 
  
```kotlin
// 省略声明列表、adapter等不是很有关的内容
val decor = DotTimelineDecoration()
decor.data = yourData
decor.color = { item, position -> Color.GRAY }
recyclerView.addItemDecoration(decor)
```

属性概览
* `color`：: (item: T, position: Int) -> Int，轴线颜色。item是当前项数据，position是其在adapter中的位置
* `nodeWidth`和`nodeHeight`：: (item: T, position: Int) -> Int，结点宽高
* `maxWidth: Int`：可能的最大结点宽度
* `offset: Int`：结点离item顶部的偏移量，根据具体的布局来设置就好
* `itemMargin: Int`：item的间距
* `paddingLeft: Int`和`paddingRight: Int`：轴线的左右padding
* `direction: Direction`：轴线在item左边还是右边
* `lineWidth: Int`：轴线宽度

## 实现详解
这可能还要从~~盘古开天~~（不是）从我都在基类里做了什么开始说起。

### BaseTimelineDecoration
1. 在`onDraw`中画了力所能及的东西：众所周知，能在这里画的也只有那条轴线了
    * 注意到本方法是对整个RecyclerView进行绘制的，所以要先循环一下children
    * 这个线吧，它还要以结点为分界，分成两部分来画。有两个原因：
        * 一是第一项不画上半条线，最后一项不画下半条线；
        * 二是以结点为分隔，上半条线是上一个item的颜色，下半条线才是这一个item的颜色。（还好这里刚好能获取整个列表对象
    * 由于从RecyclerView的`childCount和getChildAt`只包括当前在屏幕范围中可见的子项，如果拿它们的position去数据源里配对的话，就会出现错乱的情况。
    所以真正可用的position应该是`getChildAdapterPosition`
1. 由`getItemOffset`方法设置了：
    * 整个decor的宽度，考虑了方向和自带的左右padding
    * 列表item项的间距，最后一项没有（这也是decoration最常见的用途）画轴线的时候应该把间距也考虑进去
1. 实现了`getLineX`这个方法，用来计算轴线的x坐标，画线、画结点都可以用到。这里也考虑了方向
1. `abstract drawNode`：让子类自己绘制每一个结点
1. 定义了一些属性等待设置：
    * `color`：为函数类型。根据数据或位置，设置不同的轴线颜色
    * `getNodeWidth`和`getNodeHeight`：为函数类型。根据数据或位置，设置不同的结点宽高
    * `getMaxWidth`：最大结点的宽度（因为宽度也可能根据状态不一样），是用来计算整个decor的宽度和轴线的x坐标的
1. 其他可设置的属性见概览

### DotTimelineDecoration
1. 结点都是大小相同圆点的时间线（如图中1），定义了一个重要属性就是圆的半径
1. 重写了`drawNode`，绘制圆和圆的边框
1. 设置好了`getNodeWidth`和`getNodeHeight`和`getMaxWidth`，因为宽高由半径就可以确定了
1. 使用时设置`color`属性即可
1. 其他可设置的属性：
    * `strokeWidth`：边框宽度
    * `strokeColor`：边框颜色，函数类型
    * `nodeType`：结点类型，决定是否要边框，或者只要边框。如果只要边框（如图中2），则此时设置边框的颜色为color

### DrawableTimelineDecoration
1. 结点都是drawable的时间线（如图中3），定义了一个重要属性就是`drawable`，也是函数类型
1. 重写了`drawNode`，绘制drawable
1. 此时的`color`就用于轴线的颜色

> 如果还要更复杂可以直接继承base类，drawNode任你实现，比如：
### CustomDecoration
1. 实现第一个结点为图片，剩下结点为圆点的效果（如图中4）
1. 总之重写`drawNode`就行，一顿绘制

## 一点感想
* 没有具体的需求写一个控件简直全凭想象。越写越想扩展，想让它支持各种不同的场景和需求。但是其实越强大就会越复杂，用起来就更不方便了
* 本来没想到用函数类型，是定义抽象方法，使用继承的方式让子返回颜色等属性，类似于模板模式。
刚好最近在看《kotlin in action》，看到第八章幡然醒悟，我\*\*不是\*\*吗？这是拿着kotlin写java呀，函数类型它不香吗？
* 虽然但是，抽象方法可以保证子类要实现，属性什么的就不能保证一定被正确设置了，而且写完感觉设置属性好像有点繁琐呢
* 从这点可以再延伸一下，有些设计模式在语言特性前都没什么存在必要