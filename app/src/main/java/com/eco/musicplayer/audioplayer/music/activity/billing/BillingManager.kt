package com.eco.musicplayer.audioplayer.music.activity.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.eco.musicplayer.audioplayer.music.activity.remoteconfig.InAppProduct


class BillingManager(private val context: Context) {

    private lateinit var billingClient: BillingClient
    private var productDetailsList: List<ProductDetails> = emptyList()
    private var remoteProducts :List<InAppProduct> = emptyList()

    fun setRemoteConfigProducts(products:List<InAppProduct>){
        this.remoteProducts=products
    }

    fun initializeBilling(callback: BillingCallback) {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener) //nhận callback khi người dùng mua hàng hoặc hủy thanh toán purchasesUpdatedListener nhận kết quả trả về Google Play.
            .enablePendingPurchases()  //thực hiện giao dịch chưa hoàn thành
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProducts(callback)
                } else {
                    callback.onError("Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
            }
        })
    }


    private fun queryProducts(callback: BillingCallback) {
//        val productList = listOf(
//            QueryProductDetailsParams.Product.newBuilder()
//                .setProductId("free_123")
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build(),
//            QueryProductDetailsParams.Product.newBuilder()
//                .setProductId("test1")
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build(),
//            QueryProductDetailsParams.Product.newBuilder()
//                .setProductId("test2")
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build(),
//            QueryProductDetailsParams.Product.newBuilder()
//                .setProductId("test3")
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build()
//        )
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
                productDetailsList.forEach { product ->
                    product.subscriptionOfferDetails?.forEach { offer ->
                        offer.pricingPhases.pricingPhaseList.forEachIndexed { _, _ ->
                        }
                    }
                }
                callback.onProductsLoaded(productDetailsList)
            } else {
                val errorMsg = "Failed to query products: ${billingResult.debugMessage}"
                callback.onError(errorMsg)
            }
        }
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { _, _ ->

    }

    fun getProductDetails(productId: String): ProductDetails? {
        return productDetailsList.find { it.productId == productId }
    }

    //hàm mở mặc định giao diện
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

        // Fallback
        return getBasePlanOfferToken(productDetails)
    }
     fun getBasePlanOfferToken(productDetails: ProductDetails): String {
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
}
