plugins {
    alias(libs.plugins.chord.android.feature)
}

android {
    namespace = "com.team.chord.feature.setting"
}

dependencies {
    implementation(projects.core.coreData)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
