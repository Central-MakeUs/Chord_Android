import java.util.Properties

plugins {
    alias(libs.plugins.chord.android.library)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

fun localProperty(key: String): String =
    localProperties.getProperty(key).orEmpty()

fun localBooleanProperty(
    key: String,
    defaultValue: Boolean,
): Boolean =
    localProperties.getProperty(key)?.toBooleanStrictOrNull() ?: defaultValue

android {
    namespace = "com.team.chord.core.analytics"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "MIXPANEL_PROJECT_TOKEN", "\"${localProperty("MIXPANEL_PROJECT_TOKEN")}\"")
        buildConfigField("boolean", "MIXPANEL_ENABLED", localBooleanProperty("MIXPANEL_ENABLED", true).toString())
        buildConfigField("boolean", "MIXPANEL_LOGGING_ENABLED", localBooleanProperty("MIXPANEL_LOGGING_ENABLED", false).toString())
    }
}

dependencies {
    implementation(libs.mixpanel.android)

    testImplementation(libs.junit)
}
