val junitJupiterVersion: String by project
val ktxVersion: String by project
val gdxdialogsVersion: String by project
val ashleyVersion: String by project
val gdxVersion: String by project
val box2dlightsVersion: String by project
val gdxControllersVersion: String by project

plugins {
    id("org.sonarqube")
    id("jacoco")
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
    implementation("com.badlogicgames.box2dlights:box2dlights:$box2dlightsVersion")
    implementation("com.badlogicgames.gdx-controllers:gdx-controllers-core:$gdxControllersVersion")
    implementation("com.badlogicgames.ashley:ashley:$ashleyVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("de.tomgrill.gdxdialogs:gdx-dialogs-core:$gdxdialogsVersion")
    api("io.github.libktx:ktx-app:$ktxVersion")
    implementation("io.github.libktx:ktx-scene2d:$ktxVersion")
    implementation("io.github.libktx:ktx-inject:$ktxVersion")
    implementation("io.github.libktx:ktx-log:$ktxVersion")
    implementation("io.github.libktx:ktx-collections:$ktxVersion")
    implementation("io.github.libktx:ktx-box2d:$ktxVersion")
    implementation("io.github.libktx:ktx-math:$ktxVersion")
    implementation("io.github.libktx:ktx-ashley:$ktxVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "danielchesters-github")
        property("sonar.projectKey", "DanielChesters_project_donjon")
    }
}
