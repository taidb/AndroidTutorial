package com.eco.musicplayer.audioplayer.music.activity.designlayout

import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.android.billingclient.api.ProductDetails
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.activity.billing.BillingCallback
import com.eco.musicplayer.audioplayer.music.activity.billing.BillingManager
import com.eco.musicplayer.audioplayer.music.activity.util.SpannableHelper
import com.eco.musicplayer.audioplayer.music.databinding.ActivityLayoutPaywallBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class LayoutPaywallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayoutPaywallBinding
    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var billingManager: BillingManager
    private val singlePack = "test2"
    private val percent = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutPaywallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        setupBottomSheet()
        setupSpannableText()
        setupClickListeners()

        binding.constraintLayout.updateLayoutParams {
            height = (resources.displayMetrics.heightPixels * 0.68).toInt()
        }
        initializeBilling()
    }

    private fun initializeBilling() {
        billingManager = BillingManager(this)
        billingManager.initializeBilling(object : BillingCallback {
            override fun onProductsLoaded(products: List<ProductDetails>) {
                runOnUiThread {
                    updateClaim(products)
                }
            }

            override fun onError(error: String) {
                setDefaultPrices()
            }
        })
    }


    private fun updateClaim(products: List<ProductDetails>) {
        products.forEach { productsDetails ->
            if (singlePack == productsDetails.productId) {
                val subscriptionOfferDetails = productsDetails.subscriptionOfferDetails
                if (subscriptionOfferDetails.isNullOrEmpty()) {
                    return
                }

                val basePlan = findBasePlan(subscriptionOfferDetails)
                basePlan?.let { offer ->
                    val pricingPhases = offer.pricingPhases.pricingPhaseList
                    if (pricingPhases.isNotEmpty()) {
                        val firstPhase = pricingPhases[0]
                        if (firstPhase.priceAmountMicros > 0) {
                            val oldPriceFormatted = formatPrice(firstPhase.priceAmountMicros)
                            binding.txtPriceOff.text = getString(R.string.price_off, oldPriceFormatted)
                            val decreasedPrice = calculateWeeklyPrice(firstPhase)
                            binding.txtPrice.text = getString(R.string.price, decreasedPrice)

                            binding.txtDescription.text = getString(R.string.billed_weekly_cancel_anytime, oldPriceFormatted, decreasedPrice)
                        } else {
                            if (pricingPhases.size > 1) {
                                val nextPhase = pricingPhases[1]
                                val oldPriceFormatted = formatPrice(nextPhase.priceAmountMicros)
                                binding.txtPriceOff.text = getString(R.string.price_off, oldPriceFormatted)
                                val decreasedPrice = calculateWeeklyPrice(nextPhase)
                                binding.txtPrice.text = getString(R.string.price, decreasedPrice)

                                binding.txtDescription.text = getString(R.string.billed_weekly_cancel_anytime, oldPriceFormatted, decreasedPrice)
                            }
                        }
                    }
                } ?: run {
                    val defaultOldPrice = getString(R.string.price_week)
                    val defaultNewPrice = getString(R.string.price_year)
                    binding.txtPriceOff.text = defaultOldPrice
                    binding.idDiscount.text = percent.toString()
                    binding.txtPrice.text = defaultNewPrice

                    binding.txtDescription.text = getString(R.string.billed_weekly_cancel_anytime, defaultOldPrice, defaultNewPrice)
                }
            }
        }
    }

    private fun calculateWeeklyPrice(pricingPhase: ProductDetails.PricingPhase): String {
        val oldPriceVND = pricingPhase.priceAmountMicros / 1000000.0
        val newPriceVND = oldPriceVND * (100 - percent) / 100

        val formatter = java.text.DecimalFormat("#,###")
        return "${formatter.format(newPriceVND)} đ"
    }

    private fun formatPrice(micros: Long): String {
        val vndAmount = micros / 1000000.0
        val formatter = java.text.DecimalFormat("#,###.##")

        return "${formatter.format(vndAmount)} đ"
    }


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
        binding.idDiscount.text = percent.toString()
        binding.txtPrice.text = defaultNewPrice

        binding.txtDescription.text = getString(R.string.billed_weekly_cancel_anytime, defaultNewPrice, defaultOldPrice)
    }

    private fun initUI() {
        binding.txtPriceOff.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        binding.txtTryAgain.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.idDiscount.text = getString(R.string.discount, percent)

    }

    private fun setupBottomSheet() {
        behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.isFitToContents = true
        behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.35).toInt()
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
        binding.idClose.setOnClickListener { finish() }

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
                view.visibility=View.GONE


            }

            // Hiển thị thông báo reset thành công (tuỳ chọn)
            Toast.makeText(this, "Đã reset giao diện", Toast.LENGTH_SHORT).show()
        }
    }


    private fun handleClaimClick() {
        if (!::billingManager.isInitialized) {
            Toast.makeText(this, "Billing đang khởi tạo...", Toast.LENGTH_SHORT).show()
            return
        }

        val productDetails = billingManager.getProductDetails(singlePack)
        if (productDetails == null) {
            Toast.makeText(this, "Sản phẩm không khả dụng", Toast.LENGTH_SHORT).show()
            return
        }
        productDetails.let { details ->
            val offerToken = getOfferTokenForFreeTrial(details)
            billingManager.launchBillingFlow(this, details, offerToken)
        }
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
                btnClaim.visibility = View.GONE
                linearLayout2.visibility = View.VISIBLE
            }
        }, 3000)
    }
}