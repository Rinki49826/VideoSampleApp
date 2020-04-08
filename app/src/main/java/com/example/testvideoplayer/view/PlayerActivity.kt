package com.example.testvideoplayer.view

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testvideoplayer.R
import com.example.testvideoplayer.data.YoutubeVideo
import com.example.testvideoplayer.viewModel.VideoPlayerViewModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*


class PlayerActivity : AppCompatActivity() , Player.EventListener {

    private val TAG = "PlayerActivity"

    private lateinit var viewModel: VideoPlayerViewModel

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private  var player: SimpleExoPlayer? = null
    private lateinit var selectedMediaUrl: String
    var fullscreen = false
    private lateinit var adapter: PlayerWithListAdapter

    private  var playerItemDetail : YoutubeVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        viewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)

        initUI()

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

    private fun initUI(){

        playerItemDetail =  intent.getParcelableExtra("selectedItem")
        textView.text = playerItemDetail!!.title
        selectedMediaUrl = playerItemDetail!!.mediaUrl

        handleFullScreenButtonCLick()

        /*val orientation :Int = this.resources.configuration.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.visibility = VISIBLE
        } else {
            recyclerView.visibility = GONE
        }*/

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter =
            PlayerWithListAdapter(
                viewModel.preparePlayerPlayList(),
                this
            )

        recyclerView.adapter =  adapter
    }

    //Create an ExoPlayer
    private fun initializePlayer() {

        player = ExoPlayerFactory.newSimpleInstance(this)
        playerView.player = player

       // selectedMediaUrl = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"  //static url for testing
        val uri :Uri = Uri.parse(selectedMediaUrl)
        Log.e(TAG,"Print media uri:--- $uri")
        val  mediaSource :MediaSource = buildMediaSource(uri)

        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.prepare(mediaSource, false, false)

    }


    //creating a ProgressiveMediaSource
    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    //Overloaded function to Build various MediaSource for whole playlist of video/audio uri
    fun buildMediaList(uriList: Array<Uri>) : MediaSource{

        // These factories are used to construct two media sources below
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this, "exoplayer-codelab")
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
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    private fun handleFullScreenButtonCLick(){

        fullscreenButton.setOnClickListener(View.OnClickListener {
            if (fullscreen) {
                fullscreenButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@PlayerActivity,
                        R.drawable.ic_fullscreen_open
                    )
                )
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                if (supportActionBar != null) {
                    supportActionBar!!.show()
                }
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                val params = playerView.layoutParams as ConstraintLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = (0 * applicationContext.resources.displayMetrics.density).toInt()
                params.matchConstraintPercentHeight = 0.35.toFloat() //set 35% height

                playerView.layoutParams = params
                fullscreen = false
            }
            else {
                fullscreenButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@PlayerActivity,
                        R.drawable.ic_fullscreen_close
                    )
                )
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                if (supportActionBar != null) {
                    supportActionBar!!.hide()
                }
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

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
        //Log.d(TAG,"onLoadingChanged: -----------$isLoading")

    }

    override fun onPositionDiscontinuity(reason: Int) {
       // Log.d(TAG,"onPositionDiscontinuity: -----------$reason")
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
       // Log.d(TAG,"onRepeatModeChanged: -----------$repeatMode")
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
       // Log.d(TAG,"onShuffleModeEnabledChanged: -----------")
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
       // Log.d(TAG,"onTimelineChanged: -----------")
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

        Log.d(TAG, "onPlayerStateChanged: Buffering video. $playWhenReady")

        when (playbackState) {
            Player.STATE_BUFFERING -> {
                Log.e(TAG, "onPlayerStateChanged: Buffering video.")
                if (progressBar != null) {
                    progressBar.visibility = VISIBLE
                }
            }
            Player.STATE_ENDED -> {
                Log.d(TAG, "onPlayerStateChanged: Video ended.")
                player!!.seekTo(0)
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

    fun onClickListItem(view : View , item: YoutubeVideo) {
        Log.e(TAG, "Print click on List item  ${item.title}")
        selectedMediaUrl = item.mediaUrl
        textView.text = item.title
        releasePlayer()
        initializePlayer()
    }

}
