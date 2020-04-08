package com.example.testvideoplayer.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testvideoplayer.R

/*
Assignment - Make a Video player
As a next step, we would like you to make an app where you create a clone of Youtube player.
1.Should have custom controllers for play/pause, fast forward/fast backward, screen size controller for fullscreen.
2.Should show the playlist on portrait mode below the player.
3.Should support all orientation.
4.You can set fixed player height to 35% of the device display height on portrait mode and fill on other modes.
5.Use standard Google code formatting.
6.Handle LifeCycle Callbacks properly.
*/

class PlaylistsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
