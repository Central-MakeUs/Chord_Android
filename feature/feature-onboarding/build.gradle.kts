plugins {
    alias(libs.plugins.chord.android.feature)
}

android {
    namespace = "com.team.chord.feature.onboarding"
}

dependencies {
    implementation(projects.core.coreData)
}
