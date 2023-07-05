package com.ddancn.view.chessboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @author ddan.zhuang
 * @date 2023/6/30
 * @description
 */
const val cellRow = 9
const val cellCol = 7
const val TAG = "Chessboard"

class Chessboard(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val boardPaint: Paint = Paint()
    private val cellPaint: Paint = Paint()
    private val boardWidth = 2f
    private var cellSize: Float = 0.0f

    private val content =
        MutableList(cellRow) { MutableList<Chessman?>(cellCol) { null } }

    init {
        boardPaint.color = Color.BLACK
        boardPaint.strokeWidth = boardWidth

        cellPaint.textSize = 32f
        content[4][3] = Chessman(ChessType.Normal)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpec = MeasureSpec.makeMeasureSpec(width / 7 * 9, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        drawBg(canvas)
        drawChessmen(canvas)
        drawMovingChessman(canvas)
    }

    /**
     * 画网格线
     */
    private fun drawBg(canvas: Canvas?) {
        val width = width.toFloat()
        val height = height.toFloat()
        cellSize = width / cellCol

        // 绘制横向线条
        for (i in 0..cellRow) {
            val y = i * cellSize
            canvas?.drawLine(0f, y, width, y, boardPaint)
        }

        // 绘制纵向线条
        for (i in 0..cellCol) {
            val x = i * cellSize
            canvas?.drawLine(x, 0f, x, height, boardPaint)
        }
    }

    /**
     * 画棋子们
     */
    private fun drawChessmen(canvas: Canvas?) {
        content.forEachIndexed { i, row ->
            row.forEachIndexed { j, cell ->
                drawChessman(
                    canvas,
                    cell,
                    ((j + 0.5) * cellSize).toFloat(),
                    ((i + 0.5) * cellSize).toFloat()
                )
            }
        }
    }

    /**
     * 画单个棋子
     */
    private fun drawChessman(canvas: Canvas?, cell: Chessman?, x: Float, y: Float) {
        when (cell?.type) {
            ChessType.Normal -> {
                canvas?.drawCircle(x, y, cell.level * 5f, cellPaint)
            }
            ChessType.Emitter -> {
                canvas?.drawText("+", x, y, cellPaint)
            }
            else -> {}
        }
    }

    private var movingX = 0f
    private var movingY = 0f
    private var movingCell: Chessman? = null

    /**
     * 画正在被移动的棋子
     */
    private fun drawMovingChessman(canvas: Canvas?) {
        if (movingX != 0f && movingY != 0f) {
            drawChessman(canvas, movingCell, movingX, movingY)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val oldYIdx = (event.x / cellSize).toInt()
                val oldXIdx = (event.y / cellSize).toInt()
                if (content[oldXIdx][oldYIdx] == null) return false
                movingCell = content[oldXIdx][oldYIdx]
                content[oldXIdx][oldYIdx] = null
            }
            MotionEvent.ACTION_MOVE -> {
                movingX = event.x
                movingY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                movingX = 0f
                movingY = 0f
                val newYIdx = (event.x / cellSize).toInt().coerceIn(0, cellCol - 1)
                val newXIdx = (event.y / cellSize).toInt().coerceIn(0, cellRow - 1)
                content[newXIdx][newYIdx] = movingCell
                invalidate()

            }
        }
        return true
    }

}