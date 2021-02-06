package br.com.thiagofiladelfo.clickbus.share

import br.com.thiagofiladelfo.clickbus.data.model.Cast
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import java.util.*

object Constants {

    /**
     * Url da API para obter os dados dos filmes e séries
     */
    val urlApi: String = "https://api.themoviedb.org/3/"

    /**
     * Localidade padrão do sistema
     */
    val systemLanguage: String = "${Locale.getDefault().language}-${Locale.getDefault().country}"

    /**
     * Retorna a url da imagem do filme
     */
    fun urlImagePosterMovie(movie: Movie): String =
        "http://image.tmdb.org/t/p/original${movie.posterPath}"

    /**
     * Retorna a url da imagem do filme
     */
    fun urlImageBackdropMovie(movie: Movie): String =
        "http://image.tmdb.org/t/p/original${movie.backdropPath}"

    /**
     * Retorna a url da imagem do ator
     */
    fun urlImageActor(cast: Cast): String = "http://image.tmdb.org/t/p/original${cast.profilePath}"


    /**
     * Retorna a url da página web do filme
     */
    fun urlMovie(movie: Movie): String =
        "https://www.themoviedb.org/movie/${movie.id}?language=${systemLanguage}"

    object sharePreferences {

        val favorite = "app.tmdb.clickbus.movie.favorite"

    }

}