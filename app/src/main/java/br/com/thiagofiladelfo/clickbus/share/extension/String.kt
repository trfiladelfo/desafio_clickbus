package br.com.thiagofiladelfo.clickbus.share.extension

import android.annotation.SuppressLint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.toDate(): Date? {
    var date: Date? = null

    val masks = arrayOf("yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd")
    for (mask in masks) {
        try {
            date = SimpleDateFormat(mask).parse(this)
            break
        } catch (e: ParseException) {
        }
    }

    return date
}

fun String.md5(): String {
    try {
        // Create MD5 Hash
        val digest: MessageDigest = MessageDigest.getInstance("MD5")
        digest.update(this.toByteArray())
        val messageDigest: ByteArray = digest.digest()

        // Create Hex String
        val hexString = StringBuffer()
        for (i in messageDigest.indices) hexString.append(
            Integer.toHexString(
                0xFF and messageDigest[i]
                    .toInt()
            )
        )
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        throw IllegalArgumentException()
    }
}