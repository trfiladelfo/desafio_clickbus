package br.com.thiagofiladelfo.clickbus.ui.view.main.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.data.repository.MovieRepository
import br.com.thiagofiladelfo.clickbus.databinding.HomeDetailFragmentBinding
import br.com.thiagofiladelfo.clickbus.share.Constants
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.share.extension.toDate
import br.com.thiagofiladelfo.clickbus.ui.base.BaseFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.MainActivity
import br.com.thiagofiladelfo.clickbus.ui.view.main.home.holder.SectionsPagerAdapter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class HomeDetailFragment : BaseFragment() {
    private val args: HomeDetailFragmentArgs by navArgs()

    private var _binding: HomeDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels(
        factoryProducer = { HomeViewModel.ViewModelFactory(MovieRepository()) }
    )

    private val adapter: SectionsPagerAdapter by lazy {  SectionsPagerAdapter(childFragmentManager) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as? MainActivity)?.supportActionBar?.title = args.movie.title

        _binding = HomeDetailFragmentBinding.inflate(inflater, container, false)
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
    private fun setInitializeComponentes(movie: Movie) {

        Glide.with(binding.root)
            .load(Constants.urlImageBackdropMovie(movie))
            .into(binding.imageviewBackdrop)

        Glide.with(binding.root)
            .load(Constants.urlImagePosterMovie(movie))
            .into(binding.imageviewPoster)

        binding.textviewTitle.text = movie.title
        binding.textviewDate.text = SimpleDateFormat("MMM yyyy").format(movie.releaseDate.toDate())
        binding.tabs.setupWithViewPager(binding.viewpager)
    }

    private fun setObservables() {
        viewModel.movie.observe(viewLifecycleOwner) {
            when(it.status) {
                Emitter.Status.START -> {}
                Emitter.Status.COMPLETE -> showMovieDetail(it.data!!)
                Emitter.Status.ERROR -> {
                    AlertDialog.Builder(requireContext()).let { builder ->
                        builder.setMessage(it.error!!.message)
                        builder.setNegativeButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    }.create().show()
                }
            }
        }
    }
    // Inicializadores ==============


    //UI ==============
    private fun showMovieDetail(movie: MovieDetail) {
        binding.textviewTime.text = "${(movie.runtime / 60)}h ${(movie.runtime % 60).toString().padStart(2, '0')}m"
        binding.textviewStatus.text = movie.status
        binding.textviewGenders.text = movie.genres.joinToString(" | ") { it.name }

        adapter.movie = args.movie
        adapter.movieDetail = movie
        binding.viewpager.adapter = adapter
    }
    //UI ==============

    //Data ==============
    private fun fetchMovieDetail(movie: Movie) {
        viewModel.getMovieDetail(movie)
    }

}