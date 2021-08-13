rootProject.name = "playground-kotlin-compose-for-web"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform") version "1.5.21"
        id("org.jetbrains.compose") version "0.5.0-build270"
    }
}

include(
    "chess",
    "tic-tac-toe"
)
