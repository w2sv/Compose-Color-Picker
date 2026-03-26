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
    implementation(libs.androidx.core)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.material)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.compose.extended.gestures)
    implementation(libs.compose.extended.colors)
    implementation(libs.compose.colorful.sliders)
}
