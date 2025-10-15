package com.example.androidtutorial.activity.model


data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val time: String = "",
    val isFixed: Boolean = false
)
