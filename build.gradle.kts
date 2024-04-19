plugins {
    id("org.sonarqube") version app.web.chechkysh.buildsrc.Depends.Versions.sonarqubeVersion
    detekt
    id("com.github.ben-manes.versions") version app.web.chechkysh.buildsrc.Depends.Versions.checkDependencyVersionsVersion
    id("com.osacky.doctor") version app.web.chechkysh.buildsrc.Depends.Versions.gradleDoctorVersion
    id("com.autonomousapps.dependency-analysis") version app.web.chechkysh.buildsrc.Depends.Versions.dependencyAnalysisVersion
    id("org.sonatype.gradle.plugins.scan") version app.web.chechkysh.buildsrc.Depends.Versions.sonatypeScanGradleVersion
    id("org.jetbrains.kotlin.plugin.serialization") version app.web.chechkysh.buildsrc.Depends.Versions.kotlinVersion

}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(app.web.chechkysh.buildsrc.Depends.ClassPaths.gradle)
        classpath(
            kotlin(
                app.web.chechkysh.buildsrc.Depends.ClassPaths.kotlin_gradle_plugin,
                version = app.web.chechkysh.buildsrc.Depends.Versions.kotlinVersion
            )
        )
        classpath(app.web.chechkysh.buildsrc.Depends.ClassPaths.navigation_safe_args_gradle_plugin)
        classpath(app.web.chechkysh.buildsrc.Depends.ClassPaths.hilt_android_gradle_plugin)
        classpath(app.web.chechkysh.buildsrc.Depends.ClassPaths.sonarqube_gradle_plugin)
    }
}

configure<com.osacky.doctor.DoctorExtension> {
    disallowMultipleDaemons.set(false)
    negativeAvoidanceThreshold.set(500)
    warnWhenJetifierEnabled.set(true)

    javaHome {
        ensureJavaHomeIsSet.set(true)
        ensureJavaHomeMatches.set(true)
        failOnError.set(false)
    }
}
