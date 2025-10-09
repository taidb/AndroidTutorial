package com.example.androidtutorial.activity

import java.io.Serializable

data class Student(val name:String ,val age:Int,val address:String) : Serializable {
}