package br.com.thiagofiladelfo.clickbus.share.exception

open class TMException(message: String? = "Houve uma falha desconhecida", cause: Throwable? = null) :
    Throwable(message, cause)