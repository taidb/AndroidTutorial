package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.android.billingclient.api.ProductDetails
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.billing.BillingManager
import com.eco.musicplayer.audioplayer.music.activity.billing.asProductDetailsOffer
import com.eco.musicplayer.audioplayer.music.activity.billing.model.IN_APP
import com.eco.musicplayer.audioplayer.music.activity.billing.model.ProductDetailsOffer
import com.eco.musicplayer.audioplayer.music.activity.billing.model.ProductInfo
import com.eco.musicplayer.audioplayer.music.activity.billing.model.SUBS
import com.eco.musicplayer.audioplayer.music.activity.billing.queryAlls
import com.eco.musicplayer.audioplayer.music.activity.billing.state.BillingQueryState

import com.eco.musicplayer.audioplayer.music.activity.util.SpannableHelper
import com.eco.musicplayer.audioplayer.music.databinding.ActivityLayoutPaywallBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PaywallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayoutPaywallBinding
    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var billingManager: BillingManager

    // val TAG = "BillingOffers"
    private val yearlyProductId = "test2"
    private val weeklyProductId = "test1"     // Weekly
    private val lifetimeProductId = "test3"
    private val productInfos = listOf(
        ProductInfo(SUBS, yearlyProductId, false),
        ProductInfo(SUBS, weeklyProductId, false),
        ProductInfo(IN_APP, lifetimeProductId, false)
    )
    private var yearlyProduct :ProductDetailsOffer?=null
    private var weeklyProduct :ProductDetailsOffer?=null
    private var lifeTimeProduct :ProductDetailsOffer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywallBinding.inflate(layoutInflater)
        binding = ActivityLayoutPaywallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindow()
        initUI()
        setupBottomSheet()
        setupSpannableText()
        setupClickListeners()
        setupBillingManager()
        binding.constraintLayout.updateLayoutParams {
            height = (resources.displayMetrics.heightPixels * 0.58).toInt()
        }
        // initializeBilling()
    }

    private fun setupBillingManager() {
        billingManager = BillingManager(this)
        billingManager.queryAlls(
            identity = this::class.java.simpleName,
            productInfos =productInfos,
            onDataState = { state ->
                when (state){
                   is BillingQueryState.ProductDetailsComplete -> {
                       updateProductDetailsUI(state.products)
                   }
                    is BillingQueryState.PurchaseComplete ->{
                       // checkPurchases(state.purchases)
                    }
                    is BillingQueryState.Error ->{
                       // handleBillingError(state.exception)
                    }
                }
            }
        )
    }
    private fun updateProductDetailsUI(products: List<ProductDetails>){
        products.forEach { productDetails ->
            android.util.Log.d(
                "BILLING_PRODUCT", """ 
                ProductId: ${productDetails.productId}
                Title:${productDetails.title}
                Description: ${productDetails.description}
                Offer Token(s): ${productDetails.subscriptionOfferDetails}
                OneTimePrice: ${productDetails.oneTimePurchaseOfferDetails?.formattedPrice}
                """.trimIndent()
            )

            val offer = productDetails.asProductDetailsOffer()
            android.util.Log.d(
                "BILLING_PRODUCT", """ 
                ProductId: ${productDetails.productId}
                Title:${productDetails.title}
                Description: ${productDetails.description}
                Offer Token(s): ${productDetails.subscriptionOfferDetails}
                OneTimePrice: ${productDetails.oneTimePurchaseOfferDetails?.formattedPrice}
                """.trimIndent()
            )

            when (productDetails.productId) {
                yearlyProductId -> {
                    yearlyProduct = offer

                }

                weeklyProductId -> {
                }

            }
        }
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
//
//    private fun initializeBilling() {
//        billingManager = BillingManager(this)
//        billingManager.initializeBilling(object : BillingCallback {
//            override fun onProductsLoaded(products: List<ProductDetails>) {
//                val test2Product = products.find { it.productId == "test2" }
//
//                if (test2Product != null) {
//                    val offerList = test2Product.subscriptionOfferDetails
//                    if (!offerList.isNullOrEmpty()) {
//                        offerList.forEach { offer ->
//                            Log.d(TAG, "-------------------------------------------")
//                            Log.d(TAG, "Offer ID: ${offer.offerId}")
//                            Log.d(TAG, "Base Plan ID: ${offer.basePlanId}")
//                            Log.d(TAG, "Offer Token: ${offer.offerToken}")
//                            Log.d(TAG, "Tags: ${offer.offerTags}")
//
//                            // Mỗi offer có thể có nhiều giai đoạn giá (pricing phase)
//                            offer.pricingPhases.pricingPhaseList.forEachIndexed { index, phase ->
//                                Log.d(TAG, "  Phase $index:")
//                                Log.d(TAG, "    Price: ${phase.formattedPrice}")
//                                Log.d(TAG, "    Billing Period: ${phase.billingPeriod}")
//                                Log.d(TAG, "    Recurrence Mode: ${phase.recurrenceMode}")
//                                Log.d(TAG, "    Duration (cycle count): ${phase.billingCycleCount}")
//                                Log.d(TAG, "    Price (micros): ${phase.priceAmountMicros}")
//                            }
//                        }
//                    } else {
//                        Log.w(TAG, "Không có offer nào cho sản phẩm test2")
//                    }
//                } else {
//                    Log.e(TAG, "Không tìm thấy sản phẩm test2")
//                }
//                runOnUiThread {
//                    updateClaim(products)
//                }
//            }
//
//            override fun onError(error: String) {
//                setDefaultPrices()
//            }
//        })
//    }

//
//    private fun updateClaim(products: List<ProductDetails>) {
//        products.forEach { productsDetails ->
//            if (singlePack == productsDetails.productId) {
//                val subscriptionOfferDetails = productsDetails.subscriptionOfferDetails
//                if (subscriptionOfferDetails.isNullOrEmpty()) {
//                    return
//                }
//
//                val basePlan = findBasePlan(subscriptionOfferDetails)
//                basePlan?.let { offer ->
//                    val pricingPhases = offer.pricingPhases.pricingPhaseList
//                    if (pricingPhases.isNotEmpty()) {
//                        val firstPhase = pricingPhases[0]
//                        if (firstPhase.priceAmountMicros > 0) {
//                            val oldPriceFormatted = formatPrice(firstPhase.priceAmountMicros)
//                            binding.txtPriceOff.text = getString(R.string.price_off, oldPriceFormatted)
//                            val decreasedPrice = calculateWeeklyPrice(firstPhase)
//                            binding.txtPrice.text = getString(R.string.price, decreasedPrice)
//
//                            binding.txtDescription.text = getString(R.string.billed_weekly_cancel_anytime, oldPriceFormatted, decreasedPrice)
//                        } else {
//                            if (pricingPhases.size > 1) {
//                                val nextPhase = pricingPhases[1]
//                                val oldPriceFormatted = formatPrice(nextPhase.priceAmountMicros)
//                                binding.txtPriceOff.text = getString(R.string.price_off, oldPriceFormatted)
//                                val decreasedPrice = calculateWeeklyPrice(nextPhase)
//                                binding.txtPrice.text = getString(R.string.price, decreasedPrice)
//
//                                binding.txtDescription.text = getString(R.string.billed_weekly_cancel_anytime, oldPriceFormatted, decreasedPrice)
//                            }
//                        }
//                    }
//                } ?: run {
//                    val defaultOldPrice = getString(R.string.price_week)
//                    val defaultNewPrice = getString(R.string.price_year)
//                    binding.txtPriceOff.text = defaultOldPrice
//                    binding.idDiscount.text = percent.toString()
//                    binding.txtPrice.text = defaultNewPrice
//
//                    binding.txtDescription.text = getString(R.string.billed_weekly_cancel_anytime, defaultOldPrice, defaultNewPrice)
//                }
//            }
//        }
//    }
//
//    private fun calculateWeeklyPrice(pricingPhase: ProductDetails.PricingPhase): String {
//        val oldPriceVND = pricingPhase.priceAmountMicros / 1000000.0
//        val newPriceVND = oldPriceVND * (100 - percent) / 100
//
//        val formatter = java.text.DecimalFormat("#,###")
//        return "${formatter.format(newPriceVND)} đ"
//    }
//
//    private fun formatPrice(micros: Long): String {
//        val vndAmount = micros / 1000000.0
//        val formatter = java.text.DecimalFormat("#,###.##")
//
//        return "${formatter.format(vndAmount)} đ"
//    }


    private fun findBasePlan(offerDetails: List<ProductDetails.SubscriptionOfferDetails>): ProductDetails.SubscriptionOfferDetails? {
        return offerDetails.firstOrNull { offer ->
            offer.pricingPhases.pricingPhaseList.any { phase ->
                phase.priceAmountMicros > 0L
            }
        } ?: offerDetails.firstOrNull()
    }

    private fun setDefaultPrices() {
        val defaultOldPrice = getString(R.string.price_week)
        val defaultNewPrice = getString(R.string.price_year)
        binding.txtPriceOff.text = defaultOldPrice
        //  binding.idDiscount.text = percent.toString()
        binding.txtPrice.text = defaultNewPrice

        binding.txtDescription.text =
            getString(R.string.billed_weekly_cancel_anytime, defaultNewPrice, defaultOldPrice)
    }

    private fun initUI() {
        binding.txtPriceOff.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        binding.txtTryAgain.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        //binding.idDiscount.text = getString(R.string.discount, percent)

    }

    private fun setupBottomSheet() {
        behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.isFitToContents = true
        behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.45).toInt()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }


    private fun setupSpannableText() {
        SpannableHelper.setupTermsAndPrivacyText(
            this,
            binding.txtPrivacyPolicies,
            onTermsClick = {
                println("TERMS CLICKED IN ACTIVITY")
                Toast.makeText(this, "Điều khoản dịch vụ", Toast.LENGTH_SHORT).show()
            },
            onPrivacyClick = {
                println("PRIVACY CLICKED IN ACTIVITY")
                Toast.makeText(this, "Chính sách bảo mật", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun getOfferTokenForFreeTrial(productDetails: ProductDetails): String {
        val subscriptionOfferDetails = productDetails.subscriptionOfferDetails
        if (subscriptionOfferDetails.isNullOrEmpty()) return ""

        subscriptionOfferDetails.forEach { offer ->
            offer.pricingPhases.pricingPhaseList.forEach { phase ->
                if (phase.priceAmountMicros == 0L) {
                    return offer.offerToken
                }
            }
        }

        return getBasePlanOfferToken(productDetails)
    }

    private fun getBasePlanOfferToken(productDetails: ProductDetails): String {
        val subscriptionOfferDetails = productDetails.subscriptionOfferDetails
        if (subscriptionOfferDetails.isNullOrEmpty()) return ""

        subscriptionOfferDetails.forEach { offer ->
            offer.pricingPhases.pricingPhaseList.forEach { phase ->
                if (phase.priceAmountMicros > 0L) {
                    return offer.offerToken
                }
            }
        }

        return subscriptionOfferDetails.firstOrNull()?.offerToken ?: ""
    }

    private fun setupClickListeners() {
        binding.icClose.setOnClickListener { finish() }

        binding.btnClaim.setOnClickListener {
            handleClaimClick()
        }
        binding.txtTryAgain.setOnClickListener {
            resetUI()
        }
    }

    private fun resetUI() {
        runOnUiThread {
            with(binding) {
                // Reset nút Claim
                btnClaim.apply {
                    text = getString(R.string.claim_offer)
                    isEnabled = true
                    visibility = View.VISIBLE
                    alpha = 1f
                }

                // Reset progress và loading
                progress.visibility = View.GONE
                progress.progress = 0

                // Reset visibility của các layout
                linearLayout.visibility = View.VISIBLE
                linearLayout1.visibility = View.VISIBLE
                linearLayout2.visibility = View.GONE

                // Reset các view khác
                view.visibility = View.VISIBLE
                txtDescription.visibility = View.VISIBLE
                view.visibility = View.GONE


            }

        }
    }


    private fun handleClaimClick() {
//        if (!::billingManager.isInitialized) {
//            Toast.makeText(this, "Billing đang khởi tạo...", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val productDetails = billingManager.getProductDetails(singlePack)
//        if (productDetails == null) {
//            Toast.makeText(this, "Sản phẩm không khả dụng", Toast.LENGTH_SHORT).show()
//            return
//        }
//        productDetails.let { details ->
//            val offerToken = getOfferTokenForFreeTrial(details)
//            billingManager.launchBillingFlow(this, details, offerToken)
//        }

        with(binding) {
            btnClaim.text = ""
            btnClaim.isEnabled = false
            progress.visibility = View.VISIBLE
            linearLayout.visibility = View.INVISIBLE
            linearLayout1.visibility = View.INVISIBLE
            view.visibility = View.VISIBLE
            txtDescription.visibility = View.INVISIBLE
        }

        Handler(Looper.getMainLooper()).postDelayed({
            with(binding) {
                view.visibility = View.INVISIBLE
                progress.visibility = View.GONE
                btnClaim.visibility = View.INVISIBLE
                linearLayout2.visibility = View.VISIBLE
            }
        }, 3000)

    }
}