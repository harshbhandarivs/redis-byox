/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("buildlogic.java-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")

    // Logging dependencies
    implementation("org.slf4j:slf4j-api:2.0.13")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.6")
    runtimeOnly("ch.qos.logback:logback-core:1.5.6")

}

application {
    // Define the main class for the application.
    mainClass = "com.harsh.redis.app.App"
}