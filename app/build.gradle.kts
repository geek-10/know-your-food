plugins {
    id("com.android.application")

    // Google services gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.knowyourfood"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.knowyourfood"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Firebase SDK
    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Realtime Database Dependency
    implementation("com.google.firebase:firebase-database")

    // Authentication Dependency
    implementation("com.google.firebase:firebase-auth")

    // Adding Google Sign-In Dependency
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Lottie Animations Dependency
    implementation("com.airbnb.android:lottie:6.1.0")


}