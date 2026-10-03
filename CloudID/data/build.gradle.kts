plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "ru.mirea.samsonova.cloudid.data"
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
    androidResources {
        noCompress += "tflite"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.appcompat)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.room.runtime)
    implementation(libs.lifecycle.livedata)
    implementation(libs.tensorflow.lite)
    annotationProcessor(libs.room.compiler)
}
