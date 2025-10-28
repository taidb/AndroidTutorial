package com.eco.musicplayer.audioplayer.music.activity.util


interface BottomSheetListener {
    fun onPlanSelected(plan: Int)
    fun onContinueClicked(plan: Int)
    fun onBottomSheetClosed()
}