package br.com.thiagofiladelfo.clickbus.ui.view.main.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.repository.MovieRepository
import br.com.thiagofiladelfo.clickbus.databinding.HomeFragmentBinding
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.ui.base.BaseFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.home.holder.MovieAdapter

class HomeFragment : BaseFragment() {

    private val binding by viewBinding(HomeFragmentBinding::bind)
    private lateinit var viewModel: HomeViewModel

    private val adapter = MovieAdapter()

    private val filterGenders: ArrayList<Int> = arrayListOf()
    private var page = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel = ViewModelProvider(
            this,
            HomeViewModel.ViewModelFactory(
                MovieRepository()
            )
        ).get(HomeViewModel::class.java)

        setInitializeComponentes()
        setObservables()
    }


    ////// CODIFICACAO

    // Inicializadores
    private fun setInitializeComponentes() {
        binding.recycleViewMovies.adapter = adapter

        //Paginacao
        binding.recycleViewMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && ! binding.swipeRefreshMovies.isRefreshing) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager

                    val visibleItemCount = manager.childCount
                    val totalItemCount = manager.itemCount
                    val firstVisibleItemPosition = manager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        fetchMovies(filterGenders.toTypedArray(), ++page)
                    }
                }
            }
        })

        //Swipe Refresh
        binding.swipeRefreshMovies.setOnRefreshListener {
            page = 1
            fetchMovies(filterGenders.toTypedArray(), page)
        }

    }

    private fun setObservables() {
        viewModel.movies.observe(viewLifecycleOwner, {
            when(it.status) {
                Emitter.Status.START -> showLoading()
                Emitter.Status.COMPLETE -> showMovies(it.data ?: emptyList())
                Emitter.Status.ERROR -> AlertDialog.Builder(requireContext()).let { builder ->
                    builder.setMessage(it.error!!.message)
                    builder.setNegativeButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                }.create().show()
            }
        })
    }
    // Inicializadores


    //UI
    private fun showLoading() {
        binding.swipeRefreshMovies.isRefreshing = true
    }

    private fun showMovies(movies: List<Movie>) {
        binding.swipeRefreshMovies.isRefreshing = false
        if (movies.isNotEmpty()) {
            adapter.add(movies)
            adapter.notifyDataSetChanged()
        }
    }
    //UI

    //Data
    private fun fetchMovies(genders: Array<Int> = emptyArray(), page: Int = 1) {
        if (page == 1) adapter.clear()
        viewModel.getMovies(genders = genders, page = page)
    }

}