import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.team.chord.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "chord.android.application"
            implementationClass = "com.team.chord.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "chord.android.library"
            implementationClass = "com.team.chord.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "chord.android.feature"
            implementationClass = "com.team.chord.AndroidFeatureConventionPlugin"
        }
        register("androidCompose") {
            id = "chord.android.compose"
            implementationClass = "com.team.chord.AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "chord.android.hilt"
            implementationClass = "com.team.chord.AndroidHiltConventionPlugin"
        }
        register("jvmLibrary") {
            id = "chord.jvm.library"
            implementationClass = "com.team.chord.JvmLibraryConventionPlugin"
        }
    }
}
