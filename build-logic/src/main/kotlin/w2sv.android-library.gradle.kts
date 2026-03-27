plugins {
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

// Compile unit test code with JVM 11 to fix inconsistency
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xannotation-default-target=param-property",
            "-Xcontext-sensitive-resolution",
            "-Xcontext-parameters"
        )
        optIn.addAll(
            "kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi",
            "kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }
}

android {
    namespace = "com.w2sv.${path.removePrefix(":").replace(':', '.')}"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes { getByName("release") { isMinifyEnabled = false } }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures { buildConfig = false }

    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all { test -> test.failOnNoDiscoveredTests = false }
        }
    }
}
