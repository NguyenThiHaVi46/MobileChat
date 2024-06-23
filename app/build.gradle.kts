plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.mychat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mychat"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.emoji2.views)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.google.services)
    implementation(libs.firebaseui)
    implementation(libs.generativeai)
    implementation(libs.guava)
    implementation(libs.reactive.streams)
    implementation(libs.libphonenumber)
    implementation(libs.glide)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.storage)
    implementation("com.hbb20:ccp:2.5.0")

//    implementation(libs.firebase.bom)

    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.mindrot:jbcrypt:0.4")

    implementation (platform("com.google.firebase:firebase-bom:33.1.0"))
    //noinspection UseTomlInstead
    implementation ("com.google.firebase:firebase-auth")
    implementation ("androidx.emoji2:emoji2:1.3.0")
    implementation ("androidx.emoji2:emoji2-bundled:1.2.0")



}