package br.com.thiagofiladelfo.clickbus.ui.view.launcher

import android.os.Bundle
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.ui.base.BaseActivity
import br.com.thiagofiladelfo.clickbus.ui.view.home.HomeActivity
import br.com.thiagofiladelfo.clickbus.ui.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LauncherActivity: BaseActivity(R.layout.launcher_activity) {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        auth = Firebase.auth
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        //Seleção de tela quando o usuario for ou não logado
        val screen = if (auth.currentUser != null)
            HomeActivity.getInstance(this@LauncherActivity)
        else
            LoginActivity.getInstance(this@LauncherActivity)

        // Encaminha para tela
        CoroutineScope(Dispatchers.Main).launch {
            delay(2500)
            startActivity(screen)
            finish()
        }
    }

}