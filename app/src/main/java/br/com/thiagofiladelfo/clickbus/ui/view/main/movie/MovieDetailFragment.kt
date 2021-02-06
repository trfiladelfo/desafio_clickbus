package br.com.thiagofiladelfo.clickbus.ui.view.main.movie

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.data.repository.MovieRepository
import br.com.thiagofiladelfo.clickbus.databinding.MovieDetailFragmentBinding
import br.com.thiagofiladelfo.clickbus.share.Constants
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.share.extension.toDate
import br.com.thiagofiladelfo.clickbus.ui.base.BaseFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.MainActivity
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common.Business
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common.adapter.SectionsPagerAdapter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

/**
 * Fragmento responsável por exibir os dados detalhado do filme
 */
class MovieDetailFragment : BaseFragment() {
    private val args: MovieDetailFragmentArgs by navArgs()

    private var _binding: MovieDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels(
        factoryProducer = { MovieViewModel.ViewModelFactory(MovieRepository()) }
    )

    //Adapter para montar as abas (sessões)
    private val adapter: SectionsPagerAdapter by lazy { SectionsPagerAdapter(requireContext(), childFragmentManager) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as? MainActivity)?.supportActionBar?.title = args.movie.title

        _binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitializeComponentes(args.movie)
        setObservables()
        fetchMovieDetail(args.movie)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    ////// CODIFICACAO

    // Inicializadores ==============
    /**
     * Inicializa a interface
     */
    private fun setInitializeComponentes(movie: Movie) {

        Glide.with(binding.root)
            .load(Constants.urlImageBackdropMovie(movie))
            .placeholder(R.drawable.ic_baseline_cloud_queue_24)
            .error(R.drawable.not_found_backdrop)
            .into(binding.imageviewBackdrop)

        Glide.with(binding.root)
            .load(Constants.urlImagePosterMovie(movie))
            .placeholder(R.drawable.ic_baseline_cloud_queue_24)
            .error(R.drawable.not_found_poster)
            .into(binding.imageviewPoster)

        binding.textviewTitle.text = movie.title
        binding.tabs.setupWithViewPager(binding.viewpager)

        binding.textviewDate.text = movie.releaseDate?.let {
            try { SimpleDateFormat("MMM yyyy").format(it.toDate ()) } catch (e:Throwable) { "" }
        }

        binding.buttonFavorite.setOnClickListener {
            movie.favorited = !movie.favorited
            viewModel.favoriteMovie(movie)
        }
        updateFavorited(movie)


        binding.buttonShare.setOnClickListener { Business.shareMovie(requireActivity(), movie) }

    }

    /**
     * Marca os observadores de eventos
     */
    private fun setObservables() {
        viewModel.movie.observe(viewLifecycleOwner) {
            when (it.status) {
                Emitter.Status.START -> {
                }
                Emitter.Status.COMPLETE -> showMovieDetail(it.data!!)
                Emitter.Status.ERROR -> {
                    AlertDialog.Builder(requireContext()).let { builder ->
                        builder.setMessage(it.error!!.message)
                        builder.setNegativeButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    }.create().show()
                }
            }
        }

        viewModel.favorited.observe(viewLifecycleOwner, {
            when (it.status) {
                Emitter.Status.START -> {
                }
                Emitter.Status.COMPLETE -> updateFavorited(it.data!!)
                Emitter.Status.ERROR -> {
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
     * Exibe os dados de detalhamento do filme
     */
    private fun showMovieDetail(movie: MovieDetail) {
        binding.textviewTime.text = movie.runtime.let {
            val buffer = StringBuffer("${(movie.runtime / 60)}h")

            val minute = (movie.runtime % 60)
            if (minute != 0)
                buffer.append(" ${minute.toString().padStart(2, '0')}m")
            buffer.toString()
        }

        binding.textviewStatus.text = movie.status
        binding.textviewGenders.text = movie.genres.joinToString(" | ") { it.name }

        adapter.movie = args.movie
        adapter.movieDetail = movie
        binding.viewpager.adapter = adapter
    }

    /**
     * Atualiza informação do filme para sinalizar que favoritou um não
     */
    private fun updateFavorited(movie: Movie) {
        Business.rulesFavorited(binding.buttonFavorite, movie)
    }
    //UI ==============

    //Data ==============
    /**
     * Recupera dados sobressalente da listagem do filme
     */
    private fun fetchMovieDetail(movie: Movie) {
        viewModel.getMovieDetail(movie)
    }

}