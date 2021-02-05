package br.com.thiagofiladelfo.clickbus.ui.view.main.home.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.thiagofiladelfo.clickbus.data.model.Cast

class CastAdapter: RecyclerView.Adapter<CastHolder> () {

    private val casts: ArrayList<Cast> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastHolder =
        CastHolder.getInstance(parent)

    override fun onBindViewHolder(holder: CastHolder, position: Int) {
        val data = casts[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = casts.size

    /////


    /**
     * Limpa a listagem de atores
     */
    fun clear() = casts.clear()

    /**
     * Adiciona a listagem para ser exibida
     */
    fun add(casts: Collection<Cast>) = casts.forEach { this.casts.add(it) }

    /**
     * Adiciona um ator ao final da lista para ser exibido
     */
    fun add(cast: Cast) = casts.add(cast)


}