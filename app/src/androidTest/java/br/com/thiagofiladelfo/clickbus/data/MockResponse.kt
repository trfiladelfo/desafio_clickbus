package br.com.thiagofiladelfo.clickbus.data

import okhttp3.mockwebserver.MockResponse
import java.io.InputStreamReader

fun MockResponse.setBodyWith(path: String): String? {
    var content: String? = null

    try {
        val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    } catch (e: Throwable) {}

    return content
}