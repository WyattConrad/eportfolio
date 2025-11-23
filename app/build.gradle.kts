plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("androidx.room")
}

android {
    namespace = "com.wyattconrad.cs_360weighttracker"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.wyattconrad.cs_360weighttracker"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"



    }

    packaging {
        resources {
            // Use pickFirsts for the new conflicting file
            pickFirsts += "META-INF/LICENSE-notice.md"

            // Retain fix for the previous LICENSE.md file
            pickFirsts += "META-INF/LICENSE.md"

            // Common exclusions often needed for JUnit/testing libraries
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
            excludes += "**/*.SF"
            excludes += "**/*.DSA"
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.foundation)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.compose.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.ui.test.junit4)
    val roomVersion = "2.8.4"

    implementation(libs.legacy.support.v4)
    implementation(libs.preference)

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation("at.favre.lib:bcrypt:0.10.2")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.57.2")
    kapt("com.google.dagger:hilt-compiler:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

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
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")

    // Chart
    implementation ("io.github.ehsannarmani:compose-charts:0.2.0")

    // For unit testing with JUnit
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    testImplementation(libs.core.testing)
    testImplementation(libs.awaitility)

    val mockkVersion = "1.14.6"

    // For unit testing with Mockito
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

    // Optional: For Android-specific integration
    androidTestImplementation("io.mockk:mockk-android:${mockkVersion}")
    androidTestImplementation("io.mockk:mockk-agent:${mockkVersion}")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")
    androidTestImplementation("com.google.truth:truth:1.1.5")

    // Test rules and transitive dependencies:
    androidTestImplementation(libs.androidx.ui.test.junit4)
// Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    debugImplementation(libs.androidx.compose.ui.tooling)
}

kapt {
    correctErrorTypes = true
}