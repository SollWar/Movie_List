package com.example.sollwar.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sollwar.movielist.adapters.DefaultLoadStateAdapter
import com.example.sollwar.movielist.adapters.MovieAdapter
import com.example.sollwar.movielist.adapters.TryAgainAction
import com.example.sollwar.movielist.databinding.ActivityMainBinding
import com.example.sollwar.movielist.databinding.ItemMovieBinding
import com.example.sollwar.movielist.databinding.PartDefaultLoadStateBinding
import com.example.sollwar.movielist.network.model.Result
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)
        setupMovieList()
    }

    private fun setupMovieList() {
        val adapter = MovieAdapter(this)
        val tryAgainAction: TryAgainAction = {adapter.retry()}
        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)
        val adapterWithLoadState = adapter.withLoadStateFooter(footerAdapter)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapterWithLoadState

        mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
            binding.loadStateView,
            tryAgainAction
        )

        observeMovies(adapter)
        observeLoadState(adapter)
    }
    private fun observeMovies(adapter: MovieAdapter) {
        lifecycleScope.launch {
            viewModel.movieFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
    private fun observeLoadState(adapter: MovieAdapter) {
        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(200).collectLatest { state ->
                mainLoadStateHolder.bind(state.refresh)
                Log.d("loadStateFlow", state.refresh.toString())
            }
        }
    }

    /*private inner class DefaultLoadStateAdapter : LoadStateAdapter<DefaultLoadStateAdapter.Holder>() {

        override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
            holder.bind(loadState)
        }

        override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PartDefaultLoadStateBinding.inflate(inflater, parent, false)
            return Holder(binding, tryAgainAction)
        }

        inner class Holder(
            private val binding: PartDefaultLoadStateBinding,
            private val tryAgainAction: () -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {

            init {
                binding.tryAgainButton.setOnClickListener {
                    tryAgainAction
                }
            }

            fun bind(loadState: LoadState) = with(binding) {
                messageTextView.isVisible = loadState is LoadState.Error
                tryAgainButton.isVisible = loadState is LoadState.Error
                progressBar.isVisible = loadState is LoadState.Error
            }

        }
    }*/
}