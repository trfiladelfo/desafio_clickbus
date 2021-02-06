package br.com.thiagofiladelfo.clickbus.ui.view.main.movie.common

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.share.Constants

/**
 * Objeto concentrador de regra de negócio
 */
object Business {

    /**
     * Regra de negócio para exibição do favoritado
     */
    fun rulesFavorited(imageButton: ImageButton, movie: Movie) {
        imageButton.let {
            when {
                movie.favorited -> {
                    it.setImageResource(R.drawable.ic_baseline_favorite_24)
                    it.setColorFilter(
                        ContextCompat.getColor(
                            imageButton.context,
                            android.R.color.holo_red_dark
                        ), PorterDuff.Mode.MULTIPLY
                    )
                }
                else -> {
                    it.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    it.setColorFilter(
                        ContextCompat.getColor(
                            imageButton.context,
                            android.R.color.white
                        ), PorterDuff.Mode.MULTIPLY
                    )
                }
            }
        }
    }

    /**
     * Regra de negócio para compartilha os dados do filme
     */
    fun shareMovie(activity: Activity, movie: Movie) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Já assistiu a '${movie.title}'? \n ${Constants.urlMovie(movie)}"
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        activity.startActivity(shareIntent)
    }

}