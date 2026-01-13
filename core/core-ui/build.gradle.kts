plugins {
    alias(libs.plugins.chord.android.library)
    alias(libs.plugins.chord.android.compose)
}

android {
    namespace = "com.team.chord.core.ui"
}

dependencies {
    implementation(projects.core.coreCommon)
    implementation(projects.core.coreDomain)
}
