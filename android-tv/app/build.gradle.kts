plugins { id("com.android.application") }

android {
    namespace = "dev.beforesun.magicenglishbuddy.tv"
    compileSdk = 35
    defaultConfig {
        applicationId = "dev.beforesun.magicenglishbuddy.tv"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
}

dependencies { implementation("androidx.webkit:webkit:1.13.0") }
