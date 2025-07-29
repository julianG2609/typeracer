plugins {
    id("java-app")
    id("org.openjfx.javafxplugin")
}

repositories {
    mavenCentral()
}

javafx {
    version = "22.0.1"
    modules("javafx.controls", "javafx.fxml", "javafx.media", "javafx.graphics")
}