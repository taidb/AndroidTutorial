package com.example.androidtutorial.activity.util


interface BottomSheetListener {
    fun onPlanSelected(plan: Int)
    fun onContinueClicked(plan: Int)
    fun onBottomSheetClosed()
}