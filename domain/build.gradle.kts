plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin(Plugins.KOTLIN_KAPT)
    id(Plugins.HILT_PLUGIN)
}

android {
    namespace = "com.knichu.domain"
    compileSdk = DefaultConfig.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = DefaultConfig.MIN_SDK_VERSION
        targetSdk = DefaultConfig.TARGET_SDK_VERSION
        buildToolsVersion = DefaultConfig.BUILD_TOOLS_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    api("io.reactivex.rxjava3:rxandroid:3.0.0")
    api("io.reactivex.rxjava3:rxjava:3.1.0")

    api("androidx.fragment:fragment-ktx:1.6.1")
    api("androidx.navigation:navigation-fragment-ktx:2.5.3")
    api("androidx.navigation:navigation-ui-ktx:2.5.3")

    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("com.google.dagger:hilt-android:2.44")
}

kapt {
    correctErrorTypes = true
}