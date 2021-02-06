package br.com.thiagofiladelfo.clickbus.share.exception

/**
 * Classe de exceção para identificar o erro de conexão
 * @param message - mensagem para ser apresentada na tela
 * @param cause - excessão que originou o erro
 */
class TMNetworkingException(message: String? = "Houve uma falha com a comunicação da Internet", cause: Throwable? = null) :
    TMException(message, cause)