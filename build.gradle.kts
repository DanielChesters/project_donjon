import io.gitlab.arturbosch.detekt.detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val sonarqubeVersion: String by project
    val kotlinVersion: String by project
    val gradleVersionsPluginVersion: String by project

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    }

    dependencies {
        classpath("com.github.ben-manes:gradle-versions-plugin:$gradleVersionsPluginVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:$sonarqubeVersion")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.16.0")
    }
}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    version = "0.1"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        google()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://oss.sonatype.org/content/repositories/releases/")
    }

    detekt {
        ignoreFailures = true
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        this.jvmTarget = "11"
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
