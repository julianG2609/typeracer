## Requirements
This project uses java 21.

## Dependencies
* junit-jupiter:5.9.1
* :spotbugs-annotations:4.8.6
* javafx 0.1.0
* spotbugs 6.0.16
* spotless 7.0.0.BETA1
* gson:2.11.0
* jlama-core:0.2.1
* jlama-native:0.2.1

## To start a server:
* `./gradlew :serverUI:run`
## To connect a player:
* `./gradlew :gameUI:run`
## To run headless server:
* `./gradlew :serverui:run --args="headless"`
