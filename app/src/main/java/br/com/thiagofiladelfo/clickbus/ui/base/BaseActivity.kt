package br.com.thiagofiladelfo.clickbus.ui.base

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity {
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)
}

