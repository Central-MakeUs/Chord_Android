plugins {
    alias(libs.plugins.chord.android.feature)
}

android {
    namespace = "com.team.chord.feature.setup"
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
}
