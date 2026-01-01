plugins {
    alias(libs.plugins.chord.android.application)
    alias(libs.plugins.chord.android.compose)
    alias(libs.plugins.chord.android.hilt)
}

android {
    namespace = "com.team.chord"

    defaultConfig {
        applicationId = "com.team.chord"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.core.coreData)
    implementation(projects.core.coreCommon)
    implementation(projects.core.coreUi)
    implementation(projects.feature.featureHome)
    implementation(projects.feature.featureOnboarding)
    implementation(projects.feature.featureAuth)
    implementation(projects.feature.featureSetup)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
