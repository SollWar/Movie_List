package com.example.sollwar.movielist.network

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.sollwar.movielist.network.model.Result
import com.example.sollwar.movielist.network.retrofit.MovieReviewsAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.*
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
        var movies = listOf<Result>()
        val request = movieReviewsAPI.getMovieList(offset = offset)
        try {
            movies = request.body()!!.results
            Log.d("RetrofitSuccess", movies.toString())
        } catch (e: Exception) {
            Log.d("RetrofitError", e.toString())
        }
        Log.d("RetrofitSuccess", "Test")
        return@withContext movies
    }

}