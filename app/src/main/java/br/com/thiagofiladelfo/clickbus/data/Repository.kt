package br.com.thiagofiladelfo.clickbus.data

import android.content.Context
import android.net.ConnectivityManager

interface IRepository {
    var context: Context
}

abstract class Repository: IRepository {
    override lateinit var context: Context

    constructor()
    constructor(context: Context) {
        this.context = context
    }

    @Suppress("DEPRECATION")
    private fun networkAvailable(): Boolean {
        val networkInfo = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}