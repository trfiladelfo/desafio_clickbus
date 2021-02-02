package br.com.thiagofiladelfo.clickbus.data.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import br.com.thiagofiladelfo.clickbus.R
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

class LoginRepository: Repository {
    constructor()
    constructor(context: Context): super(context)

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private var auth: FirebaseAuth = Firebase.auth

    @WorkerThread
    suspend fun signIn(activity: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.google_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        activity.startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    @WorkerThread
    suspend fun signedIn(requestCode: Int, resultCode: Int, data: Intent?): String? {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            return task.getResult(ApiException::class.java)!!.idToken
        } else {
            throw ApiException(RESULT_INTERNAL_ERROR)
        }
    }

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