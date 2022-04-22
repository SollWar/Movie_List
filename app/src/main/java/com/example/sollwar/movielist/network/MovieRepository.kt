package com.example.sollwar.movielist.network

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.sollwar.movielist.network.model.Result
import com.example.sollwar.movielist.network.retrofit.MovieReviewsAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.lang.Exception

class MovieRepository {

    private val movieReviewsAPI = MovieReviewsAPI.getInstance()

    fun getPagedMovie(): Flow<PagingData<Result>> {
        val loader: MoviePageLoader = {offset ->
            getMovies(offset)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { MoviePagingSource(loader, true) }
        ).flow
    }

    private suspend fun getMovies(offset: Int): List<Result> = withContext(Dispatchers.IO) {
        val request = movieReviewsAPI.getMovieList(offset = offset)
        Log.d("RetrofitError", "IS")
        return@withContext request.body()!!.results
    }

}