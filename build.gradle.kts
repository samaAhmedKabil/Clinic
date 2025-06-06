// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}
buildscript{
    repositories{
        google()
        mavenCentral()
    }
    dependencies{
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
        classpath ("com.google.gms:google-services:4.3.15")
    }
}