package br.com.thiagofiladelfo.clickbus.ui.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import br.com.thiagofiladelfo.clickbus.ui.base.BaseActivity

class HomeActivity: BaseActivity() {

    companion object {
        /**
         * Recupera a instancia para uma abrir uma activity
         */
        fun getInstance(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}