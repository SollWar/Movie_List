package com.example.sollwar.movielist.network.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sollwar.movielist.network.model.MovieReviewsResult
import retrofit2.converter.gson.GsonConverterFactory
import com.example.sollwar.movielist.network.model.Result
import retrofit2.*

class MovieFetch {
    private val movieReviewsAPI: MovieReviewsAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        movieReviewsAPI = retrofit.create(MovieReviewsAPI::class.java)
    }

    /*fun getMovieList(): LiveData<List<Result>> {
        val mutableLiveData: MutableLiveData<List<Result>> = MutableLiveData()
        val request = movieReviewsAPI.getMovieList()
        request.enqueue(object : Callback<MovieReviewsResult> {
            override fun onResponse(
                call: Call<MovieReviewsResult>,
                response: Response<MovieReviewsResult>
            ) {
                mutableLiveData.value = response.body()?.results ?: listOf()
                Log.d("RetrofitSuccess", mutableLiveData.value.toString())
            }

            override fun onFailure(call: Call<MovieReviewsResult>, t: Throwable) {
                Log.d("RetrofitFail", "Ошибка загрузки")
            }
        })
        return mutableLiveData
    }*/
}