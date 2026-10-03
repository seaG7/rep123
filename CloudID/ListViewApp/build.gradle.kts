plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ru.mirea.samsonova.listviewapp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "ru.mirea.samsonova.listviewapp"
        minSdk = 33
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
}
