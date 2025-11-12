package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.eco.musicplayer.audioplayer.music.R

class CanvasGradientTextViewImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Thuộc tính đọc từ XML
    private var labelText: String = ""
    @SuppressLint("UseKtx")
    private var startColor: Int = Color.parseColor("#C172FF")
    @SuppressLint("UseKtx")
    private var endColor: Int = Color.parseColor("#08E6F8")
    private var rectWidth: Float = 50f
    private var rectHeight: Float = 50f
    private var radius: Float = 25f

    private var textSizePx: Float = 40f
    private var textColor: Int = Color.BLACK // Thêm biến textColor
    private var shadowColor: Int = Color.TRANSPARENT
    private var shadowRadius: Float = 0f
    private var shadowDx: Float = 0f
    private var shadowDy: Float = 0f

    init {
        setupAttributes(attrs)
        setupPaints()
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientViewAndImage)

        labelText = typedArray.getString(R.styleable.GradientViewAndImage_text) ?: ""
        textSizePx = typedArray.getDimension(R.styleable.GradientViewAndImage_Sizetext, 40f)
        startColor = typedArray.getColor(R.styleable.GradientViewAndImage_Colorstart, startColor)
        endColor = typedArray.getColor(R.styleable.GradientViewAndImage_Colorend, endColor)
        rectWidth = typedArray.getDimension(R.styleable.GradientViewAndImage_width, 50f)
        rectHeight = typedArray.getDimension(R.styleable.GradientViewAndImage_height, 50f)
        radius = typedArray.getDimension(R.styleable.GradientViewAndImage_radius, rectHeight / 2f)
        shadowColor = typedArray.getColor(R.styleable.GradientViewAndImage_Colorshadow, Color.TRANSPARENT)
        shadowRadius = typedArray.getFloat(R.styleable.GradientViewAndImage_Radiusshadow, 0f)
        shadowDx = typedArray.getFloat(R.styleable.GradientViewAndImage_Dxshadow, 0f)
        shadowDy = typedArray.getFloat(R.styleable.GradientViewAndImage_Dyshadow, 0f)

        // Thêm xử lý textColor
        textColor = typedArray.getColor(R.styleable.GradientViewAndImage_Colortext, Color.BLACK)

        typedArray.recycle()
    }

    private fun setupPaints() {
        // Cấu hình rectPaint với shadow
        rectPaint.apply {
            if (shadowColor != Color.TRANSPARENT && shadowRadius > 0) {
                setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
            }
        }

        // Cấu hình textPaint
        textPaint.apply {
            color = textColor
            textSize = textSizePx
            typeface = Typeface.DEFAULT_BOLD
            // Có thể thêm shadow cho text nếu cần
            if (shadowColor != Color.TRANSPARENT && shadowRadius > 0) {
                setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Tạo gradient
        val gradient = LinearGradient(
            0f, 0f, rectWidth, rectHeight,
            startColor, endColor, Shader.TileMode.CLAMP
        )
        rectPaint.shader = gradient

        // Tính toán vị trí hình chữ nhật
        val left = paddingLeft.toFloat()
        val top = (height - rectHeight) / 2f
        val right = left + rectWidth
        val bottom = top + rectHeight
        val rectF = RectF(left, top, right, bottom)

        // Vẽ hình chữ nhật với gradient
        canvas.drawRoundRect(rectF, radius, radius, rectPaint)

        // Vẽ text
        val textX = right + 20f
        val textY = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(labelText, textX, textY, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val textWidth = textPaint.measureText(labelText)
        val desiredWidth = (rectWidth + textWidth + paddingLeft + paddingRight + 20f).toInt()
        val desiredHeight = (rectHeight + paddingTop + paddingBottom).toInt()

        val finalWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val finalHeight = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(finalWidth, finalHeight)
    }

}