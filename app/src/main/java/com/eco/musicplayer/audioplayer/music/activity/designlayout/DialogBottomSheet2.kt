package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.android.billingclient.api.Purchase
import com.eco.musicplayer.audioplayer.music.activity.billing.asProductDetailsOffer
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.billing.BillingManager
import com.eco.musicplayer.audioplayer.music.activity.billing.buy
import com.eco.musicplayer.audioplayer.music.activity.billing.model.IN_APP
import com.eco.musicplayer.audioplayer.music.activity.billing.model.ProductDetailsOffer
import com.eco.musicplayer.audioplayer.music.activity.billing.model.ProductInfo
import com.eco.musicplayer.audioplayer.music.activity.billing.model.SUBS
import com.eco.musicplayer.audioplayer.music.activity.billing.queryAlls
import com.eco.musicplayer.audioplayer.music.activity.billing.state.BillingPurchasesState
import com.eco.musicplayer.audioplayer.music.activity.billing.state.BillingQueryState
import com.eco.musicplayer.audioplayer.music.databinding.ActivityDialogBottomSheet2Binding


class DialogBottomSheet2 : AppCompatActivity() {
    private lateinit var binding: ActivityDialogBottomSheet2Binding
    private lateinit var billingManager: BillingManager
    private lateinit var sharedPreferences: SharedPreferences

    private var selectedPlan = 1
    private var hasUsedFreeTrial = false

    // Product IDs
    private val yearlyProductId = "test1"  // Yearly with free trial
    private val weeklyProductId = "free_123"     // Weekly
    private val lifetimeProductId = "test3"   // Lifetime

    private val productInfos = listOf(
        ProductInfo(SUBS, yearlyProductId, false),
        ProductInfo(SUBS, weeklyProductId, false),
        ProductInfo(IN_APP, lifetimeProductId, false)
    )

    private var yearlyProduct: ProductDetailsOffer? = null
    private var weeklyProduct: ProductDetailsOffer? = null
    private var lifetimeProduct: ProductDetailsOffer? = null

    companion object {
        private const val PREFS_NAME = "billing_prefs"
        private const val KEY_HAS_USED_FREE_TRIAL = "has_used_free_trial"
        private const val KEY_FREE_TRIAL_PRODUCT_ID = "free_trial_product_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBottomSheet2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        hasUsedFreeTrial = sharedPreferences.getBoolean(KEY_HAS_USED_FREE_TRIAL, false)

        setupWindow()
        setupBillingManager()
        setupListeners()
        selectPlan(1)
    }

    private fun setupBillingManager() {
        billingManager = BillingManager(this)

        billingManager.queryAlls(
            identity = this::class.java.simpleName,
            productInfos = productInfos,
            onDataState = { state ->
                when (state) {
                    is BillingQueryState.ProductDetailsComplete -> {
                        updateProductDetailsUI(state.products)
                    }
                    is BillingQueryState.PurchaseComplete -> {
                        checkPurchases(state.purchases)
                    }
                    is BillingQueryState.Error -> {
                        handleBillingError(state.exception)
                    }
                }
            }
        )
    }

    private fun updateProductDetailsUI(products: List<com.android.billingclient.api.ProductDetails>) {
        products.forEach { productDetails ->
            val offer = productDetails.asProductDetailsOffer()
            when (productDetails.productId) {
                yearlyProductId -> {
                    yearlyProduct = offer
                    updateYearlyPlanUI(offer)
                }
                weeklyProductId -> {
                    weeklyProduct = offer
                    updateWeeklyPlanUI(offer)
                }
                lifetimeProductId -> {
                    lifetimeProduct = offer
                    // Có thể thêm lifetime plan nếu cần
                }
            }
        }

        // Sau khi có product details, kiểm tra lại UI dựa trên trạng thái free trial
        updateUIForFreeTrialStatus()
    }

    private fun updateYearlyPlanUI(offer: ProductDetailsOffer) {
        binding.txtPrice2.text = offer.formattedPrice

        // Tính toán giá theo tuần
        val weeklyPriceMicros = when (offer.typePeriod) {
            ProductDetailsOffer.TypePeriod.YEAR -> offer.priceAmountMicros / 52
            ProductDetailsOffer.TypePeriod.MONTH -> offer.priceAmountMicros / 4
            else -> offer.priceAmountMicros
        }
        val weeklyPrice = weeklyPriceMicros / 1000000.0

        binding.txtDesPrice1.text = getString(R.string.only_price_per_week, "$${String.format("%.3f", weeklyPrice)}")

        // Cập nhật free trial info
        if (offer.typeOffer != ProductDetailsOffer.TypeOffer.NONE && !hasUsedFreeTrial) {
            binding.txtAutoRenew.text = getString(
                R.string.after_free_trial_ends_yearly_max,
                offer.formattedPrice
            )
        } else {
//            binding.txtAutoRenew.text = getString(R.string.auto_renew_description)
        }
    }

