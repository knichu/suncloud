// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.buildGradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
    }
}

plugins {
    id(Plugins.ANDROID_APPLICATION) version "7.3.1" apply false
    id(Plugins.ANDROID_LIBRARY) version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id(Plugins.HILT_PROJECT_PLUGIN) version "2.50" apply false
}

//// 프로젝트를 초기화하거나 리셋해야 할 때
//tasks.register("clean", Delete::class) {
//    delete(rootProject.buildDir)
//}