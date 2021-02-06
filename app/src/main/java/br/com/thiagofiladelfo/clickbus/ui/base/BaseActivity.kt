package br.com.thiagofiladelfo.clickbus.ui.base

import androidx.appcompat.app.AppCompatActivity

/**
 * Classe base para todas as activity da aplicação
 */
open class BaseActivity : AppCompatActivity {
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)
}

