plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
//    id("com.google.devtools.ksp")
//    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
//    id("com.google.devtools.ksp") version "2.1.10-1.0.29"
//    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

android {
    namespace = "com.example.newsapp_api"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.newsapp_api"
        minSdk = 26
        targetSdk = 34
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

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.android.async.http)
    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("com.facebook.shimmer:shimmer:0.5.0")

//    // Room dependencies
//    implementation("androidx.room:room-runtime:2.6.1") // Use the latest version
//    annotationProcessor("androidx.room:room-compiler:2.6.1") // Use the latest version
//    ksp("androidx.room:room-compiler:2.6.1") //Use the latest version
//    implementation("androidx.room:room-ktx:2.6.1") // Kotlin extensions for Room
//
//    implementation(libs.androidx.lifecycle.viewmodel.ktx)
//    implementation(libs.lifecycle.livedata.ktx)
//    implementation(libs.androidx.room.runtime)
//    ksp(libs.room.compiler)

    val lifecycle_version = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

//    val room_version = "2.6.1"
//    implementation("androidx.room:room-runtime:$room_version")
//    ksp("androidx.room:room-compiler:$room_version")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}