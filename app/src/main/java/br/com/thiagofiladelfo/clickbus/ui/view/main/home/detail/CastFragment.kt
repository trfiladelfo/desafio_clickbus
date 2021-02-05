package br.com.thiagofiladelfo.clickbus.ui.view.main.home.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import br.com.thiagofiladelfo.clickbus.data.model.Cast
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.data.repository.MovieRepository
import br.com.thiagofiladelfo.clickbus.databinding.AboutHomeDetailFragmentBinding
import br.com.thiagofiladelfo.clickbus.databinding.CastHomeDetailFragmentBinding
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.ui.base.BaseFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.home.HomeViewModel
import br.com.thiagofiladelfo.clickbus.ui.view.main.home.holder.CastAdapter
import br.com.thiagofiladelfo.clickbus.ui.view.main.home.holder.MovieAdapter
import java.text.DecimalFormat
import java.text.NumberFormat

class CastFragment : BaseFragment() {

    private var _binding: CastHomeDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels(
        factoryProducer = { HomeViewModel.ViewModelFactory(MovieRepository()) }
    )

    private val adapter = CastAdapter()

    companion object {
        fun newInstance(movie: Movie): CastFragment = CastFragment().also {
            it.arguments = Bundle().also {
                it.putParcelable("movie", movie)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CastHomeDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.recyclerviewCasts.adapter = adapter

        val movie: Movie = arguments?.getParcelable("movie")!!

        viewModel.credits.observe(viewLifecycleOwner){
           when(it.status) {
               Emitter.Status.START -> {}
               Emitter.Status.COMPLETE -> showActors(it.data!!.cast)
               Emitter.Status.ERROR -> {
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

    private fun showActors(casts: List<Cast>) {
        adapter.clear()
        adapter.add(casts)
        adapter.notifyDataSetChanged()
    }

}