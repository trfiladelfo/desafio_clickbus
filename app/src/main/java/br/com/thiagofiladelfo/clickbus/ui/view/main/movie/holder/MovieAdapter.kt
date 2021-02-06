package br.com.thiagofiladelfo.clickbus.ui.view.main.movie.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.thiagofiladelfo.clickbus.data.model.Movie

class MovieAdapter : RecyclerView.Adapter<MovieHolder>() {

    private val movies: ArrayList<Movie> = arrayListOf()

    //Listeners
    private lateinit var onItemClickListener: (movie: Movie) -> Unit
    private lateinit var onFavoriteListener: (movie: Movie) -> Unit
    private lateinit var onShareListener: (movie: Movie) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
        MovieHolder.getInstance(parent)

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val data = movies[position]

        //eventos
        if (::onFavoriteListener.isInitialized) {
            holder.setOnFavoriteListener { movie -> onFavoriteListener(movie) }
        }

        if (::onShareListener.isInitialized) {
            holder.setOnShareListener { onShareListener(data) }
        }

        if (::onItemClickListener.isInitialized) {
            holder.itemView.setOnClickListener { onItemClickListener(data) }
        }

        //interface
        holder.bind(data)
    }

    override fun getItemCount(): Int = movies.size

    /////

    /**
     * Implemento do evento do click de um item específico
     *
     * @param listener Unit: objeto de referencia para o evento
     * @return movie Movie: informações do item clicado
     */
    fun setOnItemClickListener(listener: (movie: Movie) -> Unit) {
        this.onItemClickListener = listener
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

    /**
     * Limpa a listagem de filmes
     */
    fun clear() = movies.clear()

    /**
     * Adiciona a listagem para ser exibida
     */
    fun add(movies: Collection<Movie>) = movies.forEach { this.movies.add(it) }

    /**
     * Adiciona um filme ao final da lista para ser exibido
     */
    fun add(movie: Movie) = movies.add(movie)

    /**
     * Retorna o índice do filme correspondente, ou -1 se a não contiver tal filme.
     */
    fun indexOf(movie: Movie): Int = movies.indexOfFirst { it.id == movie.id }

}