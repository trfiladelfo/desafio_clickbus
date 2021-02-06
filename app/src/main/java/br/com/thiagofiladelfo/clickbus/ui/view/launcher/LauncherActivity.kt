package br.com.thiagofiladelfo.clickbus.ui.view.launcher

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.ui.base.BaseActivity
import br.com.thiagofiladelfo.clickbus.ui.view.login.LoginActivity
import br.com.thiagofiladelfo.clickbus.ui.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Classe inicializadora da aplicação
 */
class LauncherActivity : BaseActivity(R.layout.launcher_activity) {

    // Objeto do Firebase para autenticação
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            window.insetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(WindowInsets.Type.systemBars())
            }
        else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }

        supportActionBar?.hide()
        auth = Firebase.auth
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        //Seleção de tela quando o usuario for ou não logado
        val screen = if (auth.currentUser != null)
            MainActivity.getInstance(this@LauncherActivity)
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