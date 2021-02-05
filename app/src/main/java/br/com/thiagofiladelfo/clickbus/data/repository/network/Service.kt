package br.com.thiagofiladelfo.clickbus.data.repository.network

import br.com.thiagofiladelfo.clickbus.BuildConfig
import br.com.thiagofiladelfo.clickbus.data.model.Credits
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.data.repository.network.callback.MoviesResult
import br.com.thiagofiladelfo.clickbus.share.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

/**
 * Interface para realizar comunicação com o Serviço remoto do TMDB
 */
interface Service {

    /**
     * Busca todos os filmes poupares
     * @param apiKey = Chave de conexão com o servidor - Opcional
     * @param language = Regionalização do conteúdo - Opcional
     * @param genres = Filtro para vários generos - Opcional
     * @param page = Posição de página - Obrigatório, valor padrão 1 (um)
     */
    @GET("discover/movie?sort_by=popularity.desc")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = Constants.systemLanguage,
        @Query("with_genres") genres: String = "",
        @Query("page") page: Int = 0,
    ): Call<MoviesResult>

    /**
     * Busca um determinado filmes
     * @param id = Idenficador do filme
     * @param apiKey = Chave de conexão com o servidor - Opcional
     * @param language = Regionalização do conteúdo - Opcional
     */
    @GET("movie/{id}")
    fun getMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = Constants.systemLanguage
    ): Call<MovieDetail>


    @GET("movie/{id}/credits")
    fun getCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = Constants.systemLanguage
    ): Call<Credits>

}