package com.hafiztaruligani.movieday.presentation.detail

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.*
import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.movieday.BuildConfig
import com.hafiztaruligani.movieday.databinding.YoutubePlayerBinding
import com.hafiztaruligani.movieday.util.Cons.KEY

/**
 * YouTubePlayerSupportFragment is not support in androidx
 **/
class YoutubePlayer : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var binding : YoutubePlayerBinding
    private lateinit var videoKey : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = YoutubePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // get video key
        intent.getStringExtra(KEY)?.let {
            videoKey = it
        }?: run {
            showErrorMessage()
            finish()
        }

        setupYoutubePlayer()

    }

    private fun setupYoutubePlayer() {
        val playerView = YouTubePlayerView(this)
        playerView.layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.youtubePlayer.addView(playerView)
        playerView.initialize(BuildConfig.YOUTUBE_API_KEY, this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        // load the video
        if (!wasRestored) {
            youTubePlayer?.loadVideo(videoKey)
            youTubePlayer?.setFullscreen(true)
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {
        val  code = 0
        if (youTubeInitializationResult?.isUserRecoverableError == true) {
            youTubeInitializationResult.getErrorDialog(this, code).show()
        } else showErrorMessage()
        finish()
    }

    private fun showErrorMessage(){
        val errorMessage = Resource.COMMON_ERROR
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

}