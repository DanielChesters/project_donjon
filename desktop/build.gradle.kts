import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val gdxdialogsVersion: String by project
val gdxVersion: String by project

plugins {
    application
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

application {
    mainClassName = "com.oni.donjon.desktop.DesktopLauncher"
    applicationName = "Projet-donjon"
}

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-controllers-lwjgl3:$gdxVersion")
    implementation("de.tomgrill.gdxdialogs:gdx-dialogs-desktop:$gdxdialogsVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
}
