pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://repository.map.naver.com/archive/maven") }
    }
}
rootProject.name = "Suncloud"
include(":app")
include(":forecast")
include(":nationwide")
include(":setting")
include(":common")
include(":common-ui")
include(":domain")
include(":data")
include(":gateway")
