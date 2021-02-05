package br.com.thiagofiladelfo.clickbus.ui.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.thiagofiladelfo.clickbus.R
import br.com.thiagofiladelfo.clickbus.databinding.MainActivityBinding
import br.com.thiagofiladelfo.clickbus.ui.base.BaseActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity: BaseActivity() {

    companion object {
        /**
         * Recupera a instancia para uma abrir uma activity
         */
        fun getInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

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
            if(destination.id == R.id.homeDetailFragment) {
                binding.containerSearchMove.visibility = View.GONE
            } else {
                binding.containerSearchMove.visibility = View.VISIBLE
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_for_you, R.id.navigation_favorite)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val user = Firebase.auth.currentUser

        Glide.with(binding.root)
            .load(user!!.photoUrl)
            .circleCrop()
            .into(binding.imageviewProfile)
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()

}