package com.eco.musicplayer.audioplayer.music.activity.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*


class BillingManager(private val context: Context) {

    private lateinit var billingClient: BillingClient
    private var productDetailsList: List<ProductDetails> = emptyList()

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
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("free_123")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("test1")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("test2")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("test3")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()


        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList != null) {
                this.productDetailsList = productDetailsList
                productDetailsList.forEach { product ->
                    product.subscriptionOfferDetails?.forEach { offer ->
                        offer.pricingPhases.pricingPhaseList.forEachIndexed { index, phase ->
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

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->

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
}
