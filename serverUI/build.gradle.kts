plugins {
    id("javafx")
    id("java-app")
}
dependencies {
    implementation(project(":networking:server"))
}

application{
    mainClass = "Main"
}