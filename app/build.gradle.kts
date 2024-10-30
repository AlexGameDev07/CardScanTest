plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "alejando.murcia.appcardscantest"
    compileSdk = 34

    defaultConfig {
        applicationId = "alejando.murcia.appcardscantest"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //CardScan
    implementation(libs.stripe.cardscan) {
        exclude(group = "com.stripe", module = "ml-core-cardscan") // Excluir la dependencia conflictiva
    }
    implementation(libs.ml.core.googleplay)

    //Retrofit
    implementation(libs.retrofit) // Retrofit
    implementation(libs.converter.gson) // Convertidor Gson (opcional)
    implementation(libs.logging.interceptor) // Interceptor de logging (opcional)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
