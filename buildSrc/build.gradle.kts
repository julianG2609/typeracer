plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.openjfx:javafx-plugin:0.1.0")
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:6.0.16")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:7.0.0.BETA1")
}