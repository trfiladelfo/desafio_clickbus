package br.com.thiagofiladelfo.clickbus.ui.view.main.movie

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.repository.FavoriteRepository
import br.com.thiagofiladelfo.clickbus.databinding.FavoriteFragmentBinding
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.ui.base.BaseFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.favorite.FavoriteDetailDialogFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.favorite.FavoriteViewModel
import br.com.thiagofiladelfo.clickbus.ui.view.main.favorite.common.adapter.FavoriteAdapter
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common.Business

class FavoriteFragment : BaseFragment() {

    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoriteViewModel

    private val adapter = FavoriteAdapter()

    private var page = 1
    private var query: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            FavoriteViewModel.ViewModelFactory(
                FavoriteRepository()
            )
        ).get(FavoriteViewModel::class.java)

        setInitializeComponentes()
        setObservables()

        fetchMovies()
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
        adapter.setOnFavoriteListener { removeFavorite(it) }
        adapter.setOnShareListener { shareMovie(it) }
        binding.recycleViewMovies.adapter = adapter

        //Swipe Refresh
        binding.swipeRefreshMovies.setOnRefreshListener {
            page = 1
            fetchMovies(page, query)
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
                    AlertDialog.Builder(requireContext()).let { builder ->
                        builder.setMessage(it.error!!.message)
                        builder.setNegativeButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    }.create().show()
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
        if (movies.isNotEmpty()) {
            adapter.add(movies)
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * Atualiza informação do filme para sinalizar que favoritou um não
     */
    private fun updateFavorited(movie: Movie) {
        adapter.removeItem(movie)
        adapter.notifyDataSetChanged()
    }
    //UI ==============

    //Data ==============
    /**
     * Recupera lista de dados dos filmes
     */
    private fun fetchMovies(page: Int = 1, query: String? = null) {
        if (page == 1) adapter.clear()
        viewModel.getMovies(page = page, query)
    }

    /**
     * Realiza o compartilhamento dos dados de um filme
     */
    private fun shareMovie(movie: Movie) {
        Business.shareMovie(requireActivity(), movie)
    }

    private fun removeFavorite(movie: Movie) {
        AlertDialog.Builder(requireContext()).let { builder ->
            builder.setMessage("Deseja realmente remover o filme '${movie.title}' dos favoritos?")
            builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()

                movie.favorited = !movie.favorited
                viewModel.favoriteMovie(movie)
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()

    }

    /**
     * Transição para a tela de detalhamento de um filme
     */
    private fun showDetailMovie(movie: Movie) =
        FavoriteDetailDialogFragment.newInstance(movie)
            .show(childFragmentManager, "detail_movie_favorite")

}