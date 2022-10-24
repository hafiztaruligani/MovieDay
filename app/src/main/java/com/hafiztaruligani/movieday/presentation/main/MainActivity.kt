package com.hafiztaruligani.movieday.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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

    private var initiateView = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBarController()
    }

    // controlling action bar for each fragment
    private fun setupActionBarController() {

        supportActionBar?.hide()
        supportActionBar?.setHomeAsUpIndicator(R.drawable.left_arrow);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_nav_host) as NavHostFragment
        navController = navHostFragment.navController

        // listener for fragment
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when(destination.id){

                R.id.detailFragment, R.id.userReviewsFragment -> {
                    val data = arguments?.getParcelable<MovieSimple>("movie_simple")
                    data?.title?.let {
                        setupActionBar(it)
                    }
                }

                R.id.pagingMovieFragment -> {
                    val data = arguments?.getParcelable<Genre>("genre")
                    data?.name?.let {
                        setupActionBar(it.plus(' ').plus(getString(R.string.movies)))
                    }
                }

                R.id.moviesFragment -> {
                    if(!initiateView) setupActionBar(resources.getString(R.string.app_name))
                    else initiateView = false
                }

                else -> supportActionBar?.hide()

            }
        }

    }

    fun setupActionBar(s: String) {
        supportActionBar?.title = s
        supportActionBar?.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navController.popBackStack()
        return super.onOptionsItemSelected(item)
    }

}