plugins {
    alias(libs.plugins.chord.android.feature)
}

android {
    namespace = "com.team.chord.feature.menu"
}

dependencies {
    implementation(projects.feature.featureMenuaddShared)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
