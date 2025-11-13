plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.wyattconrad.cs_360weighttracker"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.wyattconrad.cs_360weighttracker"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
    }

}

dependencies {
    implementation(libs.core.ktx)
    val room_version = "2.8.3"

    implementation(libs.legacy.support.v4)
    implementation(libs.preference)


    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.runtime)
    androidTestImplementation(composeBom)

    // For unit testing with JUnit
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    testImplementation(libs.core.testing)
    testImplementation(libs.awaitility)

    // For unit testing with Mockito
    testImplementation(libs.mockito.core)

    // Optional: For Android-specific integration
    androidTestImplementation(libs.mockito.android)
}