package br.com.thiagofiladelfo.clickbus.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto para prover insumos para a aplicação
 */
internal object Resources {

    // Instancia para conexão da API TMdb
    private val API: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val tmdbService: TmdbService by lazy { retrofit.create(TmdbService::class.java) }
}