package br.com.thiagofiladelfo.clickbus.share.extension

import android.annotation.SuppressLint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Extensão para transformar uma string em data
 */
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