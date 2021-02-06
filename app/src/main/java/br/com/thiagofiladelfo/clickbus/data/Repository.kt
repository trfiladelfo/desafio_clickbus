package br.com.thiagofiladelfo.clickbus.data

import android.content.Context
import android.net.ConnectivityManager

/**
 * Interface de contrato para um repositório
 * @see Repository
 */
interface IRepository {
    var context: Context
}

/**
 * Implementação padrão para um repositório
 */
abstract class Repository : IRepository {
    override lateinit var context: Context

    constructor()
    constructor(context: Context) {
        this.context = context
    }

    /**
     * Valida se o aplicativo tem conexão com a Internet
     */
    @Suppress("DEPRECATION")
    fun networkAvailable(): Boolean {
        val networkInfo =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}