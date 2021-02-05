package br.com.thiagofiladelfo.clickbus.data.repository.network

import br.com.thiagofiladelfo.clickbus.share.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto para prover insumos para a aplicação
 */
internal object Resources {

    // Instancia para conexão da API TMdb
    private val api: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.urlApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val tmdbService: Service by lazy { api.create(Service::class.java) }
}