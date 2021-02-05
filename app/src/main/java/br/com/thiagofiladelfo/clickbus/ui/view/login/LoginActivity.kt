package br.com.thiagofiladelfo.clickbus.ui.view.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import br.com.thiagofiladelfo.clickbus.data.model.Credential
import br.com.thiagofiladelfo.clickbus.data.repository.LoginRepository
import br.com.thiagofiladelfo.clickbus.databinding.LoginActivityBinding
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.ui.base.BaseActivity
import br.com.thiagofiladelfo.clickbus.ui.view.main.MainActivity

class LoginActivity : BaseActivity() {

    companion object {
        /**
         * Recupera a instancia para uma abrir uma activity
         */
        fun getInstance(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private lateinit var viewModel: LoginViewModel      //viewModel
    private lateinit var binding: LoginActivityBinding  //ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        viewModel = ViewModelProvider(this,
            LoginViewModel.ViewModelFactory(
                LoginRepository()
            )
        ).get(LoginViewModel::class.java)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        binding.buttonGoogleSignIn.setOnClickListener { signInAction() }

        viewModel.credential.observe(this, {
            when (it.status) {
                Emitter.Status.START -> {
                }
                Emitter.Status.COMPLETE -> moveHomeAction()
                Emitter.Status.ERROR -> AlertDialog.Builder(this).let { builder ->
                    builder.setMessage(it.error!!.message)
                    builder.setNegativeButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                }.create().show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.signedIn(this, requestCode, data)
    }

    ///// CODIFICACAO

    /**
     * Método para responder a ação do botão de login
     */
    private fun signInAction() {
        viewModel.signIn(this)
    }

    /**
     * Método para mudar de tela para Home
     */
    private fun moveHomeAction() {
        startActivity(MainActivity.getInstance(this))
        finish()
    }
}