plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    kotlin(Plugins.KOTLIN_KAPT)
    id(Plugins.HILT_PLUGIN)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.knichu.suncloud"
    compileSdk = DefaultConfig.COMPILE_SDK_VERSION
    buildToolsVersion = DefaultConfig.BUILD_TOOLS_VERSION

    signingConfigs {
        getByName("debug") {
            keyAlias = getPropertyValue("suncloud.debug.keyAlias")
            keyPassword = getPropertyValue("suncloud.debug.keyPassword")
            storeFile = file("../DebugKeyStore")
            storePassword = getPropertyValue("suncloud.debug.storePassword")
        }

        create("suncloud") {
            keyAlias = getPropertyValue("suncloud.prod.keyAlias")
            keyPassword = getPropertyValue("suncloud.prod.keyPassword")
            storeFile = file("../SuncloudKeyStore")
            storePassword = getPropertyValue("suncloud.prod.storePassword")
        }
    }

    defaultConfig {
        applicationId = DefaultConfig.APPLICATION_ID
        minSdk = DefaultConfig.MIN_SDK_VERSION
        targetSdk = DefaultConfig.TARGET_SDK_VERSION
        versionCode = DefaultConfig.VERSION_CODE
        versionName = DefaultConfig.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

//        // @InstallIn 무시 코드
//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments["dagger.hilt.disableModulesHaveInstallInCheck"] = "true"
//            }
//        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")

            isMinifyEnabled = false
            isShrinkResources = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            signingConfig = signingConfigs.getByName("suncloud")

            isMinifyEnabled = true
            isShrinkResources = true
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
            applicationIdSuffix = ".dev"

            addManifestPlaceholders(
                mapOf(
                    "SCHEME" to "suncloud_dev",
                    "APP_NAME" to "suncloud-dev"
                )
            )
        }
        create("prod") {
            dimension = "version"

            addManifestPlaceholders(
                mapOf(
                    "SCHEME" to "suncloud",
                    "APP_NAME" to "suncloud"
                )
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"

        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.FlowPreview"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    // module
    api(project(":gateway"))
    api(project(":forecast"))
    api(project(":nationwide"))
    api(project(":setting"))
    api(project(":data"))

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    kapt("com.google.dagger:hilt-android-compiler:2.50")
    implementation("com.google.dagger:hilt-android:2.50")

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.0.0")
}

kapt {
    correctErrorTypes = true
}

fun getPropertyValue(propertyKey: String): String {
    return com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir).getProperty(propertyKey)
}