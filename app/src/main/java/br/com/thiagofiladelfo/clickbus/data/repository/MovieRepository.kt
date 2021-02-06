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
import br.com.thiagofiladelfo.clickbus.share.Constants
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository : Repository {
    constructor()
    constructor(context: Context) : super(context)

    private val service: Service by lazy { Resources.tmdbService }
    private val localStore by lazy { MovieDatabase.getDatabase(context).movieDAO() }

    /**
     * Busca a listagem de filmes
     * @param genders = Códigos dos generos - Opcional
     * @param page = Posição de página - Obrigatório, valor padrão 1 (um)
     */

    suspend fun getMovies(page: Int = 1, query: String? = null): List<Movie> =
        withContext(Dispatchers.IO) {
            val response = (when {
                query == null || query.isEmpty() -> service.getPopularMovies(page = page)
                else -> service.getMoviesByQuery(page = page, query=query)
            }).execute()

            if (response.isSuccessful) {
                val favorited = localStore.getMovies().associate { it.id to it.favorited }
                response.body()!!.results.onEach {
                    it.favorited = favorited.getOrDefault(it.id, false)
                }
            } else throw Exception("Sorry, o carregamento dos filmes falhou")
        }

    suspend fun getMovieDetail(movie: Movie): MovieDetail =
        withContext(Dispatchers.IO) {
            val response = service.getMovieDetail(id = movie.id).execute()

            if (response.isSuccessful)
                response.body()!!
            else throw Exception("Sorry, o carregamento do filme falhou")
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
        if (movie.favorited) {
            Glide.with(context).downloadOnly().load(Constants.urlImagePosterMovie(movie))
            Glide.with(context).downloadOnly().load(Constants.urlImageBackdropMovie(movie))
            localStore.insert(entity)
        } else localStore.delete(entity.id)
        return movie
    }

    suspend fun getCredits(movie: Movie): Credits =
        withContext(Dispatchers.IO) {
            val response = service.getCredits(id = movie.id).execute()

            if (response.isSuccessful)
                response.body()!!
            else throw Exception("Sorry, o carregamento dos creditos falhou")
        }


}