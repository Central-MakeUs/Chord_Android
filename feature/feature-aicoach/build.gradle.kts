plugins {
    alias(libs.plugins.chord.android.feature)
}

android {
    namespace = "com.team.chord.feature.aicoach"
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
