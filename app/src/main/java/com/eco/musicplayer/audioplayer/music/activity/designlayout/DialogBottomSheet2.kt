package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.android.billingclient.api.ProductDetails
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.billing.BillingCallback
import com.eco.musicplayer.audioplayer.music.activity.billing.BillingManager
import com.eco.musicplayer.audioplayer.music.activity.remoteconfig.InAppProduct
import com.eco.musicplayer.audioplayer.music.activity.remoteconfig.PaywallConfig
import com.eco.musicplayer.audioplayer.music.activity.remoteconfig.RemoteConfig
import com.eco.musicplayer.audioplayer.music.databinding.ActivityDialogBottomSheet2Binding

class DialogBottomSheet2 : AppCompatActivity() {
    private lateinit var binding: ActivityDialogBottomSheet2Binding
    private var selectedPlan = 1
    private lateinit var billingManager: BillingManager
    val TAG = "BillingOffers"
    private lateinit var remoteConfig: RemoteConfig
    private var paywallConfig: PaywallConfig? = null
    private val planProductMap = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBottomSheet2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindow()
        initializeRemoteConfig()
        //setupInitialLayout()
        setupListeners()
    }


    @SuppressLint("UseKtx")
    private fun setupWindow() {
        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#80000000")))
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT

            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
        }
    }

    private fun initializeRemoteConfig() {
        remoteConfig = RemoteConfig()
        remoteConfig.fetchAndActivate {
            paywallConfig = remoteConfig.getPaywallConfig()
            paywallConfig?.let { config ->
                updatePlanMapping(config.products)
                initializeBilling()
            }
        }
    }

    private fun updatePlanMapping(products: List<InAppProduct>?) {
        products?.let { productList ->
            productList.forEachIndexed { index, product ->
                planProductMap[index + 1] = product.productId ?: ""
            }
        }

        if (planProductMap.isEmpty()) {
            planProductMap[1] = "test1"
            planProductMap[2] = "free_123"
        }
    }

    private fun initializeBilling() {
        billingManager = BillingManager(this)
        paywallConfig?.products?.let { products ->
            billingManager.setRemoteConfigProducts(products)
        }
        billingManager.initializeBilling(object : BillingCallback {
            override fun onProductsLoaded(products: List<ProductDetails>) {
                val test1Product = products.find { it.productId == "test1" }
                val test2Product = products.find { it.productId == "free_123" }

                if (test1Product != null) {
                    val offerList = test1Product.subscriptionOfferDetails
                    if (!offerList.isNullOrEmpty()) {
                        offerList.forEach { offer ->
                            Log.d(TAG, "-------------------------------------------")
                            Log.d(TAG, "Offer ID: ${offer.offerId}")
                            Log.d(TAG, "Base Plan ID: ${offer.basePlanId}")
                            Log.d(TAG, "Offer Token: ${offer.offerToken}")
                            Log.d(TAG, "Tags: ${offer.offerTags}")

                            // Mỗi offer có thể có nhiều giai đoạn giá (pricing phase)
                            offer.pricingPhases.pricingPhaseList.forEachIndexed { index, phase ->
                                Log.d(TAG, "  Phase $index:")
                                Log.d(TAG, "    Price: ${phase.formattedPrice}")
                                Log.d(TAG, "    Billing Period: ${phase.billingPeriod}")
                                Log.d(TAG, "    Recurrence Mode: ${phase.recurrenceMode}")
                                Log.d(TAG, "    Duration (cycle count): ${phase.billingCycleCount}")
                                Log.d(TAG, "    Price (micros): ${phase.priceAmountMicros}")
                            }
                        }
                    } else {
                        Log.w(TAG, "Không có offer nào cho sản phẩm test2")
                    }
                } else {
                    Log.e(TAG, "Không tìm thấy sản phẩm test2")
                }
                if (test2Product != null) {
                    val offerList = test2Product.subscriptionOfferDetails
                    if (!offerList.isNullOrEmpty()) {
                        offerList.forEach { offer ->
                            Log.d(TAG, "-------------------------------------------")
                            Log.d(TAG, "Offer ID: ${offer.offerId}")
                            Log.d(TAG, "Base Plan ID: ${offer.basePlanId}")
                            Log.d(TAG, "Offer Token: ${offer.offerToken}")
                            Log.d(TAG, "Tags: ${offer.offerTags}")

                            // Mỗi offer có thể có nhiều giai đoạn giá (pricing phase)
                            offer.pricingPhases.pricingPhaseList.forEachIndexed { index, phase ->
                                Log.d(TAG, "  Phase $index:")
                                Log.d(TAG, "    Price: ${phase.formattedPrice}")
                                Log.d(TAG, "    Billing Period: ${phase.billingPeriod}")
                                Log.d(TAG, "    Recurrence Mode: ${phase.recurrenceMode}")
                                Log.d(TAG, "    Duration (cycle count): ${phase.billingCycleCount}")
                                Log.d(TAG, "    Price (micros): ${phase.priceAmountMicros}")
                            }
                        }
                    } else {
                        Log.w(TAG, "Không có offer nào cho sản phẩm test2")
                    }
                } else {
                    Log.e(TAG, "Không tìm thấy sản phẩm test2")
                }
                runOnUiThread {
                    updateUIWithProductDetails(products)
                    val selectionPosition = paywallConfig?.selectionPosition ?: 1
                    selectPlan(selectionPosition)
                }
            }

            override fun onError(error: String) {
                runOnUiThread {
                    setDefaultPrices()
                    Log.e("Billing", "Error: $error")
                }
            }
        })
    }

    private fun updateUIWithProductDetails(products: List<ProductDetails>) {
        products.forEach { productDetails ->
            when (productDetails.productId) {
                planProductMap[2] -> updateYearlyPlanUI(productDetails)
                planProductMap[1] -> updateWeeklyPlanUI(productDetails)
            }
        }
    }

    private fun updateYearlyPlanUI(productDetails: ProductDetails) {
        val subscriptionOfferDetails = productDetails.subscriptionOfferDetails
        if (subscriptionOfferDetails.isNullOrEmpty()) {
            Log.e("Billing", "No subscription offers for yearly plan")
            return
        }

        val basePlan = findBasePlan(subscriptionOfferDetails)
        basePlan?.let { offer ->
            val pricingPhases = offer.pricingPhases.pricingPhaseList
            if (pricingPhases.isNotEmpty()) {
                // Lấy phase đầu tiên (thường là giá chính)
                val firstPhase = pricingPhases[0]

                // Chỉ hiển thị nếu giá > 0
                if (firstPhase.priceAmountMicros > 0) {
                    binding.txtTime1.text = firstPhase.formattedPrice
                    binding.txtTime2.text = getString(R.string.year)
                    binding.txtAutoRenew.text =getString(R.string.after_free_trial_ends_yearly_max,firstPhase.formattedPrice)
                    // Tính giá theo tuần để hiển thị
                    val weeklyPrice = calculateWeeklyPrice(firstPhase)
                    binding.txtDesPrice1.text =getString(R.string.only_price_per_week,weeklyPrice)
                } else {
                    // Nếu phase đầu tiên là free trial, tìm phase tiếp theo
                    if (pricingPhases.size > 1) {
                        val nextPhase = pricingPhases[1]
                        binding.txtTime1.text = nextPhase.formattedPrice
                        binding.txtTime2.text = getString(R.string.year)
                        binding.txtAutoRenew.text =getString(R.string.after_free_trial_ends_yearly_max,nextPhase.formattedPrice)
                        val weeklyPrice = calculateWeeklyPrice(nextPhase)
                        binding.txtDesPrice1.text =  getString(R.string.only_price_per_week,weeklyPrice)
                    }
                }
            }
        } ?: run {
            binding.txtTime1.text = getString(R.string.price_year)
            binding.txtTime2.text = getString(R.string.year)
            binding.txtDesPrice1.text =getString(R.string.only_price_per_week,"$19.99")
            binding.txtAutoRenew.text =getString(R.string.after_free_trial_ends_yearly_max,"19.99")
        }
    }


    private fun updateWeeklyPlanUI(productDetails: ProductDetails) {
        val subscriptionOfferDetails = productDetails.subscriptionOfferDetails
        if (subscriptionOfferDetails.isNullOrEmpty()) {
            Log.e("Billing", "No subscription offers for weekly plan")
            return
        }

        val basePlan = findBasePlan(subscriptionOfferDetails)
        basePlan?.let { offer ->
            val pricingPhases = offer.pricingPhases.pricingPhaseList
            if (pricingPhases.isNotEmpty()) {
                val firstPhase = pricingPhases[0]
                if (firstPhase.priceAmountMicros > 0) {
                    binding.txtPrice.text = firstPhase.formattedPrice
                    binding.txtTime4.text =getString(R.string.week)
                    binding.txtAutoRenew.text =getString(R.string.after_free_trial_ends_weekly,firstPhase.formattedPrice)
                } else {
                    if (pricingPhases.size > 1) {
                        val nextPhase = pricingPhases[1]
                        binding.txtPrice.text = nextPhase.formattedPrice
                        binding.txtTime4.text = getString(R.string.week)
                        binding.txtAutoRenew.text =getString(R.string.after_free_trial_ends_weekly,nextPhase.formattedPrice)
                    }
                }
            }
        } ?: run {
            binding.txtPrice.text = getString(R.string.price_week)
            binding.txtTime4.text = getString(R.string.week)
            binding.txtAutoRenew.text =getString(R.string.after_free_trial_ends_weekly,"4.99")
        }
    }

    private fun findBasePlan(offerDetails: List<ProductDetails.SubscriptionOfferDetails>):
            ProductDetails.SubscriptionOfferDetails? {

        //tìm plan không phải free trial
        offerDetails.forEach { offer ->
            offer.pricingPhases.pricingPhaseList.forEach { phase ->
                if (phase.priceAmountMicros > 0L) {
                    return offer
                }
            }
        }

        // Nếu không tìm thấy, trả về offer đầu tiên
        return offerDetails.firstOrNull()
    }

        private fun calculateWeeklyPrice(pricingPhase: ProductDetails.PricingPhase): String {
           // return when (pricingPhase.billingPeriod) {
           //     "P1W" -> { // 1 year
                    val yearlyPrice = pricingPhase.priceAmountMicros / 1000000.0
                    val weeklyPrice = yearlyPrice / 52.0
                    val formatter = java.text.DecimalFormat("#,###.##")

                 return  "${formatter.format(weeklyPrice)} đ"
                   // "$${String.format("%.3f", weeklyPrice)}"
//                }

//               // "P1M" -> { // 1 month
//                    val monthlyPrice = pricingPhase.priceAmountMicros / 1000000.0
//                    val weeklyPrice = monthlyPrice / 4.33
//                    val formatter = java.text.DecimalFormat("#,###.##")
//                    return  "${formatter.format(weeklyPrice)} đ"
//                    //"$${String.format("%.3f", weeklyPrice)}"
//              // }

//                else -> {
//                    pricingPhase.formattedPrice
//                }
        //    }
        }

    private fun setDefaultPrices() {
        binding.txtTime1.text =getString(R.string.price_year)
        binding.txtPrice.text = getString(R.string.price_week)
        binding.txtDesPrice1.text =getString(R.string.only_price_per_week, "$4.99")

    }

