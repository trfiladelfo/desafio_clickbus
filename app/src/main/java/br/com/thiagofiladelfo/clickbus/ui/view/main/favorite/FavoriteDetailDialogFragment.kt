package br.com.thiagofiladelfo.clickbus.ui.view.main.favorite

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.databinding.FavoriteDetailDialogFragmentBinding
import br.com.thiagofiladelfo.clickbus.share.Constants
import br.com.thiagofiladelfo.clickbus.share.extension.toDate
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common.Business
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class FavoriteDetailDialogFragment : DialogFragment() {
    private var _binding: FavoriteDetailDialogFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteDetailDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitializeComponentes(requireArguments().getParcelable("movie")!!)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).also {
            it.window?.requestFeature(Window.FEATURE_NO_TITLE)
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
            .into(binding.imageviewBackdrop)

        Glide.with(binding.root)
            .load(Constants.urlImagePosterMovie(movie))
            .into(binding.imageviewPoster)

        binding.textviewTitle.text = movie.title
        binding.textviewDate.text = SimpleDateFormat("MMM yyyy").format(movie.releaseDate.toDate())
        binding.textviewContext.text = movie.overview

        updateFavorited(movie)

        binding.buttonShare.setOnClickListener { Business.shareMovie(requireActivity(), movie) }

    }

    // Inicializadores ==============


    //UI ==============
    /**
     * Atualiza informação do filme para sinalizar que favoritou um não
     */
    private fun updateFavorited(movie: Movie) {
        //Business.rulesFavorited(binding.buttonFavorite, movie)
    }
    //UI ==============

    companion object {
        /**
         * Recupera a instancia da classe de manipulação dos filmes
         */
        fun newInstance(movie: Movie): FavoriteDetailDialogFragment =
            FavoriteDetailDialogFragment().also {
                it.arguments = bundleOf("movie" to movie)
            }
    }
}