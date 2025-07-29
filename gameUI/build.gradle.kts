plugins {
    id("javafx")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass = "Main"
}

dependencies {
    implementation(project(":game"))
    implementation(project(":networking:shared"))
    implementation(project(":data"))
}