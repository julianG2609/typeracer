plugins{
    application
    id("java-general")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    enableAssertions = true
}

application{
    applicationDefaultJvmArgs = listOf("--enable-preview", "--add-modules", "jdk.incubator.vector")
}