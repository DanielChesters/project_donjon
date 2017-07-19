import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val gradleVersionsPluginVersion:String by extra
    val kotlinVersion:String by extra
    val sonarqubeVersion:String by extra
    val junitPlatformVersion:String by extra

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
    dependencies {
        classpath("com.github.ben-manes:gradle-versions-plugin:$gradleVersionsPluginVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:$sonarqubeVersion")
        classpath("org.junit.platform:junit-platform-gradle-plugin:$junitPlatformVersion")
    }
}

subprojects {
    apply {
        plugin("com.github.ben-manes.versions")
        plugin("kotlin")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    version = "0.1"
    ext {
        appName = "Projet-donjon"
    }

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
    }
}

