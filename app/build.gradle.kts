import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.cop4655.isstracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cop4655.isstracker"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val secretsFile = rootProject.file("secrets.properties")
        val properties = Properties()
        properties.load(secretsFile.inputStream())

        // Return an empty string in case of property being null
        val googleMapsApiKey = properties.getProperty("GOOGLE_MAPS_API_KEY") ?: ""
        val locationPermissionRequest = properties.getProperty("LOCATION_PERMISSION_REQUEST") ?: ""
        val n2yoApiKey = properties.getProperty("N2YO_API_KEY") ?: ""
        android.buildFeatures.buildConfig = true
        // For accessing the property using BuildConfig
        resValue("string", "GOOGLE_MAPS_API_KEY", googleMapsApiKey)

        buildConfigField(
            type = "int",
            name = "LOCATION_PERMISSION_REQUEST",
            value = locationPermissionRequest
        )
        buildConfigField(
            type = "String",
            name = "N2YO_API_KEY",
            value = n2yoApiKey
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.retrofit)
    implementation(libs.android.gms)
    implementation (libs.picasso)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
