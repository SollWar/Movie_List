package com.example.sollwar.movielist.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.sollwar.movielist.data.model.Result
import com.example.sollwar.movielist.data.retrofit.MovieReviewsAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepository {

    private val movieReviewsAPI = MovieReviewsAPI.getInstance()

    fun getPagedMovie(): Flow<PagingData<Result>> {
        val loader: MoviePageLoader = { offset ->
            getMovies(offset)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { MoviePagingSource(loader) }
        ).flow
    }

    private suspend fun getMovies(offset: Int): List<Result> = withContext(Dispatchers.IO) {
        val request = movieReviewsAPI.getMovieList(offset = offset)
        Log.d("Retrofit", request.isSuccessful.toString())
        if (request.code() == 429) {
            Log.d("Retrofit", "Ошибка 429")
        }
        return@withContext request.body()!!.results
    }

}