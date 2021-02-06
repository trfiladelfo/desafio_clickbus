package br.com.thiagofiladelfo.clickbus.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * Classe representativa da credencial do usuário logado no aplicativo
 */
data class Credential(
    val uuid: String,
    val isAnonymous: Boolean,
    val displayName: String?,
    val email: String?,
    val phoneNumber: String?,
    val photoUrl: Uri?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Uri::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
        parcel.writeByte(if (isAnonymous) 1 else 0)
        parcel.writeString(displayName)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeParcelable(photoUrl, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Credential> {
        override fun createFromParcel(parcel: Parcel): Credential {
            return Credential(parcel)
        }

        override fun newArray(size: Int): Array<Credential?> {
            return arrayOfNulls(size)
        }
    }
}