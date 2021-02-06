package br.com.thiagofiladelfo.clickbus.ui.view.main.movie.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import br.com.thiagofiladelfo.clickbus.data.model.Cast
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.repository.MovieRepository
import br.com.thiagofiladelfo.clickbus.databinding.CastMovieDetailFragmentBinding
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.ui.base.BaseFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.MovieViewModel
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common.adapter.CastAdapter

/**
 * Fragmento responsável pela exibição da aba de Elenco do detalhamento de um filme
 */
class CastFragment : BaseFragment() {

    private var _binding: CastMovieDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels(
        factoryProducer = { MovieViewModel.ViewModelFactory(MovieRepository()) }
    )

    private val adapter = CastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CastMovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.recyclerviewCasts.adapter = adapter

        val movie: Movie = arguments?.getParcelable("movie")!!

        viewModel.credits.observe(viewLifecycleOwner) {
            when (it.status) {
                Emitter.Status.START -> binding.swipeRefreshCasts.isRefreshing = true
                Emitter.Status.COMPLETE -> showActors(it.data!!.cast)
                Emitter.Status.ERROR -> {
                    binding.swipeRefreshCasts.isRefreshing = false
                    AlertDialog.Builder(requireContext()).let { builder ->
                        builder.setMessage(it.error!!.message)
                        builder.setNegativeButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    }.create().show()
                }
            }
        }

        viewModel.getCredits(movie)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * Exibe a listagem com o elenco do filme
     */
    private fun showActors(casts: List<Cast>) {
        binding.swipeRefreshCasts.isRefreshing = false
        adapter.clear()
        adapter.add(casts)
        adapter.notifyDataSetChanged()
    }

    companion object {
        @Volatile
        private var INSTANCE: CastFragment? = null

        /**
         * Recupera a instancia da classe de manipulação dos filmes
         */
        fun newInstance(movie: Movie): CastFragment =
            (INSTANCE ?: synchronized(this) {
                CastFragment().also {
                    it.arguments = Bundle().also {
                        it.putParcelable("movie", movie)
                    }

                    INSTANCE = it
                }
            })
    }

}