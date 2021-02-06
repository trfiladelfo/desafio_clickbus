package br.com.thiagofiladelfo.clickbus.ui.base

import androidx.fragment.app.Fragment

/**
 * Classe base para todas os fragmentos da aplicação
 */
open class BaseFragment : Fragment {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)
}