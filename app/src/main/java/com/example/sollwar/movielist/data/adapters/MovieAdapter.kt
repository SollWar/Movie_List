package com.example.sollwar.movielist.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sollwar.movielist.R
import com.example.sollwar.movielist.databinding.ItemMovieBinding
import com.example.sollwar.movielist.data.model.Result
import com.squareup.picasso.Picasso

class MovieAdapter : PagingDataAdapter<Result, MovieAdapter.Holder>(MovieDiffCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val result = getItem(position) ?: return
        with(holder.binding) {
            title.text = result.display_title
            summaryShort.text = result.summary_short
            Picasso.get()
                .load(result.multimedia.src)
                .placeholder(R.drawable.placeholder)
                .into(poster)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    class Holder(
        val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

class MovieDiffCallback : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.display_title == newItem.display_title
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }

}