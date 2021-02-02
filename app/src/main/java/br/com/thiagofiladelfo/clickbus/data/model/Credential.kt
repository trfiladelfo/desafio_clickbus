package br.com.thiagofiladelfo.clickbus.data.model

import android.net.Uri

data class Credential(
    val uuid: String,
    val isAnonymous: Boolean,
    val displayName: String?,
    val email: String?,
    val phoneNumber: String?,
    val photoUrl: Uri?
)