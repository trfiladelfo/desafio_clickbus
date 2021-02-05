package br.com.thiagofiladelfo.clickbus.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("budget") val budget: Long,
    @SerializedName("genres") val genres: List<Gender>,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("imdb_id") val imdbId: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("production_companies") val companies: List<ProductionCompany>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val title: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("vote_average") val averageScore: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("status") val status: String,
    @SerializedName("revenue") val revenue: Long,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.createTypedArrayList(Gender) ?: arrayListOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.createTypedArrayList(ProductionCompany) ?: arrayListOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(backdropPath)
        parcel.writeLong(budget)
        parcel.writeTypedList(genres)
        parcel.writeString(homepage)
        parcel.writeString(imdbId)
        parcel.writeString(originalTitle)
        parcel.writeString(overview)
        parcel.writeDouble(popularity)
        parcel.writeString(posterPath)
        parcel.writeTypedList(companies)
        parcel.writeString(releaseDate)
        parcel.writeString(title)
        parcel.writeString(tagline)
        parcel.writeDouble(averageScore)
        parcel.writeInt(voteCount)
        parcel.writeInt(runtime)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieDetail> {
        override fun createFromParcel(parcel: Parcel): MovieDetail {
            return MovieDetail(parcel)
        }

        override fun newArray(size: Int): Array<MovieDetail?> {
            return arrayOfNulls(size)
        }
    }
}