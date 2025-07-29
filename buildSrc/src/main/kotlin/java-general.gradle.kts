import gradle.kotlin.dsl.accessors._31b737e57e3fa3a2ffab294b9568a8c9.compileOnly
import gradle.kotlin.dsl.accessors._31b737e57e3fa3a2ffab294b9568a8c9.spotbugs

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    java
    id("com.github.spotbugs")
    id("com.diffplug.spotless")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    "testImplementation"("org.junit.jupiter:junit-jupiter:5.9.1")
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.withType<JavaCompile>(){
    options.compilerArgs.addAll(listOf("--enable-preview", "--add-modules", "jdk.incubator.vector"))
}

tasks.withType<JavaExec>(){
    args.addAll(listOf("--enable-preview", "--add-modules", "jdk.incubator.vector"))
}

spotless{
    java{
        googleJavaFormat("1.22.0").reflowLongStrings()
    }
}

tasks.withType(com.github.spotbugs.snom.SpotBugsTask::class.java ){
    reports.create("html")
}

tasks {
    javadoc {
        options {
            (this as CoreJavadocOptions).addBooleanOption("Werror", true)
        }
    }
}
