package br.com.thiagofiladelfo.clickbus.share

import br.com.thiagofiladelfo.clickbus.share.exception.TMException

object Emitter {

    enum class Status {
        START, COMPLETE, ERROR
    }

    class Message<out T>(
            val status: Status,
            val data: T? = null,
            val error: TMException? = null
    ) {

        val success: Boolean = (status != Status.COMPLETE && status != Status.ERROR) && error != null
        val finished: Boolean = (status == Status.COMPLETE || status == Status.ERROR)
    }
}