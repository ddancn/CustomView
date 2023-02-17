package com.ddancn.view.colorgroup

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddancn.view.R

/**
 * @author ddan.zhuang
 * @date 2020/1/17
 * 一个颜色选择器，包含CircleColor列表
 */
val DEFAULT_COLORS = listOf(
    Color.WHITE,
    Color.LTGRAY,
    Color.GRAY,
    Color.DKGRAY,
    Color.BLACK
)

class CircleColorGroup(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var colorList = DEFAULT_COLORS.toMutableList()
    private val recyclerView = RecyclerView(context)
    private val adapter = ColorAdapter(colorList)

    private val column: Int
    private val radius: Float
    private val strokeWidth: Float
    private val strokeGap: Float
    private val anim = AnimParam()
    private val itemMarginHorizontal: Float
    private val itemMarginVertical: Float

    /**
     * 上一个选中的位置
     */
    private var prePosition = 0

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CircleColorGroup)
        // 列数，默认为5
        column = array.getColor(R.styleable.CircleColorGroup_ccg_column, 5)
        // 半径，默认25px
        radius = array.getDimension(R.styleable.CircleColorGroup_ccg_radius, 25f)
        // 边框宽度，默认3px
        strokeWidth = array.getDimension(R.styleable.CircleColorGroup_ccg_strokeWidth, 3f)
        // 与边框之间的距离，默认等于边框宽度
        strokeGap = array.getDimension(R.styleable.CircleColorGroup_ccg_strokeGap, strokeWidth)
        // 上下左右margin，当item在边缘行/列时无效
        val margin = array.getDimension(R.styleable.CircleColorGroup_ccg_itemMargin, 5f)
        itemMarginHorizontal =
            array.getDimension(R.styleable.CircleColorGroup_ccg_itemMarginHorizontal, margin)
        itemMarginVertical =
            array.getDimension(R.styleable.CircleColorGroup_ccg_itemMarginVertical, margin)
        // 动画参数
        anim.animDuration =
            array.getInt(R.styleable.CircleColorGroup_ccg_animDuration, ANIM_DURATION.toInt()).toLong()
        anim.animScale = array.getFloat(R.styleable.CircleColorGroup_ccg_animScale, ANIM_SCALE)
        anim.animType =
            AnimParam.AnimType.getMode(array.getInt(R.styleable.CircleColorGroup_ccg_animType, 0))
        array.recycle()

        // 列表和适配器
        recyclerView.layoutManager = GridLayoutManager(context, column)
        recyclerView.adapter = adapter
        adapter.onChosenListener = { color, position ->
            setChosen(position)
        }
        addView(recyclerView)
    }

    /**
     * 设置颜色列表
     */
    fun setColorList(list: List<Int>) {
        colorList.clear()
        colorList.addAll(list)
        adapter.notifyDataSetChanged()
    }

    /**
     * 设置选中项
     * @param position 被选中的项
     */
    fun setChosen(position: Int) {
        if (position in 0..colorList.lastIndex) {
            setChosen(prePosition, false)
            setChosen(position, true)
            prePosition = position
        }
    }

    /**
     * 改变某项状态
     * @param position 要改变的项
     * @param chosen 改为选中/非选中状态
     */
    fun setChosen(position: Int, chosen: Boolean) {
        val item = recyclerView.getChildAt(position) as CircleColor
        item.setChosen(chosen)
        // 缩放动画
        if (anim.animType == AnimParam.AnimType.SHRINK) {
            val scale = if (chosen) anim.animScale else 1f
            item.animate().scaleX(scale).scaleY(scale).duration = anim.animDuration
        }
    }

    fun setChosenListener(listener: ((color: Int, position: Int) -> Unit)) {
        adapter.onChosenListener = { color, position ->
            listener.invoke(color, position)
            setChosen(position)
        }
    }

    /**
     * 颜色列表适配器
     */
    inner class ColorAdapter(private val colorList: MutableList<Int>) :
        RecyclerView.Adapter<ViewHolder>() {

        var onChosenListener: ((color: Int, position: Int) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = CircleColor(parent.context)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = colorList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // 设置边距
            val lp = MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            lp.leftMargin = if (isLeft(position)) 0 else itemMarginHorizontal.toInt()
            lp.topMargin = if (isTop(position)) 0 else itemMarginVertical.toInt()
            lp.rightMargin = if (isRight(position)) 0 else itemMarginHorizontal.toInt()
            lp.bottomMargin = if (isBottom(position)) 0 else itemMarginVertical.toInt()
            holder.itemView.layoutParams = lp
            // 设置其他属性
            val color = colorList[position]
            (holder.itemView as CircleColor).apply {
                this.setColor(color)
                this.radius = this@CircleColorGroup.radius
                this.strokeWidth = this@CircleColorGroup.strokeWidth
                this.strokeGap = this@CircleColorGroup.strokeGap
                this.anim = this@CircleColorGroup.anim
                // 默认选中第一个
                this.setChosen(position == 0)
                this.setOnClickListener {
                    onChosenListener?.invoke(color, position)
                }
            }

        }

    }

    fun isTop(position: Int): Boolean = position < column

    fun isBottom(position: Int): Boolean = position >= adapter.itemCount - column

    fun isLeft(position: Int): Boolean = position % column == 0

    fun isRight(position: Int): Boolean = (position + 1) % column == 0

    class ViewHolder(itemView: CircleColor) : RecyclerView.ViewHolder(itemView)
}