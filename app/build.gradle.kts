import app.web.chechkysh.buildsrc.Depends
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {

    compileSdk = Depends.Versions.androidCompileSdkVersion

    defaultConfig {
        multiDexEnabled = true
        applicationId = "app.web.chechkysh.mvimodularizationtemplate"
        minSdk = Depends.Versions.minSdkVersion
        targetSdk = Depends.Versions.targetSdkVersion
        versionCode = Depends.Versions.appVersionCode
        versionName = Depends.generateVersionName()
        testInstrumentationRunner =
            Depends.Versions.testInstrumentationRunner
        javaCompileOptions.annotationProcessorOptions.arguments += mapOf(
            "room.schemaLocation" to "$projectDir/schemas"
        )
    }
    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }
    buildTypes {
        named("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            signingConfig = signingConfigs.getByName("debug")
        }
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    lint {
        abortOnError = false
    }
    //testOptions.unitTests.returnDefaultValues = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = freeCompilerArgs + listOf("-XXLanguage:+InlineClasses")
    }
}

dependencies {
    implementation(Depends.Libraries.kotlin)
    implementation(Depends.Libraries.android_core_ktx)
    implementation(Depends.Libraries.multidex)
    implementation(Depends.Libraries.fragment_ktx)
    implementation(Depends.Libraries.paging_runtime_ktx)
    implementation(Depends.Libraries.paging_rx)
    implementation(Depends.Libraries.dataStore_preferences)
    //dependency injection
    implementation(Depends.Libraries.hilt_android)
    kapt(Depends.Libraries.hilt_android_compiler)
    kapt(Depends.Libraries.hilt_compiler)
    implementation(Depends.Libraries.java_inject)
    //network
    implementation(Depends.Libraries.apollo_graphql_runtime)
    implementation(Depends.Libraries.apollo_normalized_cache)
    debugImplementation(Depends.Libraries.chucker)
    releaseImplementation(Depends.Libraries.chucker_no_op)
    //other
    implementation(Depends.Libraries.timber)
    implementation(Depends.Libraries.arrow)
    debugImplementation(Depends.Libraries.leak_canary)
    //test
    testImplementation(Depends.Libraries.junit)
    androidTestImplementation(Depends.Libraries.test_ext_junit)
    androidTestImplementation(Depends.Libraries.test_runner)
    androidTestImplementation(Depends.Libraries.espresso_core)
    api("com.theartofdev.edmodo:android-image-cropper:2.8.+")

    implementation(project(Depends.Core.designSystem))
    implementation(project(Depends.Core.navigation))
    implementation(project(Depends.Common.exceptions))
    implementation(project(Depends.Features.splash))
    implementation(project(Depends.Features.main))
    implementation(project(Depends.Features.onboarding))
}