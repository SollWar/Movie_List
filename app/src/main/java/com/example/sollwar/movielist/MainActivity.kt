package com.example.sollwar.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sollwar.movielist.databinding.ActivityMainBinding
import com.example.sollwar.movielist.databinding.ItemMovieBinding
import com.example.sollwar.movielist.network.model.Result
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)
        setupMovieList()
    }

    private fun setupMovieList() {
        val adapter = MovieAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        observeMovies(adapter)

    }
    private fun observeMovies(adapter: MovieAdapter) {
        lifecycleScope.launch {
            viewModel.movieFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private inner class MovieAdapter : PagingDataAdapter<Result, MovieAdapter.Holder>(MovieDiffCallback()) {

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val result = getItem(position) ?: return
            with (holder.binding) {
                title.text = result.display_title
                headline.text = result.headline
                summaryShort.text = result.summary_short
                Picasso.with(this@MainActivity)
                    .load(result.multimedia.src)
                    .into(poster)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMovieBinding.inflate(inflater, parent, false)
            return Holder(binding)
        }

        inner class Holder(
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
}