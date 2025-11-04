import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.databinding.ActivityPaywallBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaywallBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: ActivityPaywallBottomSheetDialogBinding
    private var selectedPlan = 1
    private var dismissListener: (() -> Unit)? = null

    fun setOnDismissListener(listener: () -> Unit) {
        dismissListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        binding = ActivityPaywallBottomSheetDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        setupInitialState()
        setupClickListeners()

        return dialog
    }


    override fun onStart() {
        super.onStart()

        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { view ->
                val behavior = BottomSheetBehavior.from(view)

                setupFixedBottomSheetBehavior(behavior)
            }

            dialog.window?.setDimAmount(0.7f)
        }
    }

    private fun setupFixedBottomSheetBehavior(behavior: BottomSheetBehavior<*>) {
        behavior.apply {
            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels
            val peekHeight = (screenHeight * 0.7).toInt()

            this.peekHeight = peekHeight
            this.skipCollapsed = false
            this.isFitToContents = true
            this.isHideable = true
                this.isDraggable = true

            state = BottomSheetBehavior.STATE_COLLAPSED
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })

        }
    }


    private fun setupInitialState() {
        showTryForFreeLayout()
    }

    private fun setupClickListeners() {
        binding.icClose.setOnClickListener { dismiss() }
        binding.btnIap1.setOnClickListener { selectPlan(1) }
        binding.btnIap2.setOnClickListener { selectPlan(2) }
        binding.btnTryForFree.setOnClickListener { handleTryForFreeClick() }
    }

    private fun selectPlan(plan: Int) {
        selectedPlan = plan
        when (plan) {
            1 -> applyPlan1UI()
            2 -> applyPlan2UI()
        }
    }

    private fun applyPlan1UI() {
        binding.apply {
            btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_selected)
            btnIap2.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
            tvMostPopular.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.color_8147FF)
            txtAutoRenew.text =(getString(R.string.after_free_trial_ends_yearly_max))
        }
    }

    private fun applyPlan2UI() {
        binding.apply {
            btnIap2.setBackgroundResource(R.drawable.bg_btn_no_pw_4_unselected)
            btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
            tvMostPopular.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.color_908DAC)
            txtAutoRenew.text =(getString(R.string.after_free_trial_ends_weekly))
        }
    }

    private fun handleTryForFreeClick() {
        if (selectedPlan == 1) {
            showMainProgress(true)
        }
        handleButtonLoading(binding.btnTryForFree, binding.progress2, selectedPlan)
    }

    private fun handleButtonLoading(
        button: AppCompatTextView,
        progress: View,
        plan: Int
    ) {
        prepareButtonForLoading(button, progress)
        Handler(Looper.getMainLooper()).postDelayed({
            resetButtonAfterLoading(button, progress, plan)
        }, 2000)
    }

    private fun prepareButtonForLoading(button: AppCompatTextView, progress: View) {
        button.text = ""
        button.isEnabled = false
        button.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.color_5F5F5F)
        progress.visibility = View.VISIBLE
        setTextGroupVisibility(false)
    }

    private fun resetButtonAfterLoading(
        button: AppCompatTextView,
        progress: View,
        plan: Int
    ) {
        progress.visibility = View.GONE
        if (plan == 1) showMainProgress(false)

        button.apply {
            isEnabled = true
            text = getString(R.string.try_for_free)
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.color_8147FF)
        }
        setTextGroupVisibility(true)
    }

    private fun showMainProgress(show: Boolean) {
        binding.apply {
            val visibilityMain = if (show) View.INVISIBLE else View.VISIBLE
            val visibilityProgress = if (show) View.VISIBLE else View.GONE

            linearLayout.visibility = visibilityMain
            linearLayout1.visibility = visibilityMain

            progress.visibility = visibilityProgress
            progress1.visibility = visibilityProgress

            val color = ContextCompat.getColor(requireContext(), R.color.color_0F1E47)
            progress.indeterminateTintList = ColorStateList.valueOf(color)
            progress1.indeterminateTintList = ColorStateList.valueOf(color)
        }
    }

    private fun setTextGroupVisibility(show: Boolean) {
        binding.apply {
            txtAutoRenew.visibility = if (show) View.VISIBLE else View.INVISIBLE
            txtNoPayment.visibility = if (show) View.VISIBLE else View.INVISIBLE
            txtCancel.visibility = if (show) View.GONE else View.INVISIBLE
        }
    }

    private fun showTryForFreeLayout() {
        binding.apply {
            frameLayout.visibility = View.VISIBLE
            frameLayout2.visibility = View.GONE
            txtNoPayment.visibility = View.VISIBLE
            txtCancel.visibility = View.GONE
        }
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}