package com.jay.orderdemo.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * NOTE : Make sure to register BaseApplication in [AndroidManifest.xml]
 */
@HiltAndroidApp
class BaseApplication : Application() {

}