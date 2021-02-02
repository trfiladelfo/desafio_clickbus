package br.com.thiagofiladelfo.clickbus.ui.view.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.*
import br.com.thiagofiladelfo.clickbus.App
import br.com.thiagofiladelfo.clickbus.data.model.Credential
import br.com.thiagofiladelfo.clickbus.data.repository.LoginRepository
import br.com.thiagofiladelfo.clickbus.share.Emitter
import br.com.thiagofiladelfo.clickbus.share.exception.TMException
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    /**
     * Classe para instanciar o ViewModel
     */
    class ViewModelFactory(private val repository: LoginRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            repository.context = App.applicationContext
            return LoginViewModel(repository) as T
        }
    }

    private val _credential = MutableLiveData<Emitter.Message<Credential>>()

    /**
     * Objeto para observer o processamento do login
     */
    val credential: LiveData<Emitter.Message<Credential>>
        get() = _credential


    /**
     * Realiza o login do usuário via conta Google
     */
    fun signIn(activity: Activity) = viewModelScope.launch { repository.signIn(activity) }

    /**
     * Termina os procedimentos da autenticação do Usuário via Google Account
     */
    fun signedIn(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) =
        viewModelScope.launch {
            try {
                val idToken = repository.signedIn(requestCode, resultCode, data)
                if (idToken != null) {
                    val credential = repository.getCredential(activity, idToken)
                    _credential.value = Emitter.Message(
                        status = Emitter.Status.COMPLETE,
                        data = credential
                    )
                } else _credential.value = Emitter.Message(
                    status = Emitter.Status.ERROR,
                    error = TMException("Ops acho que você errou alguma informação da sua conta do Google, verifique e tente novamente")
                )

            } catch (e: ApiException) {
                _credential.value = Emitter.Message(
                    status = Emitter.Status.ERROR,
                    error = TMException("Houve uma falha ao acessar a sua conta do Google, verifique e tente novamente")
                )
            }
        }
}