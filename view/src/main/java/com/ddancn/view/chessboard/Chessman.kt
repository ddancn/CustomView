package com.ddancn.view.chessboard

import android.graphics.Color

/**
 * @author ddan.zhuang
 * @date 2023/7/5
 * @description
 */
data class Chessman(var type: ChessType, var level:Int = 1, var color: Int = Color.BLACK)

enum class ChessType {
    Emitter,
    Normal,
    None
}