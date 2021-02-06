package br.com.thiagofiladelfo.clickbus.ui.view.main.movie

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.repository.MovieRepository
import br.com.thiagofiladelfo.clickbus.databinding.MovieFragmentBinding
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.share.exception.TMException
import br.com.thiagofiladelfo.clickbus.share.exception.TMNetworkingException
import br.com.thiagofiladelfo.clickbus.ui.base.BaseFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common.Business
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common.adapter.MovieAdapter
import com.facebook.shimmer.Shimmer
import com.google.android.material.snackbar.Snackbar

/**
 * Fragmento responsável por exibir a listagem dos filmes
 * Está sendo exibido os mais populares
 */
class MovieFragment : BaseFragment() {

    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MovieViewModel
    private val adapter = MovieAdapter()

    private var page = 1
    private var query: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel = ViewModelProvider(
            this,
            MovieViewModel.ViewModelFactory(
                MovieRepository()
            )
        ).get(MovieViewModel::class.java)

        setInitializeComponentes()
        setObservables()
        fetchMovies()
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerViewContainer.startShimmer()
    }

    override fun onPause() {
        binding.shimmerViewContainer.stopShimmer()
        super.onPause()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    ////// CODIFICACAO

    fun searchMovie(query: String) {
        this.query = if (query.isNotEmpty())
            query else null

        page = 1
        fetchMovies(page, query)
    }

    // Inicializadores ==============
    /**
     * Inicializa a interface
     */
    private fun setInitializeComponentes() {
        adapter.setOnItemClickListener { showDetailMovie(it) }
        adapter.setOnFavoriteListener { viewModel.favoriteMovie(it) }
        adapter.setOnShareListener { shareMovie(it) }
        binding.recycleViewMovies.adapter = adapter

        //Paginacao
        binding.recycleViewMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !binding.swipeRefreshMovies.isRefreshing) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager

                    val visibleItemCount = manager.childCount
                    val totalItemCount = manager.itemCount
                    val firstVisibleItemPosition = manager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        fetchMovies(++page, query)
                    }
                }
            }
        })

        //Swipe Refresh
        binding.swipeRefreshMovies.setOnRefreshListener {
            page = 1
            fetchMovies(page, query)
        }

        arrayOf(binding.buttonTryAgainSearch, binding.buttonTryAgainNetwork).forEach {
            it.setOnClickListener { fetchMovies(page, query) }
        }
    }

    /**
     * Marca os observadores de eventos
     */
    private fun setObservables() {
        viewModel.movies.observe(viewLifecycleOwner, {
            when (it.status) {
                Emitter.Status.START -> binding.swipeRefreshMovies.isRefreshing = true
                Emitter.Status.COMPLETE -> showMovies(it.data ?: emptyList())
                Emitter.Status.ERROR -> {
                    binding.swipeRefreshMovies.isRefreshing = false
                    errorHandler(it.error!!)
                }
            }
        })

        viewModel.favorited.observe(viewLifecycleOwner, {
            when (it.status) {
                Emitter.Status.START -> binding.swipeRefreshMovies.isRefreshing = true
                Emitter.Status.COMPLETE -> updateFavorited(it.data!!)
                Emitter.Status.ERROR -> {
                    binding.swipeRefreshMovies.isRefreshing = false
                    AlertDialog.Builder(requireContext()).let { builder ->
                        builder.setMessage(it.error!!.message)
                        builder.setNegativeButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    }.create().show()
                }
            }
        })
    }

    // Inicializadores ==============


    //UI ==============
    /**
     * Exibe a lista com os dados dos filmes
     */
    private fun showMovies(movies: List<Movie>) {
        binding.swipeRefreshMovies.isRefreshing = false

        if (page == 1 && movies.isEmpty()) {
            binding.viewflipper.displayedChild = 1

        } else if (movies.isNotEmpty()) {
            binding.viewflipper.displayedChild = 3
            adapter.add(movies)
            adapter.notifyDataSetChanged()
        } else {
            binding.viewflipper.displayedChild = 3
            Snackbar.make(binding.root, "Você chegou ao final da lista", Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * Atualiza informação do filme para sinalizar que favoritou um não
     */
    private fun updateFavorited(movie: Movie) {
        val index = adapter.indexOf(movie)
        if (index != -1)
            adapter.notifyItemChanged(index)
    }

    /**
     * Tratamento visual para quando ocorrer um erro
     */
    private fun errorHandler(error: TMException) {
        when(error) {
            is TMNetworkingException -> binding.viewflipper.displayedChild = 2
            else -> {
                binding.viewflipper.displayedChild = 3
                AlertDialog.Builder(requireContext()).let { builder ->
                    builder.setMessage(error.message)
                    builder.setNegativeButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                }.create().show()
            }
        }

    }

    //UI ==============

    //Data ==============
    /**
     * Recupera lista de dados dos filmes
     */
    private fun fetchMovies(page: Int = 1, query: String? = null) {
        if (page == 1) {
            adapter.clear()
            binding.viewflipper.displayedChild = 0
            Shimmer.AlphaHighlightBuilder()
                .setBaseAlpha(0f)
                .setDuration(2000L)
                .setDropoff(0.1f)
                .setIntensity(0.35f)
                .setShape(Shimmer.Shape.RADIAL)
        }

        viewModel.getMovies(page = page, query)
    }

    /**
     * Realiza o compartilhamento dos dados de um filme
     */
    private fun shareMovie(movie: Movie) {
        Business.shareMovie(requireActivity(), movie)
    }

    /**
     * Transição para a tela de detalhamento de um filme
     */
    private fun showDetailMovie(movie: Movie) {
        findNavController().navigate(
            R.id.action_navigation_home_to_homeDetailFragment,
            bundleOf("movie" to movie)
        )
    }

}