plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.virakumaro.testbookcabin.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        buildConfigField("String", "BASE_URL", "\"https://airline.api.cert.platform.sabre.com/\"")
        buildConfigField("String", "AUTH_KEY", "\"Basic VmpFNlQwUk5NREpUUnpwUFJEcFBSQT09OlRVOUNNbE5IVDBRPQ==\"")
        buildConfigField("boolean", "USE_MOCK_DATA", "true")

    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(libs.koin.core)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
}