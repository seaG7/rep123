plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "ru.mirea.samsonova.Lesson9.data"
    compileSdk {
        version = release(37)
    }
    defaultConfig {
        minSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":domain"))
}