    private fun updateWeeklyPlanUI(offer: ProductDetailsOffer) {
        binding.txtPrice.text = offer.formattedPrice
    }

    private fun checkPurchases(purchases: List<Purchase>) {
        val hasActiveSubscription = checkIfUserHasActiveSubscription(purchases)
        val hasUsedFreeTrialBefore = checkIfUserUsedFreeTrial(purchases)

        hasUsedFreeTrial = hasActiveSubscription || hasUsedFreeTrialBefore

        if (hasUsedFreeTrial) {
            saveFreeTrialUsage()
            updateUIForFreeTrialStatus()
        }
    }

    private fun checkIfUserHasActiveSubscription(purchases: List<Purchase>): Boolean {
        return purchases.any { purchase ->
            purchase.products.any { productId ->
                (productId == yearlyProductId || productId == weeklyProductId) &&
                        purchase.isAcknowledged
            }
        }
    }

    private fun checkIfUserUsedFreeTrial(purchases: List<Purchase>): Boolean {
        // Kiểm tra purchase history từ SharedPreferences
        if (sharedPreferences.getBoolean(KEY_HAS_USED_FREE_TRIAL, false)) {
            return true
        }

        // Kiểm tra các purchase cũ có product ID của free trial
        return purchases.any { purchase ->
            purchase.products.contains(yearlyProductId) && purchase.isAcknowledged
        }
    }

    private fun saveFreeTrialUsage() {
        sharedPreferences.edit()
            .putBoolean(KEY_HAS_USED_FREE_TRIAL, true)
            .putString(KEY_FREE_TRIAL_PRODUCT_ID, yearlyProductId)
            .apply()
    }

    private fun updateUIForFreeTrialStatus() {
        if (hasUsedFreeTrial) {
            // User đã dùng free trial, ẩn nút Try for Free
            showContinueLayout()
            updateUITextForUsedFreeTrial()
        } else {
            // User chưa dùng free trial, hiển thị bình thường
            showTryForFreeLayout()
        }
    }

    private fun updateUITextForUsedFreeTrial() {
//        binding.txtAutoRenew.text = getString(R.string.auto_renew_description)
        binding.txtNoPayment.visibility = View.GONE
        binding.txtCancel.visibility = View.VISIBLE

        binding.btnTryForFree.text = getString(R.string.continue1)
    }

