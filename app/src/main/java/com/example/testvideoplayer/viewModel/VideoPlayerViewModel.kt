package com.example.testvideoplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testvideoplayer.data.YoutubeVideo


class VideoPlayerViewModel : ViewModel() {

    // Create a LiveData with a YoutubeVideo
    private val playlistClickItem: MutableLiveData<YoutubeVideo> by lazy {
        MutableLiveData<YoutubeVideo>()
    }

    //private val mediaList = MutableLiveData<MutableList<YoutubeVideo>>()

    fun setListClickItem(item: YoutubeVideo) {
        playlistClickItem.value = item
    }

    fun getListClickItem(): LiveData<YoutubeVideo> {
        return playlistClickItem
    }


    fun prepareList(): MutableList<YoutubeVideo> {
        val videoArrayList: MutableList<YoutubeVideo> = ArrayList()
        // add first item

        videoArrayList.add(
            YoutubeVideo(1L,
                "Sending Data to a New Activity with Intent Extras",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Sending+Data+to+a+New+Activity+with+Intent+Extras.mp4",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Sending+Data+to+a+New+Activity+with+Intent+Extras.png",
                "Description for media object #1"
            )
        )

        videoArrayList.add(
            YoutubeVideo(2L,
                "REST API, Retrofit2, MVVM Course SUMMARY",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/REST+API+Retrofit+MVVM+Course+Summary.mp4",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/REST+API%2C+Retrofit2%2C+MVVM+Course+SUMMARY.png",
                "Description for media object #2"
            )
        )

        videoArrayList.add(
            YoutubeVideo(3L,
                "MVVM and LiveData",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/MVVM+and+LiveData+for+youtube.mp4",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/mvvm+and+livedata.png",
                "Description for media object #3"
            )
        )

        videoArrayList.add(
            YoutubeVideo(4L,
                "Swiping Views with a ViewPager",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/SwipingViewPager+Tutorial.mp4",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Swiping+Views+with+a+ViewPager.png",
                "Description for media object #4"
            )
        )

        videoArrayList.add(
            YoutubeVideo(5L,
                "Database Cache, MVVM, Retrofit, REST API demo for upcoming course",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Rest+api+teaser+video.mp4",
                "https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Rest+API+Integration+with+MVVM.png",
                "Description for media object #5"
            )
        )

        return videoArrayList
    }

  /**
   *  to get dummy data for player playlist view
   *  Actually list will be based on selected video/media type. eg : if you will select game video from general playlist
   *  so in next screen it will display selected media along with playlist which is  related to that  video
   *  */

    fun preparePlayerPlayList(): MutableList<YoutubeVideo> {
       val videoArrayList: MutableList<YoutubeVideo> = ArrayList()

       // add first item
       videoArrayList.add(
          YoutubeVideo(1L,
           "Thugs Of Hindostan - Official Trailer | Amitabh Bachchan | Aamir Khan",
           "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3",
           "https://i.ytimg.com/vi/zI-Pux4uaqM/maxresdefault.jpg",
           "Description for media object #1"

          )
       )

       videoArrayList.add(
           YoutubeVideo(2L,
               "Colors for Children to Learning with Baby Fun Play with Color Balls Dolphin...",
               "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3",
               "https://i.ytimg.com/vi/8ZK_S-46KwE/maxresdefault.jpg",
               "Description for media object #2"
           )
       )

       videoArrayList.add(
           YoutubeVideo(3L,
               "8czMWUH7vW4",
               "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3",
               "https://i.ytimg.com/vi/8czMWUH7vW4/hqdefault.jpg",
               "Description for media object #3"
           )
       )

       videoArrayList.add(
           YoutubeVideo(4L,
               "EXPERIMENT Glowing 1000 degree METAL BALL vs Gunpowder (100 grams)",
               "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3",
               "https://i.ytimg.com/vi/YrQVYEb6hcc/maxresdefault.jpg",
               "Description for media object #4"

           )
       )
        return videoArrayList
    }

}
