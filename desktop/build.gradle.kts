val gdxdialogsVersion: String by project
val gdxVersion: String by project
val gdxControllersVersion: String by project

plugins {
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

application {
    mainClass.set("com.oni.donjon.desktop.DesktopLauncher")
    applicationName = "Projet-donjon"
}

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx-controllers:gdx-controllers-desktop:$gdxControllersVersion")
    implementation("de.tomgrill.gdxdialogs:gdx-dialogs-desktop:$gdxdialogsVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}