    @SuppressLint("UseKtx")
    private fun setupWindow() {
        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#80000000")))
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(this@DialogBottomSheet2, R.color.color_FFF6E7)
            navigationBarColor = Color.TRANSPARENT

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                setDecorFitsSystemWindows(false)
                val controller = decorView.windowInsetsController
                controller?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        )
            } else {
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        )
            }
        }
    }

    private fun setupListeners() {
        binding.icClose.setOnClickListener { finish() }
        binding.btnIap1.setOnClickListener { selectPlan(1) }
        binding.btnIap2.setOnClickListener { selectPlan(2) }

        binding.btnTryForFree.setOnClickListener {
            handleTryForFreeClick()
        }
        binding.btnContinue.setOnClickListener {
            handleContinueClick()
        }
    }

    private fun selectPlan(plan: Int) {
        selectedPlan = plan
        when (plan) {
            1 -> {
                binding.btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_selected)
                binding.btnIap2.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
                binding.tvMostPopular.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.color_8147FF)

                yearlyProduct?.let {
                    if (!hasUsedFreeTrial) {
                        binding.txtAutoRenew.text =
                            getString(R.string.after_free_trial_ends_yearly_max, it.formattedPrice)
                    } else {
//                        binding.txtAutoRenew.text = getString(R.string.auto_renew_description)
                    }
                }

                if (!hasUsedFreeTrial) {
                    showTryForFreeLayout()
                } else {
                    showContinueLayout()
                }
            }
            2 -> {
                binding.btnIap2.setBackgroundResource(R.drawable.bg_btn_pw_4_selected)
                binding.btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
                binding.tvMostPopular.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.color_908DAC)

                weeklyProduct?.let {
//                    binding.txtAutoRenew.text = getString(R.string.auto_renew_description)
                }
                showContinueLayout()
            }
        }
    }

    private fun handleTryForFreeClick() {
        if (selectedPlan == 1 && !hasUsedFreeTrial) {
            showLoadingState(true)
            handleButtonLoading(binding.btnTryForFree, binding.progress2, selectedPlan)

            // Thực hiện purchase với free trial
            yearlyProduct?.let { offer ->
                billingManager.buy(
                    activity = this,
                    productDetails = offer.productDetails,
                    offerToken = offer.offerToken,
                    onBillingPurchasesListener = { state ->
                        handlePurchaseResult(state)
                    }
                )
            }
        } else {
            handleContinueClick()
        }
    }

    private fun handleContinueClick() {
        val product = when (selectedPlan) {
            1 -> yearlyProduct
            2 -> weeklyProduct
            else -> null
        }

        product?.let { offer ->
            showLoadingState(true)
            handleButtonLoading(
                if (selectedPlan == 1 && !hasUsedFreeTrial) binding.btnTryForFree else binding.btnContinue,
                if (selectedPlan == 1 && !hasUsedFreeTrial) binding.progress2 else binding.progress3,
                selectedPlan
            )

            billingManager.buy(
                activity = this,
                productDetails = offer.productDetails,
                offerToken = offer.offerToken,
                onBillingPurchasesListener = { state ->
                    handlePurchaseResult(state)
                }
            )
        }
    }

    private fun handlePurchaseResult(state: BillingPurchasesState) {
        when (state) {
            is BillingPurchasesState.PurchaseAcknowledged -> {
                // Purchase thành công - đánh dấu đã dùng free trial nếu là yearly plan
                if (selectedPlan == 1 && !hasUsedFreeTrial) {
                    hasUsedFreeTrial = true
                    saveFreeTrialUsage()
                    updateUIForFreeTrialStatus()
                }
                showPurchaseSuccess()
            }
            is BillingPurchasesState.AcknowledgePurchaseLoading -> {
                showLoadingState(state.isLoading)
            }
            is BillingPurchasesState.UserCancelPurchase -> {
                showPurchaseCancelled()
            }
            is BillingPurchasesState.Error -> {
                handleBillingError(state.exception)
            }
        }
    }

    private fun handleBillingError(exception: Exception) {
        showLoadingState(false)
        // Có thể hiển thị dialog thông báo lỗi
        // Toast.makeText(this, "Purchase failed: ${exception.message}", Toast.LENGTH_SHORT).show()
    }

    private fun showPurchaseSuccess() {
        showLoadingState(false)
        // Có thể chuyển sang màn hình chính hoặc hiển thị thông báo thành công
        finish()
    }

    private fun showPurchaseCancelled() {
        showLoadingState(false)
        // Có thể hiển thị thông báo user đã hủy
    }

    private fun showLoadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.linearLayout.visibility = View.INVISIBLE
            binding.linearLayout1.visibility = View.INVISIBLE
            binding.progress.visibility = View.VISIBLE
            binding.progress.indeterminateTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_0F1E47))
            binding.progress1.visibility = View.VISIBLE
            binding.progress1.indeterminateTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_0F1E47))
        } else {
            binding.linearLayout.visibility = View.VISIBLE
            binding.linearLayout1.visibility = View.VISIBLE
            binding.progress.visibility = View.GONE
            binding.progress1.visibility = View.GONE
        }
    }

    private fun handleButtonLoading(
        button: AppCompatTextView,
        progress: View,
        plan: Int = -1
    ) {
        button.apply {
            text = ""
            isEnabled = false
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.color_5F5F5F)
        }

        setInfoTextsVisibility(isVisible = false)
        progress.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            progress.visibility = View.GONE

            if (plan == 1) showLoadingState(false)

            button.apply {
                isEnabled = true
                text = getString(
                    if (button.id == R.id.btnTryForFree && !hasUsedFreeTrial) R.string.try_for_free else R.string.continue1
                )
                backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.color_8147FF)
            }

            setInfoTextsVisibility(isVisible = true)
        }, 2000)
    }

    private fun setInfoTextsVisibility(isVisible: Boolean) {
        binding.txtAutoRenew.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        if (!hasUsedFreeTrial) {
            binding.txtNoPayment.visibility = if (isVisible) View.VISIBLE else View.GONE
            binding.txtCancel.visibility = if (isVisible) View.GONE else View.INVISIBLE
        } else {
            binding.txtNoPayment.visibility = View.GONE
            binding.txtCancel.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun showTryForFreeLayout() {
        binding.frameLayout.visibility = View.VISIBLE
        binding.frameLayout2.visibility = View.GONE
        if (!hasUsedFreeTrial) {
            binding.txtNoPayment.visibility = View.VISIBLE
            binding.txtCancel.visibility = View.GONE
        }
    }

    private fun showContinueLayout() {
        binding.frameLayout.visibility = View.GONE
        binding.frameLayout2.visibility = View.VISIBLE
        binding.txtNoPayment.visibility = View.GONE
        binding.txtCancel.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        billingManager.detachListeners(this::class.java.simpleName)
    }
}