package com.ddancn.view.chessboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.Toast
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

/**
 * @author ddan.zhuang
 * @date 2023/6/30
 * @description 合成棋盘
 */
const val cellRow = 9
const val cellCol = 7
const val TAG = "Chessboard"
val colors = listOf(
    Color.RED, Color.BLUE, Color.GREEN, Color.BLACK
)

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
        cellPaint.strokeWidth = 2f

        content[cellRow / 2][cellCol / 2] = Chessman(ChessType.Emitter)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpec = MeasureSpec.makeMeasureSpec(width / cellCol * cellRow, MeasureSpec.EXACTLY)
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
        cellSize = (width - boardWidth) / cellCol

        // 绘制横向线条
        for (i in 0..cellRow) {
            val y = i * cellSize + boardWidth / 2
            canvas?.drawLine(0f, y, width, y, boardPaint)
        }

        // 绘制纵向线条
        for (i in 0..cellCol) {
            val x = i * cellSize + boardWidth / 2
            canvas?.drawLine(x, 0f, x, height, boardPaint)
        }
    }

    /**
     * 画棋子们
     */
    private fun drawChessmen(canvas: Canvas?) {
        content.forEachIndexed { i, row ->
            row.forEachIndexed { j, cell ->
                cell?.let {
                    drawChessman(
                        canvas,
                        cell,
                        ((j + 0.5) * cellSize).toFloat(),
                        ((i + 0.5) * cellSize).toFloat()
                    )
                }
            }
        }
    }

    /**
     * 画单个棋子
     */
    private fun drawChessman(canvas: Canvas?, cell: Chessman, x: Float, y: Float) {
        cellPaint.color = cell.color
        when (cell.type) {
            ChessType.Normal -> {
                cellPaint.style = Paint.Style.FILL
                canvas?.drawCircle(x, y, cell.level * 8f, cellPaint)
            }
            ChessType.Emitter -> {
                cellPaint.style = Paint.Style.STROKE
                canvas?.drawCircle(x, y, cell.level * 8f, cellPaint)
            }
        }
    }

    private var startX = 0f
    private var startY = 0f
    private var movingX = 0f
    private var movingY = 0f
    private var movingCell: Chessman? = null
    private var curColIdx = 0
    private var curRowIdx = 0

    /**
     * 画正在被移动的棋子
     */
    private fun drawMovingChessman(canvas: Canvas?) {
        movingCell?.let { drawChessman(canvas, it, movingX, movingY) }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                val oldColIdx = (event.x / cellSize).toInt()
                val oldRowIdx = (event.y / cellSize).toInt()
                if (content[oldRowIdx][oldColIdx] == null) return false
                movingCell = content[oldRowIdx][oldColIdx]
                content[oldRowIdx][oldColIdx] = null
            }
            MotionEvent.ACTION_MOVE -> {
                movingX = event.x
                movingY = event.y
                // 拖动事件
                if (abs(movingX - startX) > touchSlop || abs(movingY - startY) > touchSlop) {
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                curColIdx = (event.x / cellSize).toInt().coerceIn(0, cellCol - 1)
                curRowIdx = (event.y / cellSize).toInt().coerceIn(0, cellRow - 1)
                val targetCell = content[curRowIdx][curColIdx]
                // 如果目标位置有棋子
                if (targetCell != null) {
                    // 合并两项
                    if (targetCell.type == movingCell?.type && targetCell.color == movingCell?.color) {
                        merge(targetCell, movingCell)
                    }
                    // 转移原值
                    else {
                        transfer(curRowIdx, curColIdx)
                        content[curRowIdx][curColIdx] = movingCell
                    }
                }
                // 目标位置没棋子，直接放置
                else {
                    content[curRowIdx][curColIdx] = movingCell
                }
                movingCell = null
                invalidate()
                // 点击事件
                if (abs(event.x - startX) < touchSlop && abs(event.y - startY) < touchSlop) {
                    performClick()
                }
            }
        }
        return true
    }

    /**
     * 合并两个棋子
     */
    private fun merge(targetCell: Chessman, movingCell: Chessman?) {
        targetCell.level += movingCell?.level ?: 0
    }

    /**
     * 把棋子转移到最近的空位
     */
    private fun transfer(row: Int, col: Int) {
        val pos = getNearestEmptyCell(row, col)
        if (pos == null) {
            Toast.makeText(context, "棋盘已满", Toast.LENGTH_SHORT).show()
            return
        }
        content[pos.first][pos.second] = content[row][col]
        // TODO: anim
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (content[curRowIdx][curColIdx]?.type == ChessType.Emitter) {
            emit(curRowIdx, curColIdx)
        }
        return true
    }

    /**
     * 发射新棋子
     */
    private fun emit(row: Int, col: Int) {
        val emitter = content[row][col]
        val pos = getNearestEmptyCell(row, col)
        if (pos == null) {
            Toast.makeText(context, "棋盘已满", Toast.LENGTH_SHORT).show()
            return
        }
        content[pos.first][pos.second] = Chessman(
            type = if (Random.nextDouble() < 0.1) ChessType.Emitter else ChessType.Normal,
            color = colors.random()
        )
        // TODO: anim
    }

    /**
     * 获取最近的空位坐标
     */
    private fun getNearestEmptyCell(row: Int, col: Int): Pair<Int, Int>? {
        for (step in 0 until max(cellRow, cellCol)) {
            for (i in 0..step) {
                for (j in 0..step) {
                    if (i == 0 && j == 0) continue
                    if (row + i < cellRow && col + j < cellCol && content[row + i][col + j] == null)
                        return Pair(row + i, col + j)
                    if (row + i < cellRow && col - j >= 0 && content[row + i][col - j] == null)
                        return Pair(row + i, col - j)
                    if (row - i >= 0 && col + j < cellCol && content[row - i][col + j] == null)
                        return Pair(row - i, col + j)
                    if (row - i >= 0 && col - j >= 0 && content[row - i][col - j] == null)
                        return Pair(row - i, col - j)
                }
            }
        }
        return null
    }
}