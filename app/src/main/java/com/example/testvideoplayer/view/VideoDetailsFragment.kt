package com.example.testvideoplayer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.testvideoplayer.R
import com.example.testvideoplayer.data.YoutubeVideo
import com.example.testvideoplayer.viewModel.VideoPlayerViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*

class VideoDetailsFragment : Fragment(),Player.EventListener  {

    private val TAG = "VideoDetailsFragment"
    private lateinit var viewModel: VideoPlayerViewModel

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var player: SimpleExoPlayer
    var fullscreen = false

    private  var playerItemDetail : YoutubeVideo? = null


    companion object {

        fun newInstance() =
            VideoDetailsFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)
        // Create the observer which updates the UI.
      /*  viewModel.getListClickItem().observe(this, Observer{ item ->
            playerItemDetail = item
            Log.e(TAG,"1playerItemDetail:--- ${playerItemDetail.title}")
        })*/

        viewModel.getListClickItem().observe(this, Observer{ item ->
            playerItemDetail = item
            Log.e(TAG,"1playerItemDetail:--- ${item.title}")
        })

        handleFullScreenButtonCLick()
    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()

        hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }
    //Create an ExoPlayer
    private fun initializePlayer() {

        player = ExoPlayerFactory.newSimpleInstance(context)
        playerView.player = player

        val uri :Uri = Uri.parse("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3") //static url for testing
        //val uri :Uri = Uri.parse(playerItemDetail?.mediaUrl)
        Log.e(TAG,"Print media uri:--- $uri")
        val  mediaSource :MediaSource = buildMediaSource(uri)

        player.playWhenReady = playWhenReady
        player.seekTo(currentWindow, playbackPosition)
        player.prepare(mediaSource, false, false)
    }


    //creating a ProgressiveMediaSource
    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(activity, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    //Overloaded function to Build various MediaSource for whole playlist of video/audio uri
    fun buildMediaList(uriList: Array<Uri>) : MediaSource{

        // These factories are used to construct two media sources below
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(activity, "exoplayer-codelab")
        val mediaSourceFactory: ProgressiveMediaSource.Factory = ProgressiveMediaSource.Factory(dataSourceFactory)

        val playlistMediaSource = ConcatenatingMediaSource()
        uriList.forEach {
            playlistMediaSource.addMediaSource(buildMediaSource(it))
            playlistMediaSource.addMediaSource(mediaSourceFactory.createMediaSource(Uri.parse("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")))
        }

        return playlistMediaSource
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player.playWhenReady
            playbackPosition = player.currentPosition
            currentWindow = player.currentWindowIndex
            player.release()
            //player = null
        }
    }

    private fun handleFullScreenButtonCLick(){

        fullscreenButton.setOnClickListener(View.OnClickListener {
            if (fullscreen) {
                fullscreenButton.setImageDrawable(
                    context?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1,
                            R.drawable.ic_fullscreen_open
                        )
                    }
                )
                activity!!.window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
              /*  if (supportActionBar != null) {
                    supportActionBar!!.show()
                }*/
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                val params = playerView.layoutParams as ConstraintLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = (0 * activity!!.applicationContext.resources.displayMetrics.density).toInt()
                params.matchConstraintPercentHeight = 0.35.toFloat() //set 35% height

                playerView.layoutParams = params
                fullscreen = false
            }
            else {
                fullscreenButton.setImageDrawable(
                    context?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1,
                            R.drawable.ic_fullscreen_close
                        )
                    }
                )
                activity!!.window!!.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
               /* if ( activity!!.supportActionBar != null) {
                    activity!!.supportActionBar!!.hide()
                }*/
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                val params = playerView.layoutParams as ConstraintLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                playerView.layoutParams = params
                fullscreen = true
            }
        })
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

    }

    override fun onSeekProcessed() {

    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

    }

    override fun onPlayerError(error: ExoPlaybackException?) {

    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onRepeatModeChanged(repeatMode: Int) {

    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

        when (playbackState) {
            Player.STATE_BUFFERING -> {
                Log.e(TAG, "onPlayerStateChanged: Buffering video.")
                if (progressBar != null) {
                    progressBar.visibility = VISIBLE
                }
            }
            Player.STATE_ENDED -> {
                Log.d(TAG, "onPlayerStateChanged: Video ended.")
                player.seekTo(0)
            }
            Player.STATE_IDLE -> { }
            Player.STATE_READY -> {
                Log.e(TAG, "onPlayerStateChanged: Ready to play.")
                if (progressBar != null) {
                    progressBar.visibility = GONE
                }
            }
            else -> {
            }
        }

    }


}
