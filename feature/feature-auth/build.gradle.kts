plugins {
    alias(libs.plugins.chord.android.feature)
}

android {
    namespace = "com.team.chord.feature.auth"
}

dependencies {
    implementation(projects.core.coreData)
    implementation(libs.androidx.compose.material.icons.extended)
}
