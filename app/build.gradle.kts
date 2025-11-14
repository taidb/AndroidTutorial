plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs")
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.eco.musicplayer.audioplayer.music"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.eco.musicplayer.audioplayer.music"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.crashlytics)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Đã cập nhật phiên bản
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0") // Đã cập nhật phiên bản
    implementation("androidx.viewpager2:viewpager2:1.0.0") // Đã sửa phiên bản
    implementation("com.airbnb.android:lottie:6.1.0") // Đã cập nhật phiên bản
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0")) // Đã điều chỉnh phiên bản
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.8")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.fragment:fragment-ktx:1.8.2")
    implementation("org.greenrobot:eventbus:3.3.1")

    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation("com.google.android.gms:play-services-ads:23.0.0")
    implementation("com.ecomobileapp:ads-sdk:3.0.8")

    // Material design (chỉ cần 1 lần)
    implementation("com.android.billingclient:billing:7.1.1")
    implementation("com.android.billingclient:billing-ktx:7.1.1")
    implementation("com.mikhaellopez:circularprogressbar:3.1.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")

    //sự dụng koin
    implementation("io.insert-koin:koin-core:3.5.6")

    implementation("io.insert-koin:koin-android:3.5.6")


}