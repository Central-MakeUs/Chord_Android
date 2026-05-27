import java.util.Properties

plugins {
    alias(libs.plugins.chord.android.feature)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

fun localProperty(key: String): String =
    localProperties.getProperty(key).orEmpty()

android {
    namespace = "com.team.chord.feature.auth"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${localProperty("KAKAO_NATIVE_APP_KEY")}\"")
        buildConfigField("String", "NAVER_CLIENT_ID", "\"${localProperty("NAVER_CLIENT_ID")}\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"${localProperty("NAVER_CLIENT_SECRET")}\"")
        buildConfigField("String", "NAVER_CLIENT_NAME", "\"${localProperty("NAVER_CLIENT_NAME").ifBlank { "카페코치" }}\"")
    }
}

dependencies {
    implementation(projects.core.coreData)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.kakao.user)
    implementation(libs.naver.oauth)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
