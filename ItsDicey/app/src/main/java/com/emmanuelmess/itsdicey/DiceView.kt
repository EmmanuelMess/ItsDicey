package com.emmanuelmess.itsdicey

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.*
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import kotlin.random.Random

class DiceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var onResultObtained: (() -> Unit)? = null
    var onUserRestarted: (() -> Unit)? = null

    private val TIME_CLOCK_MILLIS = 1500L
    private var RADIUS: Pixels = Pixels(75f)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        RADIUS = 75.dp.toPx(context)
    }

    private val centers = SparseArray<PointF>()

    private /*lateinit*/ var timeStart: Long? = null
    private var paintCounter = false
    private var hasJustFinishedChoosing = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked) {
            ACTION_UP, ACTION_POINTER_UP -> {
                if(hasJustFinishedChoosing) return true

                centers.setValueAt(event.getPointerId(event.actionIndex), null)

                restartCounter()

                if(event.pointerCount > 2) {
                    startCounter()
                }
            }
            ACTION_DOWN, ACTION_POINTER_DOWN -> {
                if(hasJustFinishedChoosing) {
                    onUserRestarted?.invoke()
                    centers.clear()
                    hasJustFinishedChoosing = false
                }

                val point = PointF(event.getX(event.actionIndex), event.getY(event.actionIndex))
                centers.put(event.getPointerId(event.actionIndex), point)

                restartCounter()

                if(event.pointerCount > 1) {
                    startCounter()
                }
            }
        }

        invalidate()

        return true
    }

    private val counterRedPaint by lazy {
        val p = Paint()
        p.color = RED
        return@lazy p
    }
    private val counterRect by lazy {
        val sideLength = (150.dp.toPx(context)).value

        RectF(0.0F, height - sideLength, sideLength, height.toFloat())
    }

    private val paints = listOf(paint(RED), paint(BLUE), paint(GREEN), paint(YELLOW))

    override fun onDraw(canvas: Canvas) {
        canvas.drawARGB(255, 0, 0, 0)

        if(paintCounter) {
            val percentage = (System.currentTimeMillis() - timeStart!!).toDouble() / TIME_CLOCK_MILLIS.toDouble()
            canvas.drawArc(counterRect, 0f, percentage.toFloat() * 360, true, counterRedPaint)
        }

        centers.forEachIndexed { i, center ->
            canvas.drawCircle(center.x, center.y, RADIUS.value, paints[i % paints.size])
        }

        if(paintCounter) {
            invalidate()
        }
    }

    private var counterDelayedRunnable: DelayedRunnable? = null

    private fun startCounter() {
        paintCounter = true
        timeStart = System.currentTimeMillis()

        counterDelayedRunnable = DelayedRunnable(TIME_CLOCK_MILLIS) {
            var position: Int

            do {
                position = Random.nextInt(centers.size())
            } while (centers.valueAt(position) == null)

            centers.forEachIndexed { index, _ ->
                if(index != position) {
                    centers.setValueAt(index, null)
                }
            }

            paintCounter = false
            hasJustFinishedChoosing = true

            onResultObtained?.invoke()
        }
    }

    private fun restartCounter() {
        paintCounter = false
        counterDelayedRunnable?.cancel(false)
    }

    private fun paint(color: Int): Paint {
        val p = Paint()
        p.color = color
        return p
    }

}