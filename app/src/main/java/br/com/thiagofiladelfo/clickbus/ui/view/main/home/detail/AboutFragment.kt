package br.com.thiagofiladelfo.clickbus.ui.view.main.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.databinding.AboutHomeDetailFragmentBinding
import br.com.thiagofiladelfo.clickbus.ui.base.BaseFragment
import java.text.DecimalFormat
import java.text.NumberFormat

class AboutFragment : BaseFragment() {

    private var _binding: AboutHomeDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val currencyFormat = NumberFormat.getCurrencyInstance()

    companion object {
        fun newInstance(movie: MovieDetail): AboutFragment = AboutFragment().also {
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
        _binding = AboutHomeDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val movie: MovieDetail = arguments?.getParcelable("movie")!!
        binding.textviewBugdet.text = currencyFormat.format(movie.budget)
        binding.textviewRevenue.text = currencyFormat.format(movie.revenue)
        binding.textviewOverview.text = movie.overview
    }



    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}