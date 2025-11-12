package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityViewProductBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ViewProductActivity : BottomSheetDialogFragment() {
    private lateinit var binding: ActivityViewProductBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        binding = ActivityViewProductBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        return dialog
    }


    override fun onStart() {
        super.onStart()
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.let { dialog ->
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { view ->
                val behavior = BottomSheetBehavior.from(view)
                setUpFixelBottomSheetBehavior(behavior)
            }
            dialog.window?.setDimAmount(0.7f)
        }
    }

    private fun setUpFixelBottomSheetBehavior(behavior: BottomSheetBehavior<*>) {
        behavior.apply {
            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels
            val peekHeight = (screenHeight * 0.7).toInt()

            this.peekHeight = peekHeight
            this.skipCollapsed=false
            this.isFitToContents=true
            this.isHideable=true
            this.isDraggable=true
            state =BottomSheetBehavior.STATE_COLLAPSED
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })


        }

    }

}