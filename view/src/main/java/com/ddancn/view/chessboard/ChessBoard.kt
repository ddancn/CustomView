package com.ddancn.view.chessboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @author ddan.zhuang
 * @date 2023/6/30
 * @description
 */
class ChessBoard(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint: Paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        val width = width.toFloat()
        val height = height.toFloat()
        val cellSize = width / 7

        // 绘制背景
        canvas?.drawColor(Color.WHITE)

        // 绘制棋盘网格线
        paint.color = Color.BLACK
        paint.strokeWidth = 2f

        // 绘制横向线条
        for (i in 0 until 8) {
            val y = i * cellSize
            canvas?.drawLine(0f, y, width, y, paint)
        }

        // 绘制纵向线条
        for (i in 0 until 8) {
            val x = i * cellSize
            canvas?.drawLine(x, 0f, x, width, paint)
        }
    }
}