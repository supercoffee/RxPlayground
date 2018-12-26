package com.bendaschel.rxjavaplayground

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bendaschel.rxjavaplayground.network.Link

class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.tv_post_title)

    fun bind(item: Link) {
        title.text = item.title
    }
}

class ListingDiffer: DiffUtil.ItemCallback<Link>() {
    override fun areItemsTheSame(p0: Link, p1: Link): Boolean {
        return false
    }

    override fun areContentsTheSame(p0: Link, p1: Link): Boolean {
        return false
    }
}

class ListingAdapter: ListAdapter<Link, ListingViewHolder>(ListingDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ListingViewHolder{
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.listing_item, parent, false)
        return ListingViewHolder(layout)
    }

    override fun onBindViewHolder(vh: ListingViewHolder, position: Int) {
        vh.bind(getItem(position))
    }
}