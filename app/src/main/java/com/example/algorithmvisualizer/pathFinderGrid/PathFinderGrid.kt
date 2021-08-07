package com.example.algorithmvisualizer.pathFinderGrid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.algorithmvisualizer.R
import kotlin.math.min

class PathFinderGrid(context: Context?, attrs: AttributeSet?): View(context, attrs) {

    private var cellSize:Int = 0
    private var pathColor: Int = Color.rgb(188,188,182)
    private val pathPaintColor: Paint = Paint()


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val dimension  = min(height,width)
        cellSize = dimension/15
        setMeasuredDimension(dimension,dimension)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawGrid(canvas!!)

    }

    private fun drawGrid(canvas: Canvas) {

        setPaint(pathPaintColor, pathColor)
        for (i in 1..24) {
            for (j in 1..15) {
                val rectF = RectF(
                    (((j - 1) * cellSize) + 5.toFloat()),
                    (((i - 1) * cellSize) + 5.toFloat()),
                    ((j * cellSize) - 5.toFloat()),
                    ((i * cellSize) - 5.toFloat())
                )


                canvas.drawRect(
                    rectF,  // rect
                    pathPaintColor // Paint
                )

            }
        }
    }

    private fun setPaint(paintColor: Paint, color: Int) {
        paintColor.apply {
            style = Paint.Style.FILL
            this.color = color
            isAntiAlias = true
        }
    }

}