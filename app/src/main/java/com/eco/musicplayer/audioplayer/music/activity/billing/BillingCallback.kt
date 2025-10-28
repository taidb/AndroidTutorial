package com.eco.musicplayer.audioplayer.music.activity.billing

import com.android.billingclient.api.ProductDetails

interface BillingCallback {
    fun onProductsLoaded(products: List<ProductDetails>)
    fun onError(error: String)
}