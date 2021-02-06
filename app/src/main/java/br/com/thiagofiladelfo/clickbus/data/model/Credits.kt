package br.com.thiagofiladelfo.clickbus.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Credits(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<Cast>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(Cast) ?: arrayListOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeTypedList(cast)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Credits> {
        override fun createFromParcel(parcel: Parcel): Credits {
            return Credits(parcel)
        }

        override fun newArray(size: Int): Array<Credits?> {
            return arrayOfNulls(size)
        }
    }
}
