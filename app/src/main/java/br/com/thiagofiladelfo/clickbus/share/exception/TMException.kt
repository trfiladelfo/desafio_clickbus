package br.com.thiagofiladelfo.clickbus.share.exception

/**
 * Classe Genérica de exceção
 * @param message - mensagem para ser apresentada na tela
 * @param cause - excessão que originou o erro
 */
open class TMException(message: String? = "Houve uma falha desconhecida", cause: Throwable? = null) :
    Throwable(message, cause)