package br.com.thiagofiladelfo.clickbus.ui.view.main.movie.holder

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.data.model.MovieDetail
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.detail.AboutFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.detail.CastFragment

class SectionsPagerAdapter(supportFragmentManager: FragmentManager) :
    FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    lateinit var movie: Movie
    lateinit var movieDetail: MovieDetail

    private val TAB_TITLES = arrayOf("Sobre", "Elenco")

    override fun getPageTitle(position: Int): CharSequence = TAB_TITLES[position]
    override fun getCount(): Int = TAB_TITLES.size

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> if (!::movieDetail.isInitialized) Fragment() else AboutFragment.newInstance(
                movieDetail
            )
            1 -> if (!::movie.isInitialized) Fragment() else CastFragment.newInstance(movie)
            else -> Fragment()
        }
}