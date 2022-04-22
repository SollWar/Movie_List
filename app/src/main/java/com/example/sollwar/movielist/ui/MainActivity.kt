package com.example.sollwar.movielist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sollwar.movielist.data.adapters.DefaultLoadStateAdapter
import com.example.sollwar.movielist.data.adapters.MovieAdapter
import com.example.sollwar.movielist.data.adapters.TryAgainAction
import com.example.sollwar.movielist.databinding.ActivityMainBinding
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
        val adapter = MovieAdapter()
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
}