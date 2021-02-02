package br.com.thiagofiladelfo.clickbus

import android.app.Application
import timber.log.Timber


class App : Application() {

    companion object {
        lateinit var applicationContext: App
            private set
    }

    init {
        App.applicationContext = this
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}