import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val sonarqubeVersion: String by project
    val kotlinVersion: String by project
    val gradleVersionsPluginVersion: String by project

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven (url = "https://oss.sonatype.org/content/repositories/snapshots/")
    }

    dependencies {
        classpath("com.github.ben-manes:gradle-versions-plugin:$gradleVersionsPluginVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:$sonarqubeVersion")
    }
}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.ben-manes.versions")

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    version = "0.1"
    /*ext {
        appName = "Projet-donjon"
    }*/

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://oss.sonatype.org/content/repositories/releases/")
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}


