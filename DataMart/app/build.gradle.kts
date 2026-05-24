plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.datamart"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.datamart"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Mesin Retrofit untuk menarik data API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Mesin Gson untuk mengubah teks JSON menjadi Java
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Mesin Glide untuk memuat gambar dari URL (dibutuhkan untuk daftar produk nanti)
    implementation("com.github.bumptech.glide:glide:4.16.0")
}