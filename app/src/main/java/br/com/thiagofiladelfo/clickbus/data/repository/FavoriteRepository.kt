package br.com.thiagofiladelfo.clickbus.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import br.com.thiagofiladelfo.clickbus.data.Repository
import br.com.thiagofiladelfo.clickbus.data.model.Credits
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.data.repository.local.dao.database.MovieDatabase
import br.com.thiagofiladelfo.clickbus.data.repository.network.Resources
import br.com.thiagofiladelfo.clickbus.data.repository.network.Service
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRepository : Repository {
    constructor()
    constructor(context: Context) : super(context)

    private val localStore by lazy {
        val database = MovieDatabase.getDatabase(context)
        database.movieDAO()
    }

    /**
     * Busca a listagem de filmes
     * @param genders = Códigos dos generos - Opcional
     * @param page = Posição de página - Obrigatório, valor padrão 1 (um)
     */
    suspend fun getMovies(page: Int = 1, query: String? = null): List<Movie> =
        withContext(Dispatchers.IO) {
            val movies = localStore.getMovies().map {
                Movie(
                    it.id,
                    it.title,
                    it.posterPath,
                    it.adult,
                    it.overview,
                    it.releaseDate,
                    null,
                    it.originalTitle,
                    it.originalLanguage,
                    it.backdropPath,
                    it.voteCount,
                    it.video,
                    it.popularity,
                    it.voteAverage,
                    it.favorited
                )
            }
            if (query != null && query.isNotEmpty())
                movies.filter { it.title.contains(query, true) || it.originalTitle.contains(query, true)  }
            else movies
        }

    @WorkerThread
    suspend fun favoriteMovie(movie: Movie): Movie {

        /** Bloco para saber no firebase *
        val user = Firebase.auth.currentUser
        val database = Firebase.database.getReference(user!!.email!!.md5())
        database.keepSynced(true)

        database.child("favorite").child(movie.id.toString()).setValue(favorited)
        true
         */

//            context.getSharedPreferences(Constants.sharePreferences.favorite, Context.MODE_PRIVATE).edit().let {
//                it.putBoolean(movie.id.toString(), favorited)
//                it.apply()
//                it.commit()
//            }

        val entity = br.com.thiagofiladelfo.clickbus.data.repository.local.dao.entity.Movie(movie)
        if (movie.favorited) localStore.insert(entity)
        else localStore.delete(entity.id)
        return movie
    }


}