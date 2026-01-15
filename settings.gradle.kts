pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Chord"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:core-domain")
include(":core:core-data")
include(":core:core-common")
include(":core:core-ui")
include(":feature:feature-home")
include(":feature:feature-onboarding")
include(":feature:feature-auth")
include(":feature:feature-setup")
include(":feature:feature-menu")
include(":feature:feature-ingredient")
