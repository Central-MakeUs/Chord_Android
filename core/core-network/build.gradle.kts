plugins {
    alias(libs.plugins.chord.android.library)
    alias(libs.plugins.chord.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.team.chord.core.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://api.chord.team/api/v1/\"")
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/api/v1/\"")
        }
    }
}

dependencies {
    implementation(projects.core.coreDomain)

    api(libs.retrofit.core)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    api(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.androidx.datastore.preferences)
}
