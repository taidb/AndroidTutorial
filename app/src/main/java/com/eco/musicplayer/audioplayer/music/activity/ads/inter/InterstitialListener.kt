package com.eco.musicplayer.audioplayer.music.activity.ads.inter


abstract class InterstitialListener {
    open fun onAdFailToShow(error: String) {}
    open fun onAdShowedFullScreen() {}
    open fun onAdDismissedFullScreen() {}
    open fun onEcoRemoveAllAdsClicked() {}
    open fun onEcoFullScreenAdsResume() {}
}