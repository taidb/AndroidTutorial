package com.eco.musicplayer.audioplayer.music.activity.billing

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.android.billingclient.api.*
import com.eco.musicplayer.audioplayer.music.activity.remoteconfig.InAppProduct

class BillingManager(private val context: Context) {

    private lateinit var billingClient: BillingClient
    private var productDetailsList: List<ProductDetails> = emptyList()
    private var remoteProducts: List<InAppProduct> = emptyList()
    private var retryCount = 0
    private val maxRetry = 3
    private var TAG = "Billing"

    fun setRemoteConfigProducts(products: List<InAppProduct>) {
        this.remoteProducts = products
    }

    fun initializeBilling(callback: BillingCallback) {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        startBillingConnection(callback)
    }

    private fun startBillingConnection(callback: BillingCallback) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    retryCount = 0
                    queryProducts(callback)
                } else {
                    callback.onError("Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.v("Hello", "1")
                if (retryCount < maxRetry) {
                    retryCount++
                    Handler(Looper.getMainLooper()).postDelayed({
                        startBillingConnection(callback)
                    }, 3000)
                } else {
                    callback.onError("Billing service disconnected. Please check your network.")
                }
            }
        })
    }

    private fun queryProducts(callback: BillingCallback) {
        val productList = remoteProducts.map { remoteProduct ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(remoteProduct.productId ?: "")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }

        if (productList.isEmpty()) {
            callback.onError("No products configured")
            return
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList != null) {
                this.productDetailsList = productDetailsList
                callback.onProductsLoaded(productDetailsList)
                Log.v("Hello", "2")
            } else {
                Log.v("Hello", "3")
                val errorMsg = "Failed to query products: ${billingResult.debugMessage}"
                callback.onError(errorMsg)
            }
        }
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        handlePurchase(purchase)
                    }
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "Người dùng đã hủy giao dịch")
            }

            else -> {
                Log.e(TAG, "Lỗi mua hàng: ${billingResult.debugMessage}")
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(params) { result ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Đã xác nhận giao dịch thành công")
                }
            }
        } else {
            //
        }
    }


    fun getProductDetails(productId: String): ProductDetails? {
        return productDetailsList.find { it.productId == productId }
    }

    fun launchBillingFlow(activity: Activity, productDetails: ProductDetails, offerToken: String) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    fun getOfferTokenForProduct(productId: String, productDetails: ProductDetails): String {
        val remoteProduct = remoteProducts.find { it.productId == productId }
        val offerId = remoteProduct?.offerId

        if (!offerId.isNullOrEmpty()) {
            productDetails.subscriptionOfferDetails?.forEach { offer ->
                if (offer.offerId == offerId) {
                    return offer.offerToken
                }
            }
        }

        return when (productId) {
            "test1" -> {
                findPreferredFreeTrialOffer(productDetails) ?: getBasePlanOfferToken(productDetails)
            }

            "free_123" -> getIntroPriceOfferToken(productDetails)
            else -> getBasePlanOfferToken(productDetails)
        }
    }

    private fun findPreferredFreeTrialOffer(productDetails: ProductDetails): String? {
        productDetails.subscriptionOfferDetails?.forEach { offer ->
            val pricingPhases = offer.pricingPhases.pricingPhaseList
            if (pricingPhases.size >= 2) {
                val firstPhase = pricingPhases[0]
                //  val secondPhase = pricingPhases[1]

                if (firstPhase.priceAmountMicros == 0L && firstPhase.billingPeriod == "P2M") {
                    return offer.offerToken
                }
            }
        }
        return null
    }

    fun getFreeTrialOfferToken(productDetails: ProductDetails): String {
        val preferredOffer = findPreferredFreeTrialOffer(productDetails)
        if (preferredOffer != null) return preferredOffer

        productDetails.subscriptionOfferDetails?.forEach { offer ->
            offer.pricingPhases.pricingPhaseList.firstOrNull()?.let { firstPhase ->
                if (firstPhase.priceAmountMicros == 0L) return offer.offerToken
            }
        }

        return getBasePlanOfferToken(productDetails)
    }


    private fun getIntroPriceOfferToken(productDetails: ProductDetails): String {
        productDetails.subscriptionOfferDetails?.forEach { offer ->
            if (offer.offerId == "intro-price") {
                return offer.offerToken
            }
        }

        productDetails.subscriptionOfferDetails?.forEach { offer ->
            offer.pricingPhases.pricingPhaseList.firstOrNull()?.let { firstPhase ->
                if (firstPhase.priceAmountMicros in 1 until 130000000000L) {
                    Log.d(
                        TAG,
                        "Found low price intro offer for ${productDetails.productId}: ${offer.offerId}"
                    )
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
            offer.pricingPhases.pricingPhaseList.firstOrNull()?.let { firstPhase ->
                if (firstPhase.priceAmountMicros > 0L) {
                    return offer.offerToken
                }
            }
        }

        return subscriptionOfferDetails.firstOrNull()?.offerToken ?: ""
    }
}