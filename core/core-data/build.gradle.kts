plugins {
    alias(libs.plugins.chord.android.library)
    alias(libs.plugins.chord.android.hilt)
}

android {
    namespace = "com.team.chord.core.data"
}

dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.core.coreCommon)
}
