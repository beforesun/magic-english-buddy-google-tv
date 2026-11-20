plugins { id("com.android.application") }

android {
    namespace = "dev.beforesun.magicenglishbuddy.tv"
    compileSdk = 35
    defaultConfig {
        applicationId = "dev.beforesun.magicenglishbuddy.tv"
        minSdk = 23
        targetSdk = 35
        versionCode = 4
        versionName = "0.4.0"
    }
    signingConfigs {
        getByName("debug") {
            val signingPassword = System.getenv("TV_SIGN_PASS")
            if (signingPassword != null) {
                storeFile = file("sideload.p12")
                storeType = "PKCS12"
                storePassword = signingPassword
                keyAlias = "tv"
                keyPassword = signingPassword
            }
        }
    }
}

dependencies { implementation("androidx.webkit:webkit:1.13.0") }
