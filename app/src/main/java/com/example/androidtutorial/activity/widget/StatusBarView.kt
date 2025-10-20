package com.example.androidtutorial.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat


class StatusBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        // Lắng nghe và lấy chiều cao status bar để set vào layout
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.layoutParams?.height = statusBarHeight
            v.requestLayout()
            insets
        }
    }
}
