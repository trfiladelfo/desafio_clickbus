package br.com.thiagofiladelfo.clickbus

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

        Firebase.database.setPersistenceEnabled(true)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}