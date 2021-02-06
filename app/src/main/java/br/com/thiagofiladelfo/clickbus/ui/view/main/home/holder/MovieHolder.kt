package br.com.thiagofiladelfo.clickbus.ui.view.main.home.holder

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.share.Constants
import br.com.thiagofiladelfo.clickbus.share.extension.toDate
import com.bumptech.glide.Glide
import com.shunan.circularprogressbar.CircularProgressBar
import java.text.DateFormat
import java.util.*

class MovieHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        /**
         * Recupera a instancia para uma abrir uma activity
         */
        fun getInstance(parent: ViewGroup): MovieHolder = MovieHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_holder,
                parent,
                false
            )
        )
    }

    //Listeners
    private lateinit var onFavoriteListener: (movie: Movie) -> Unit
    private lateinit var onShareListener: (movie: Movie) -> Unit

    private val sdf: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())

    /**
     * Preenche os dados para popular a tela
     */
    fun bind(movie: Movie) {
        view.findViewById<TextView>(R.id.textview_title).text = movie.title
        view.findViewById<TextView>(R.id.textview_context).text = movie.overview
        view.findViewById<TextView>(R.id.textview_release_date).text =
            sdf.format(movie.releaseDate.toDate())

        val average = (movie.voteAverage.toFloat() * 10F).toInt()
        view.findViewById<TextView>(R.id.text_view_average).text = if (average > 0) "${average}%" else "NR"

        view.findViewById<ImageButton>(R.id.button_favorite).let {
            when {
                movie.favorited -> {
                    it.setImageResource(R.drawable.ic_baseline_favorite_24)
                    it.setColorFilter(ContextCompat.getColor(view.context, android.R.color.holo_red_dark), PorterDuff.Mode.MULTIPLY)
                }
                else -> {
                    it.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    it.setColorFilter(ContextCompat.getColor(view.context, android.R.color.white), PorterDuff.Mode.MULTIPLY)
                }
            }
        }

        view.findViewById<CircularProgressBar>(R.id.circle_view_average).let {
            /*
            Nao tem suporte para mudar a cor :(
            when {

                average >= 70 -> { //verde
                    //it.bgColor = 0x204529
                    it.fgColorStart = 0x21d07a
                    it.fgColorEnd = 0x21d07a
                }

                average < 70 && average >= 30 -> { //amarelo
                    //it.bgColor = 0x423d0f
                    it.fgColorStart = 0xd2d531
                    it.fgColorEnd = 0xd2d531
                }

                average > 0 && average < 30 -> { //vermelho
                    //it.bgColor = 0x571435
                    it.fgColorStart = 0xdb2360
                    it.fgColorEnd = 0xdb2360
                }

                else -> { //branco
                    //it.bgColor = 0x66666
                    it.fgColorStart = 0xd4d4d4
                    it.fgColorEnd = 0xd4d4d4
                }
            }
            */
            it.progress = average
        }

        Glide.with(view)
            .load(Constants.urlImagePosterMovie(movie))
            .into(view.findViewById(R.id.image_view_thumbnail))


        //eventos
        if (::onFavoriteListener.isInitialized) {
            view.findViewById<ImageButton>(R.id.button_favorite).setOnClickListener {
                movie.favorited = !movie.favorited
                onFavoriteListener.invoke(movie)
            }
        }

        if (::onShareListener.isInitialized) {
            view.findViewById<ImageButton>(R.id.button_share).setOnClickListener {
                onShareListener.invoke(movie)
            }
        }
    }

    /**
     * Implemento do evento do click de favoritar um filme
     *
     * @param listener Unit: objeto de referencia para o evento
     * @return movie Movie: informações do item clicado
     */
    fun setOnFavoriteListener(listener: (movie: Movie) -> Unit) {
        this.onFavoriteListener = listener
    }

    /**
     * Implemento do evento do click de compartilhar um filme
     *
     * @param listener Unit: objeto de referencia para o evento
     * @return movie Movie: informações do item clicado
     */
    fun setOnShareListener(listener: (movie: Movie) -> Unit) {
        this.onShareListener = listener
    }

}
