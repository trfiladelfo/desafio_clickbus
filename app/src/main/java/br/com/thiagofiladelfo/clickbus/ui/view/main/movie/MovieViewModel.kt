package br.com.thiagofiladelfo.clickbus.ui.view.main.movie

import androidx.lifecycle.*
import br.com.thiagofiladelfo.clickbus.App
import br.com.thiagofiladelfo.clickbus.data.model.Credits
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.data.repository.MovieRepository
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.share.exception.TMException
import br.com.thiagofiladelfo.clickbus.share.exception.TMNetworkingException
import kotlinx.coroutines.launch

/**
 * Classe de manipulação para exibição dos filmes populares
 */
class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    class ViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            repository.context = App.applicationContext
            return MovieViewModel(repository) as T
        }
    }

    private val _movies = MutableLiveData<Emitter.Message<List<Movie>>>()
    val movies: LiveData<Emitter.Message<List<Movie>>> = _movies

    private val _movie = MutableLiveData<Emitter.Message<MovieDetail>>()
    val movie: LiveData<Emitter.Message<MovieDetail>> = _movie

    private val _favorited = MutableLiveData<Emitter.Message<Movie>>()
    val favorited: LiveData<Emitter.Message<Movie>> = _favorited

    private val _credits = MutableLiveData<Emitter.Message<Credits>>()
    val credits: LiveData<Emitter.Message<Credits>> = _credits

    /**
     * Recupera a lista de filmes
     */
    fun getMovies(page: Int = 1, query: String? = null) =
        viewModelScope.launch {
            try {
                if (repository.networkAvailable()) {
                    val movies = repository.getMovies(page, query)
                    _movies.value = Emitter.Message(
                        status = Emitter.Status.COMPLETE,
                        data = movies
                    )
                } else
                    _movies.value = Emitter.Message(
                        status = Emitter.Status.ERROR,
                        error = TMNetworkingException()
                    )

            } catch (e: Exception) {
                _movies.value = Emitter.Message(
                    status = Emitter.Status.ERROR,
                    error = TMException(e.message, e)
                )
            }
        }

    /**
     * Recupera o detalhamento de um determinado filme
     */
    fun getMovieDetail(movie: Movie) =
        viewModelScope.launch {
            try {
                val detail = repository.getMovieDetail(movie)
                _movie.value = Emitter.Message(
                    status = Emitter.Status.COMPLETE,
                    data = detail
                )

            } catch (e: Exception) {
                _movie.value = Emitter.Message(
                    status = Emitter.Status.ERROR,
                    error = TMException(e.message, e)
                )
            }
        }


    /**
     * Recupera as pessoas que trabalharam neste filme
     */
    fun getCredits(movie: Movie) =
        viewModelScope.launch {
            try {
                val credits = repository.getCredits(movie)
                _credits.value = Emitter.Message(
                    status = Emitter.Status.COMPLETE,
                    data = credits
                )

            } catch (e: Exception) {
                _credits.value = Emitter.Message(
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