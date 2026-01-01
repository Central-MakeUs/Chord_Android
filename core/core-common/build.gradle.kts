plugins {
    alias(libs.plugins.chord.android.library)
    alias(libs.plugins.chord.android.hilt)
}

android {
    namespace = "com.team.chord.core.common"
}

dependencies {
    implementation(projects.core.coreDomain)
}
