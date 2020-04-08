package com.example.testvideoplayer.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.testvideoplayer.R
import com.example.testvideoplayer.data.YoutubeVideo

import kotlinx.android.synthetic.main.adapter_playlist_item.view.*


class PlayerWithListAdapter(
    private val mValues: MutableList<YoutubeVideo>,
    private val mListener: PlayerActivity
) : RecyclerView.Adapter<PlayerWithListAdapter.ViewHolder>() {

   private val mOnClickListener: View.OnClickListener
    private lateinit var  context : Context

    init {
        mOnClickListener = View.OnClickListener { v ->

            val item = v.tag as YoutubeVideo

            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener.onClickListItem(v,item)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_player_list_item, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mValues[position]
        holder.bind(item)
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)

        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        fun bind(item: YoutubeVideo) {

            with(itemView) {
                title.text = item.title
                subTitle.text = item.details
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                   // .apply(RequestOptions().override(width - 36, 200))
                    .into(imageViewItem)

            }
        }

    }
}
