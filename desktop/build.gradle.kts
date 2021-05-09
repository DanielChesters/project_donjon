val gdxdialogsVersion = "1.3.0"
val gdxVersion = "1.10.0"
val gdxControllersVersion = "2.2.0"

plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

application {
    mainClass.set("com.oni.donjon.desktop.DesktopLauncher")
//    mainClassName = "com.oni.donjon.desktop.DesktopLauncher"
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
