package br.com.thiagofiladelfo.clickbus.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Classe representativa do gênero do filme
 */
data class Gender(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Gender> {
        override fun createFromParcel(parcel: Parcel): Gender {
            return Gender(parcel)
        }

        override fun newArray(size: Int): Array<Gender?> {
            return arrayOfNulls(size)
        }
    }
}