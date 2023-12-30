// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.1.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
    }
}

plugins {
    id(Plugins.ANDROID_APPLICATION) version "7.3.1" apply false
    id(Plugins.ANDROID_LIBRARY) version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
    id(Plugins.HILT_PROJECT_PLUGIN) version "2.44" apply false
}

//// 프로젝트를 초기화하거나 리셋해야 할 때
//tasks.register("clean", Delete::class) {
//    delete(rootProject.buildDir)
//}