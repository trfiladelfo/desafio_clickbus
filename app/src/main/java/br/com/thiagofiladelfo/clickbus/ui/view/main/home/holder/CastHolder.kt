package br.com.thiagofiladelfo.clickbus.ui.view.main.home.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.data.model.Cast
import br.com.thiagofiladelfo.clickbus.data.model.Movie
import br.com.thiagofiladelfo.clickbus.share.Constants
import br.com.thiagofiladelfo.clickbus.share.extension.toDate
import com.bumptech.glide.Glide
import com.shunan.circularprogressbar.CircularProgressBar
import java.text.DateFormat
import java.util.*

class CastHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        /**
         * Recupera a instancia para uma abrir uma activity
         */
        fun getInstance(parent: ViewGroup): CastHolder = CastHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cast_holder,
                parent,
                false
            )
        )
    }

    /**
     * Preenche os dados para popular a tela
     */
    fun bind(cast: Cast) {

        view.findViewById<TextView>(R.id.textview_name).text = cast.name
        view.findViewById<TextView>(R.id.textview_character).text = cast.character

        Glide.with(view)
            .load(Constants.urlImageActor(cast))
            .circleCrop()
            .into(view.findViewById(R.id.imageview_profile))
    }
}
