package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.util.AttributeSet
import com.eco.musicplayer.audioplayer.music.R
import android.content.Context
import android.graphics.*
import androidx.appcompat.widget.AppCompatTextView

class GradientTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var angle: Float = 0f
    private var shadowColor: Int = Color.BLACK
    private var shadowRadius: Float = 0f
    private var shadowDx: Float = 0f
    private var shadowDy: Float = 0f
    private var startColor: Int = Color.parseColor("#F3F3FC")
    private var endColor: Int = Color.parseColor("#A2B1DA")

    init {
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView)

        angle = typedArray.getFloat(R.styleable.GradientTextView_angle, 0f)
        shadowColor = typedArray.getColor(R.styleable.GradientTextView_shadowColor, Color.BLACK)
        shadowRadius = typedArray.getFloat(R.styleable.GradientTextView_shadowRadius, 0f)
        shadowDx = typedArray.getFloat(R.styleable.GradientTextView_shadowDx, 0f)
        shadowDy = typedArray.getFloat(R.styleable.GradientTextView_shadowDy, 0f)
        startColor = typedArray.getColor(R.styleable.GradientTextView_startColor, Color.parseColor("#F3F3FC"))
        endColor = typedArray.getColor(R.styleable.GradientTextView_endColor, Color.parseColor("#A2B1DA"))

        typedArray.recycle()

        // Áp dụng đổ bóng
        if (shadowRadius > 0) {
            setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
        }
    }


    override fun onDraw(canvas: Canvas) {
        // Tính toán điểm gradient dựa trên góc
        val points = calculateGradientPoints(angle)

        // Tạo gradient với 2 màu
        val gradient = LinearGradient(
            points[0], points[1], points[2], points[3],
            startColor, endColor,
            Shader.TileMode.CLAMP
        )

        // Áp dụng gradient cho Paint của Text
        paint.shader = gradient

        // Áp dụng đổ bóng nếu có
        if (shadowRadius > 0) {
            setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
        }

        super.onDraw(canvas)
    }

    private fun calculateGradientPoints(angle: Float): FloatArray {
        val rad = Math.toRadians(angle.toDouble())

        val dx = Math.cos(rad).toFloat()
        val dy = Math.sin(rad).toFloat()

        val length = Math.sqrt((width * width + height * height).toDouble()).toFloat()

        val startX = width / 2 - dx * length / 2
        val startY = height / 2 - dy * length / 2
        val endX = width / 2 + dx * length / 2
        val endY = height / 2 + dy * length / 2

        return floatArrayOf(startX, startY, endX, endY)
    }
}