//    private fun setupInitialLayout() {
//        binding.btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_selected)
//        binding.btnIap2.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
//        binding.tvMostPopular.backgroundTintList =
//            ContextCompat.getColorStateList(this, R.color.color_8147FF)
//        showTryForFreeLayout()
//    }

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

                val price = binding.txtTime1.text.toString()
                binding.txtAutoRenew.text = getString(R.string.after_free_trial_ends_yearly_max, price)
                showTryForFreeLayout()
            }
            2 -> {
                binding.btnIap2.setBackgroundResource(R.drawable.bg_btn_pw_4_selected)
                binding.btnIap1.setBackgroundResource(R.drawable.bg_btn_pw_4_unselected)
                binding.tvMostPopular.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.color_908DAC)

                val price = binding.txtPrice.text.toString()
                binding.txtAutoRenew.text = getString(R.string.after_free_trial_ends_weekly, price)
                showContinueLayout()
            }
        }
    }

    private fun handleTryForFreeClick() {
        if (selectedPlan == 1) {
            showLoadingState(true)
            handleButtonLoading(binding.btnTryForFree, binding.progress2, selectedPlan)

            val productId = planProductMap[selectedPlan]
            productId?.let {
                val productDetails = billingManager.getProductDetails(it)
                productDetails?.let { details ->
                    // Sử dụng offer token từ remote config
                    val offerToken = billingManager.getOfferTokenForProduct(it, details)
                    billingManager.launchBillingFlow(this, details, offerToken)
                }
            }
        }
    }

    private fun handleContinueClick() {
        handleButtonLoading(binding.btnContinue, binding.progress3, selectedPlan)

        val productId = planProductMap[selectedPlan]
        productId?.let {
            val productDetails = billingManager.getProductDetails(it)
            productDetails?.let { details ->
                val offerToken =billingManager.getBasePlanOfferToken(details)
                billingManager.launchBillingFlow(this, details, offerToken)
            }
        }
    }




    private fun showLoadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.linearLayout.visibility = View.INVISIBLE
            binding.linearLayout1.visibility = View.INVISIBLE
            binding.progress.visibility = View.VISIBLE
            binding.progress.indeterminateTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_0F1E47C))
            binding.progress1.visibility = View.VISIBLE
            binding.progress1.indeterminateTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_0F1E47C))
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
                    if (id == R.id.btnTryForFree) R.string.try_for_free else R.string.continue1
                )
                backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.color_8147FF)
            }

            setInfoTextsVisibility(isVisible = true)
        }, 2000)
    }

    private fun setInfoTextsVisibility(isVisible: Boolean) {
        binding.txtAutoRenew.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        binding.txtNoPayment.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        binding.txtCancel.visibility = if (isVisible) View.GONE else View.INVISIBLE
    }

    private fun showTryForFreeLayout() {
        binding.frameLayout.visibility = View.VISIBLE
        binding.frameLayout2.visibility = View.GONE
        binding.txtNoPayment.visibility = View.VISIBLE
        binding.txtCancel.visibility = View.GONE
    }

    private fun showContinueLayout() {
        binding.frameLayout.visibility = View.GONE
        binding.frameLayout2.visibility = View.VISIBLE
        binding.txtNoPayment.visibility = View.GONE
        binding.txtCancel.visibility = View.VISIBLE
    }
}