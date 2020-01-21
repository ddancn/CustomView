package com.ddancn.view.colorgroup

import androidx.annotation.IntRange

/**
 * @author ddan.zhuang
 * @date 2020/1/21
 * 动画效果参数
 */
const val ANIM_DURATION = 200L
const val ANIM_SCALE = 0.9f
class AnimParam {

    var animDuration = ANIM_DURATION
    var animScale = ANIM_SCALE
    var animType = AnimType.NONE

    /**
     * 动画类型
     */
    enum class AnimType {
        // 无，默认
        NONE,
        // 选中项变小，选中其他项时前选中项复原
        SHRINK,
        // 选中项变小后立刻复原
        BOUNCE;

        companion object {

            fun getMode(@IntRange(from = 0, to = 2) value: Int): AnimType = when (value) {
                0 -> NONE
                1 -> SHRINK
                2 -> BOUNCE
                else -> NONE
            }
        }
    }
}