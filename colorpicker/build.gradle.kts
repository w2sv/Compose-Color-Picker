plugins {
    id("w2sv.android-library")
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    buildFeatures {
        compose = true
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.w2sv"
            artifactId = "colorpicker"
            version = version.toString()
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.stf.compose.extended.gestures)
    implementation(libs.stf.compose.extended.colors)
    implementation(libs.stf.compose.colorful.sliders)
    implementation(libs.w2sv.composed.core)
}
