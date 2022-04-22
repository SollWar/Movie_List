package com.example.sollwar.movielist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sollwar.movielist.databinding.ItemMovieBinding
import com.example.sollwar.movielist.network.model.Result
import com.squareup.picasso.Picasso

class MovieAdapter(private val context: Context) : PagingDataAdapter<Result, MovieAdapter.Holder>(MovieDiffCallback()) {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val result = getItem(position) ?: return
        with(holder.binding) {
            title.text = result.display_title
            headline.text = result.headline
            summaryShort.text = result.summary_short
            Picasso.with(context)
                .load(result.multimedia.src)
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

class MovieDiffCallback : DiffUtil.ItemCallback<com.example.sollwar.movielist.network.model.Result>() {
    override fun areItemsTheSame(oldItem: com.example.sollwar.movielist.network.model.Result, newItem: com.example.sollwar.movielist.network.model.Result): Boolean {
        return oldItem.display_title == newItem.display_title
    }

    override fun areContentsTheSame(oldItem: com.example.sollwar.movielist.network.model.Result, newItem: com.example.sollwar.movielist.network.model.Result): Boolean {
        return oldItem == newItem
    }

}