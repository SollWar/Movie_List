package com.example.sollwar.movielist.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.example.sollwar.movielist.data.MovieRepository
import com.example.sollwar.movielist.data.model.Result
import kotlinx.coroutines.flow.Flow

class MainViewModel : ViewModel() {
    private val movieRepository: MovieRepository = MovieRepository()

    val movieFlow: Flow<PagingData<Result>> = movieRepository.getPagedMovie()

}