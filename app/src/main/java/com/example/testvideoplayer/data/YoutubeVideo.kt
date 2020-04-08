package com.example.testvideoplayer.data


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YoutubeVideo(
    val id: Long,
    val title: String,
    val mediaUrl: String,
    val imageUrl: String,
    val details: String
) : Parcelable


