package br.com.thiagofiladelfo.clickbus.share.exception

class TMException(message: String? = "Houve uma falha desconhecida", cause: Throwable? = null) :
    Throwable(message, cause)