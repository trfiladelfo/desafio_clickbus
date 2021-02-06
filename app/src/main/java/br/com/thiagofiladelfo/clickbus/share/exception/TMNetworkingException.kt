package br.com.thiagofiladelfo.clickbus.share.exception

class TMNetworkingException(message: String? = "Houve uma falha com a comunicação da Internet", cause: Throwable? = null) :
    TMException(message, cause)