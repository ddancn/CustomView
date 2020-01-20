package com.ddancn.view.colorgroup

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
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
const val ANIM_DURATION = 200L
const val ANIM_SCALE = 0.9f

class CircleColorGroup(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    var onChosenListener: ((color: Int) -> Unit)? = null
    private var colorList = DEFAULT_COLORS.toMutableList()
    private val recyclerView = RecyclerView(context)
    private val adapter = ColorAdapter(colorList)

    private val column: Int
    private val radius: Float
    private val itemMarginHorizontal: Float
    private val itemMarginVertical: Float

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CircleColorGroup)
        // 列数，默认为5
        column = array.getColor(R.styleable.CircleColorGroup_column, 5)
        // 半径，默认25px
        radius = array.getDimension(R.styleable.CircleColorGroup_radius, 25f)
        // 上下左右margin，当item在边缘行/列时无效
        val margin = array.getDimension(R.styleable.CircleColorGroup_itemMargin, 5f)
        itemMarginHorizontal =
            array.getDimension(R.styleable.CircleColorGroup_itemMarginHorizontal, margin)
        itemMarginVertical =
            array.getDimension(R.styleable.CircleColorGroup_itemMarginVertical, margin)
        array.recycle()

        // 列表和适配器
        recyclerView.layoutManager = GridLayoutManager(context, column)
        recyclerView.adapter = adapter
        adapter.onChosenListener = { color, position ->
            onChosenListener?.invoke(color)
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
     */
    fun setChosen(position: Int) {
        if (position in 0..colorList.lastIndex) {
            recyclerView.children.forEachIndexed { index, view ->
                val cur = index == position
                (view as CircleColor).setChosen(cur)
                // 动画
                view.animate().scaleX(if (cur) ANIM_SCALE else 1f)
                    .scaleY(if (cur) ANIM_SCALE else 1f).duration = ANIM_DURATION

            }
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
            val lp = GridLayoutManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            lp.leftMargin = if (isLeft(position)) 0 else itemMarginHorizontal.toInt()
            lp.topMargin = if (isTop(position)) 0 else itemMarginVertical.toInt()
            lp.rightMargin = if (isRight(position)) 0 else itemMarginHorizontal.toInt()
            lp.bottomMargin = if (isBottom(position)) 0 else itemMarginVertical.toInt()
            holder.itemView.layoutParams = lp
            // 设置其他
            val color = colorList[position]
            holder.itemView as CircleColor
            holder.itemView.setColor(color)
            holder.itemView.radius = radius
            holder.itemView.setChosen(position == 0)
            holder.itemView.setOnClickListener {
                onChosenListener?.invoke(color, position)
            }
        }

    }

    fun isTop(position: Int): Boolean = position < column

    fun isBottom(position: Int): Boolean = position >= adapter.itemCount - column

    fun isLeft(position: Int): Boolean = position % column == 0

    fun isRight(position: Int): Boolean = (position + 1) % column == 0

    class ViewHolder(itemView: CircleColor) : RecyclerView.ViewHolder(itemView)
}