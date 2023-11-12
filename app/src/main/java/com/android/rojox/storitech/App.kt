package com.android.rojox.storitech

import android.app.Application
import com.android.rojox.core.CoreModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CoreModule.init(this)
    }
}