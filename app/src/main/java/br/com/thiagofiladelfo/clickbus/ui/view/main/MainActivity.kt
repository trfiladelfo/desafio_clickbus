package br.com.thiagofiladelfo.clickbus.ui.view.main

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.databinding.MainActivityBinding
import br.com.thiagofiladelfo.clickbus.ui.base.BaseActivity
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.FavoriteFragment
import br.com.thiagofiladelfo.clickbus.ui.view.main.movie.MovieFragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

/**
 * Classe de manipulação do desembarque do usuário na aplicação
 */
class MainActivity : BaseActivity() {

    private lateinit var binding: MainActivityBinding  //ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.containerSearchMove.visibility =
                if (destination.id == R.id.homeDetailFragment) View.GONE else View.VISIBLE
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_favorite)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val user = Firebase.auth.currentUser

        Glide.with(binding.root)
            .load(user!!.photoUrl)
            .error(R.drawable.ic_baseline_face_24)
            .circleCrop()
            .into(binding.imageviewProfile)

        binding.edittextSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchMovie(binding.edittextSearch.text.toString())
                    return@OnEditorActionListener true
                }
                else -> false
            }
        })

        binding.buttonVoiceSearch.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            )
                checkPermission()
            else listenerUser()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ListenerRecordAudioRequestCode) {
            if (resultCode == RESULT_OK) {
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()?.let {
                    binding.edittextSearch.setText(it)
                    searchMovie(it)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AuthorizeRecordAudioRequestCode && grantResults.isNotEmpty())
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) Snackbar.make(
                binding.root,
                "Que peninha! Você não permitiu o uso do microfone",
                Snackbar.LENGTH_SHORT
            ).show() else listenerUser()
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()

    /**
     * Valida as permissões necessárias para busca por voz
     */
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                AuthorizeRecordAudioRequestCode
            )
    }

    /**
     * Manipulador para realizar a captura da voz
     */
    private fun listenerUser() {
        SpeechRecognizer.createSpeechRecognizer(this)
        try {
            startActivityForResult(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).also {
                it.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                it.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                it.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.hello_say_movie_name))
            }, ListenerRecordAudioRequestCode)

        } catch (e: ActivityNotFoundException) {
            Snackbar.make(
                binding.root,
                "Ops! Seu dispositivo não tem o suporte",
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }

    /**
     * Busca a lista de filmes baseada em um fragmento do titulo
     */
    private fun searchMovie(query: String) {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        when (val currentFragment = navHostFragment.childFragmentManager.fragments[0]) {
            is MovieFragment -> currentFragment.searchMovie(query)
            is FavoriteFragment -> currentFragment.searchMovie(query)
        }
    }

    companion object {

        const val AuthorizeRecordAudioRequestCode = 777
        const val ListenerRecordAudioRequestCode = 555

        /**
         * Recupera a instancia para uma abrir uma activity
         */
        fun getInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}