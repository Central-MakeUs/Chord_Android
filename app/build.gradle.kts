import org.gradle.api.GradleException
import java.util.Properties

plugins {
    alias(libs.plugins.chord.android.application)
    alias(libs.plugins.chord.android.compose)
    alias(libs.plugins.chord.android.hilt)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

fun localProperty(key: String): String? =
    localProperties.getProperty(key)?.takeIf { it.isNotBlank() }

val requiredReleaseSigningProperties = listOf(
    "RELEASE_STORE_FILE",
    "RELEASE_STORE_PASSWORD",
    "RELEASE_KEY_ALIAS",
    "RELEASE_KEY_PASSWORD",
)

val hasReleaseSigning = requiredReleaseSigningProperties.all { localProperty(it) != null }

val releaseSigningTaskNames = listOf(
    "assembleRelease",
    "bundleRelease",
    "packageRelease",
    "publish",
)

val isSignedReleaseArtifactTaskRequested = gradle.startParameter.taskNames.any { taskName ->
    val normalizedTaskName = taskName.substringAfterLast(':')
    releaseSigningTaskNames.any { normalizedTaskName.contains(it, ignoreCase = true) }
}

if (isSignedReleaseArtifactTaskRequested && !hasReleaseSigning) {
    throw GradleException(
        "Release signing properties are missing. Add RELEASE_STORE_FILE, RELEASE_STORE_PASSWORD, " +
            "RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD to local.properties.",
    )
}

android {
    namespace = "com.team.chord"

    signingConfigs {
        create("release") {
            if (hasReleaseSigning) {
                storeFile = rootProject.file(localProperty("RELEASE_STORE_FILE")!!)
                storePassword = localProperty("RELEASE_STORE_PASSWORD")
                keyAlias = localProperty("RELEASE_KEY_ALIAS")
                keyPassword = localProperty("RELEASE_KEY_PASSWORD")
            }
        }
    }

    defaultConfig {
        applicationId = "com.team.chord"
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = true
            isShrinkResources = true
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
    implementation(projects.core.coreNetwork)
    implementation(projects.feature.featureHome)
    implementation(projects.feature.featureSetting)

    implementation(projects.feature.featureAuth)
    implementation(projects.feature.featureSetup)
    implementation(projects.feature.featureMenu)
    implementation(projects.feature.featureIngredient)
    implementation(projects.feature.featureAicoach)

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
