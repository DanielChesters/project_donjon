val junitJupiterVersion = "5.7.2"
val ktxVersion = "1.10.0-b1"
val gdxdialogsVersion = "1.3.0"
val ashleyVersion = "1.7.4"
val gdxVersion = "1.10.0"
val box2dlightsVersion = "1.5"
val gdxControllersVersion = "2.2.0"

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

jacoco {
    toolVersion = "0.8.7"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
    }
}
