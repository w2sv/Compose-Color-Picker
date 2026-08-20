import nl.littlerobots.vcu.plugin.resolver.VersionSelectors

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.versionCatalogUpdate)
}

// Enfore kgp version
buildscript { dependencies { classpath(libs.kotlin.gradle.plugin) } }

versionCatalogUpdate {
    versionSelector(VersionSelectors.PREFER_STABLE)
}

rootProject.file("version.txt")
    .takeIf { it.exists() }
    ?.readText()
    ?.trim()
    ?.let { version = it }
