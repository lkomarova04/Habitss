plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)


}

android {
    namespace = "com.example.habitss"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.habitss"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

}



dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    // Calligraphy
    implementation ("io.github.inflationx:calligraphy3:3.1.1")
    implementation("io.github.inflationx:viewpump:2.0.3")

    // Design
    implementation("androidx.exifinterface:exifinterface:1.3.7")
    implementation ("androidx.media:media:1.7.0")
    implementation ("androidx.vectordrawable:vectordrawable-animated:1.2.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.palette:palette:1.0.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")

    // Room Database
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")



    // Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    api("com.github.bumptech.glide:volley-integration:1.5.0@aar")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation ("com.google.firebase:firebase-storage:19.2.2")
    implementation ("com.google.firebase:firebase-database:19.5.1")

    implementation ("com.applandeo:material-calendar-view:1.9.2")

}
