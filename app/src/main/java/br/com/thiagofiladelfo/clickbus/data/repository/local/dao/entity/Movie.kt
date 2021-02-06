package br.com.thiagofiladelfo.clickbus.data.repository.local.dao.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    val adult: Boolean,
    val overview: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int,

    val video: Boolean,
    val popularity: Double,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,

    var favorited: Boolean
) {
    constructor(movie: br.com.thiagofiladelfo.clickbus.data.model.Movie) : this(
        movie.id,
        movie.title,
        movie.posterPath,
        movie.adult,
        movie.overview,
        movie.releaseDate,
        movie.originalTitle,
        movie.originalLanguage,
        movie.backdropPath,
        movie.voteCount,
        movie.video,
        movie.popularity,
        movie.voteAverage,
        movie.favorited
    )
}