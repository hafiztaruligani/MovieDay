package com.hafiztaruligani.movieday.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.hafiztaruligani.movieday.R
import com.hafiztaruligani.movieday.databinding.ActivityMainBinding
import com.hafiztaruligani.movieday.domain.model.Genre
import com.hafiztaruligani.movieday.domain.model.MovieSimple
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{true}

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBarController()
    }

    // controlling action bar for each fragment
    private fun setupActionBarController() {

        supportActionBar?.hide()
        supportActionBar?.setHomeAsUpIndicator(R.drawable.left_arrow);

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_nav_host) as NavHostFragment
        navController = navHostFragment.navController

        // listener for fragment
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when(destination.id){

                R.id.detailFragment, R.id.userReviewsFragment -> {
                    val data = arguments?.getParcelable<MovieSimple>("movie_simple")
                    data?.title?.let {
                        setupActionBar(it, true)
                    }
                }

                R.id.pagingMovieFragment -> {
                    val data = arguments?.getParcelable<Genre>("genre")
                    data?.name?.let {
                        setupActionBar(it.plus(' ').plus(getString(R.string.movies)), true)
                    }
                }

                R.id.moviesFragment -> {
                    setupActionBar(resources.getString(R.string.app_name), false)
                }

                else -> supportActionBar?.hide()

            }
        }

    }

    private fun setupActionBar(title: String, enableButton: Boolean) {
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(enableButton)
        supportActionBar?.show()
    }

    fun dismissSplashScreen(){
        splashScreen.setKeepOnScreenCondition { false }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navController.popBackStack()
        return super.onOptionsItemSelected(item)
    }

}