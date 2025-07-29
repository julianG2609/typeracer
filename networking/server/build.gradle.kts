plugins {
    id("java-lib")
}

var arch = if(System.getProperty("os.arch") == "amd64") "x86_64" else "aarch_64"
var os = if(System.getProperty("os.name").lowercase().startsWith("mac")) "osx" else System.getProperty("os.name").lowercase()

dependencies {
    implementation(project(":networking:shared"))
    implementation(project(":game"))
    implementation(project(":data"))
    implementation("com.github.tjake:jlama-core:0.2.1")
    implementation("com.github.tjake:jlama-native:0.2.1:${os}-"+arch)
}