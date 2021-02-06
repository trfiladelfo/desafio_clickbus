package br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.detail.AboutFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.detail.CastFragment

/**
 * Manipulador das abas (Sobre e Elenco) de detalhamento do filmes
 */
class SectionsPagerAdapter(val context: Context, supportFragmentManager: FragmentManager) :
    FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    lateinit var movie: Movie
    lateinit var movieDetail: MovieDetail

    override fun getPageTitle(position: Int): CharSequence = context.getString(TAB_TITLES[position])
    override fun getCount(): Int = TAB_TITLES.size

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> if (!::movieDetail.isInitialized) Fragment() else AboutFragment.newInstance(
                movieDetail
            )
            1 -> if (!::movie.isInitialized) Fragment() else CastFragment.newInstance(movie)
            else -> Fragment()
        }

    companion object {
        private val TAB_TITLES = arrayOf(R.string.about, R.string.cast)
    }
}