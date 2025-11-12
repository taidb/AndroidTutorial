package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.eco.musicplayer.audioplayer.music.R
import kotlin.math.min

class   GradientView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val strokeWidth = 40f
    private var progress = 55f
    private var progressText = "55%"
    private var subText = "Transaction"
    private var textSize = 70f
    private var subTextSize = 32f
    private var textColor = Color.BLACK
    private var subTextColor = Color.GRAY

    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F2F2F2")
        style = Paint.Style.STROKE
        strokeWidth = this@GradientView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@GradientView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        textSize = this@GradientView.textSize
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private val subTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = subTextColor
        textSize = this@GradientView.subTextSize
        textAlign = Paint.Align.CENTER
    }

    init {
        attrs?.let {
            val typedArray: TypedArray = context.obtainStyledAttributes(
                it,
                R.styleable.GradientView,
                defStyle,
                0
            )

            // Đọc giá trị từ XML
            progress = typedArray.getFloat(R.styleable.GradientView_progress, 55f)
            progressText = typedArray.getString(R.styleable.GradientView_progressText) ?: "${progress.toInt()}%"
            subText = typedArray.getString(R.styleable.GradientView_subText) ?: "Transaction"
            textSize = typedArray.getDimension(R.styleable.GradientView_textSize, 70f)
            subTextSize = typedArray.getDimension(R.styleable.GradientView_subTextSize, 32f)
            textColor = typedArray.getColor(R.styleable.GradientView_textColor, Color.BLACK)
            subTextColor = typedArray.getColor(R.styleable.GradientView_subTextColor, Color.GRAY)

            typedArray.recycle()

            // Cập nhật paint với các giá trị mới
            updateTextPaints()
        }
    }

    private fun updateTextPaints() {
        textPaint.apply {
            color = textColor
            textSize = this@GradientView.textSize
        }
        subTextPaint.apply {
            color = subTextColor
            textSize = this@GradientView.subTextSize
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (width <= 0 || height <= 0) return

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - strokeWidth / 2

        // Vẽ background circle
        canvas.drawCircle(centerX, centerY, radius, paintBackground)

        // Gradient shader
        val sweepGradient = SweepGradient(
            centerX, centerY,
            intArrayOf(
                Color.parseColor("#A259FF"),
                Color.parseColor("#F24E1E"),
                Color.parseColor("#00C4CC"),
                Color.parseColor("#A259FF")
            ),
            null
        )

        // Matrix để xoay gradient
        val matrix = Matrix()
        matrix.setRotate(-90f, centerX, centerY)
        sweepGradient.setLocalMatrix(matrix)
        paintProgress.shader = sweepGradient

        // Vẽ progress arc
        val oval = RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        val totalSweepAngle = 360 * (progress / 100f)
        val gapAngle = 20f
        val segmentCount = 4
        val segmentSweep = (totalSweepAngle - gapAngle * segmentCount) / segmentCount

        var startAngle = -90f
        for (i in 0 until segmentCount) {
            canvas.drawArc(oval, startAngle, segmentSweep, false, paintProgress)
            startAngle += segmentSweep + gapAngle
        }


        // Vẽ text progress
        val yPos = centerY + textPaint.descent() + textPaint.ascent()/2
        canvas.drawText(progressText, centerX, yPos, textPaint)

        // Vẽ subtext
        val subYPos = yPos + 70f
        canvas.drawText(subText, centerX, subYPos, subTextPaint)
    }


}