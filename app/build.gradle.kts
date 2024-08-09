import org.bouncycastle.util.Properties.getPropertyValue

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("androidx.navigation.safeargs.kotlin")
    id ("kotlin-parcelize")

}

android {
    namespace = "com.adstek"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.adstek"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true // Enable custom BuildConfig fields
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    flavorDimensions("adsTek")
    productFlavors {
        create("sandbox") {
            dimension = "adsTek"
            applicationId = "com.adstek.sandbox"
            resValue("string", "app_name", "AdsTek Sandbox")
            signingConfig = signingConfigs.getByName("debug")

            buildConfigField("String", resourceName("baseUrl"), getPropertyValue("stagingBaseUrl"))
        }
        create("production") {
            dimension = "adsTek"
            applicationId = "com.adstek"
            resValue("string", "app_name", "AdsTek")
            signingConfig = signingConfigs.getByName("debug")

            buildConfigField("String", resourceName("baseUrl"), getPropertyValue("productionBaseUrl"))
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.camera:camera-view:1.3.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // Room components
    implementation("androidx.room:room-runtime:2.5.0")
    kapt("androidx.room:room-compiler:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0")
    androidTestImplementation("androidx.room:room-testing:2.5.0")

// Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:\$rootProject.lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:\$rootProject.lifecycleVersion")

// Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")


// Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:okhttp")


    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")


    //Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")

    //Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    kapt ("android.arch.lifecycle:compiler:1.1.1")


    implementation("com.github.aabhasr1:OtpView:1.1.2")

    implementation ("com.github.dhaval2404:imagepicker:2.1")


    //glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    kapt ("com.github.bumptech.glide:compiler:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")


    implementation ("androidx.media3:media3-exoplayer:1.0.0")
    implementation ("androidx.media3:media3-ui:1.0.0")
    implementation ("androidx.media3:media3-exoplayer-hls:1.0.0")
    implementation ("androidx.media3:media3-session:1.0.0")

    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")


}


fun Project.getPropertyValue(propertyName: String): String {
    return properties[propertyName].toString()
}

fun resourceName(value: String): String {
    val regex = Regex("([a-z])([A-Z]+)")
    val replacement = "$1_$2"
    var result = value.replace(regex, replacement).toUpperCase()
    result = "GRADLE_$result"
    return result
}

