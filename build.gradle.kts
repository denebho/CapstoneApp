buildscript {
    repositories {
        mavenCentral()
        maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath ("com.android.tools.build:gradle:7.1.0-alpha11")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}