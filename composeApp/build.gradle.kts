import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting
        val commonMain by getting

        commonMain.dependencies {
            //Shared dependencies
            //Default
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            //Custom
            //Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.content)
            //Kotlinx
            implementation(libs.kotlinx.coroutines)
//            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.datetime)
            //Moko
            implementation(libs.moko.mvvm.core)
            implementation(libs.moko.mvvm.compose)
            //Kamel
            implementation(libs.kamel)
            //Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.bottom.navigator)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.transitions)
            //Charts plot
            implementation(libs.koala.plot)

            //Icons
            implementation(libs.composeIcons.feather)
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.sqlDelight.android)
            implementation(libs.moko.geo.compose)
            implementation(libs.moko.media.compose)
            implementation(libs.moko.biometry.compose)
            implementation(libs.moko.permission.compose)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
            implementation(libs.sqlDelight.native)
            implementation(libs.moko.geo.compose)
            implementation(libs.moko.media.compose)
            implementation(libs.moko.biometry.compose)
            implementation(libs.moko.permission.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.sqlDelight.jvm)
        }
    }
}

android {
    namespace = "udesc.eso.ddm"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "udesc.eso.ddm"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
//        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "udesc.eso.ddm"
            packageVersion = "1.0.0"
        }
    }
}
sqldelight {
    databases {
        create("TodoDatabase") {
            packageName.set("udesc.eso.ddm.kotlin")
        }
    }
}