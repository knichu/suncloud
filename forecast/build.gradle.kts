plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin(Plugins.KOTLIN_KAPT)
    id(Plugins.HILT_PLUGIN)
}

android {
    namespace = "com.knichu.forecast"
    compileSdk = DefaultConfig.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = DefaultConfig.MIN_SDK_VERSION
        buildToolsVersion = DefaultConfig.BUILD_TOOLS_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions.add("version")
    productFlavors {
        create("dev") {
            dimension = "version"
        }
        create("prod") {
            dimension = "version"
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
}

dependencies {

    // module
    api(project(":common"))
    api(project(":common-ui"))

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    kapt("com.google.dagger:hilt-android-compiler:2.50")
    implementation("com.google.dagger:hilt-android:2.50")

    // location service
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // compose
    implementation("androidx.navigation:navigation-compose:2.8.4")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")

    // ConstraintLayout for Compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")
}

kapt {
    correctErrorTypes = true
}