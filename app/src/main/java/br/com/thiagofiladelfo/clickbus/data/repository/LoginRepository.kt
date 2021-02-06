package br.com.thiagofiladelfo.clickbus.data.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import br.com.thiagofiladelfo.clickbus.BuildConfig
import br.com.thiagofiladelfo.clickbus.data.Repository
import br.com.thiagofiladelfo.clickbus.data.model.Credential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status.RESULT_INTERNAL_ERROR
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred

/**
 * Responsável por manipular os dados do usuário da aplicação.
 * Essa classe tem a função de concentrar todas as regras de négocio (ponto único da informação)
 */
class LoginRepository : Repository {
    constructor()
    constructor(context: Context) : super(context)

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private var auth: FirebaseAuth = Firebase.auth

    /**
     * Realiza o ingresso do usuário na aplicação via Google Autheticator
     * @param activity - referencia da atividade que está sendo a chamadora
     */
    @WorkerThread
    suspend fun signIn(activity: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        activity.startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    /**
     * Valida o ingresso na plataforma Google
     * @param requestCode - referencia do código de requisição
     * @param data - dados adicional oriundo do handshake no Google
     */
    @WorkerThread
    suspend fun signedIn(requestCode: Int, data: Intent?): String? {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            return task.getResult(ApiException::class.java)!!.idToken
        } else {
            throw ApiException(RESULT_INTERNAL_ERROR)
        }
    }

    /**
     * Finaliza a validação das informações coletadas durante o handshake no Google
     * @param activity - referencia da atividade que está sendo a chamadora
     * @param idToken - identificador do processamento realizado
     */
    @WorkerThread
    suspend fun getCredential(activity: Activity, idToken: String): Credential {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val response = CompletableDeferred<FirebaseUser>()

        auth.signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            when {
                task.isSuccessful -> {
                    if (task.isSuccessful)
                        response.complete(auth.currentUser!!)
                }
                else -> {
                    response.completeExceptionally(Exception())
                }
            }
        }

        val user = response.await()

        return Credential(
            user.uid,
            user.isAnonymous,
            user.displayName,
            user.email,
            user.phoneNumber,
            user.photoUrl
        )
    }

}