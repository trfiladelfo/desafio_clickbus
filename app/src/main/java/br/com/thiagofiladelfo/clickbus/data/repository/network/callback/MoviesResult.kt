package br.com.thiagofiladelfo.clickbus.data.repository.network.callback

import br.com.thiagofiladelfo.clickbus.data.model.Movie
import com.google.gson.annotations.SerializedName

data class MoviesResult(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)