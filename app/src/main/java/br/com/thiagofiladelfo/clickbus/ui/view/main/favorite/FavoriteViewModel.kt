package br.com.thiagofiladelfo.clickbus.ui.view.main.favorite

import androidx.lifecycle.*
import br.com.thiagofiladelfo.clickbus.App
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.data.repository.FavoriteRepository
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.share.exception.TMException
import kotlinx.coroutines.launch

/**
 * Classe de manipulação das informações dos filmes favoritados
 */
class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

    class ViewModelFactory(private val repository: FavoriteRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            repository.context = App.applicationContext
            return FavoriteViewModel(repository) as T
        }
    }

    private val _movies = MutableLiveData<Emitter.Message<List<Movie>>>()
    val movies: LiveData<Emitter.Message<List<Movie>>> = _movies

    private val _movie = MutableLiveData<Emitter.Message<MovieDetail>>()
    val movie: LiveData<Emitter.Message<MovieDetail>> = _movie

    private val _favorited = MutableLiveData<Emitter.Message<Movie>>()
    val favorited: LiveData<Emitter.Message<Movie>> = _favorited

    /**
     * Recupera a lista de filmes
     */
    fun getMovies(page: Int = 1, query: String? = null) =
        viewModelScope.launch {
            try {
                val movies = repository.getMovies(page, query)
                _movies.value = Emitter.Message(
                    status = Emitter.Status.COMPLETE,
                    data = movies
                )

            } catch (e: Exception) {
                _movies.value = Emitter.Message(
                    status = Emitter.Status.ERROR,
                    error = TMException(e.message, e)
                )
            }
        }

    /**
     * Marca ou remove o filme favorito
     */
    fun favoriteMovie(movie: Movie) =
        viewModelScope.launch {
            try {
                _favorited.value = Emitter.Message(
                    status = Emitter.Status.COMPLETE,
                    data = repository.favoriteMovie(movie)
                )

            } catch (e: Exception) {
                _favorited.value = Emitter.Message(
                    status = Emitter.Status.ERROR,
                    error = TMException(e.message, e)
                )
            }
        }


}