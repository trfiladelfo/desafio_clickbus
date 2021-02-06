package br.com.thiagofiladelfo.clickbus.share

import br.com.thiagofiladelfo.clickbus.share.exception.TMException

/**
 * Objeto identificados das mensagerias
 */
object Emitter {

    /**
     * Classificação das mensagerias
     */
    enum class Status { START, COMPLETE, ERROR }

    /**
     * Classe que envelopa as mensagem
     * @param status - classificação da mensagem
     * @param data - informação a ser enviada
     * @param error - caso ocorrer um erro será envelopado a mensagem neste atributo e o data terá seu valor nulo
     */
    class Message<out T>(
        val status: Status,
        val data: T? = null,
        val error: TMException? = null
    ) {

        /**
         * Sinalizador de sucesso
         */
        val success: Boolean =
            (status != Status.COMPLETE && status != Status.ERROR) && error != null

        /**
         * Sinalizador de termino de processamento
         */
        val finished: Boolean = (status == Status.COMPLETE || status == Status.ERROR)
    }
}