package com.example.testvideoplayer.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.testvideoplayer.R
import com.example.testvideoplayer.data.YoutubeVideo
import com.example.testvideoplayer.viewModel.VideoPlayerViewModel
import kotlinx.android.synthetic.main.fragment_video_list.view.*


class VideoListFragment : Fragment() {

    private lateinit var viewModel: VideoPlayerViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VideoRecyclerViewAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_list, container, false)

        viewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)


        //recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView = view.recyclerView

        // Set the adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()

        adapter =
            VideoRecyclerViewAdapter(
                viewModel.prepareList(),
                this
            )
        recyclerView.adapter =  adapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(VideoPlayerViewModel::class.java)
        // TODO: Use the ViewModel
    }



    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        private const val TAG: String = "VideoListFragment"

        fun newInstance() =
            VideoListFragment()

       /* const val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        fun newInstance(columnCount: Int) =
            VideoListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }*/
    }

    fun onClickListItem(view : View , item: YoutubeVideo) {
        Log.e(TAG, "Print click on List item  ${item.title}")

       // viewModel.setListClickItem(item)
        // Navigation.findNavController(view).navigate(R.id.action_videoListFragment_to_videoDetailsFragment)

        val i = Intent(context, PlayerActivity::class.java)
        i.putExtra("selectedItem",item)
        //i.putExtra("myString", "This is a message for ActivityB")
        startActivity(i)

    }


}
