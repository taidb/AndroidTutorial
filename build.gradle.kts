// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {

        //    classpath ("com.google.dagger:hilt-android-gradle-plugin:$hilt_version")
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0") // Kiểm tra phiên bản Kotlin phù hợp


    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    kotlin("kapt") version "2.0.0"
    id("com.google.gms.google-services") version "4.4.4" apply false

}