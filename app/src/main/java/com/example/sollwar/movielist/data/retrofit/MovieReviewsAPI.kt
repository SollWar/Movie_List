package com.example.sollwar.movielist.data.retrofit

import com.example.sollwar.movielist.BuildConfig
import com.example.sollwar.movielist.data.model.MovieReviewsResult
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieReviewsAPI {
    @GET("svc/movies/v2/reviews/all.json")
    suspend fun getMovieList(
        @Query("api-key") api_key: String = BuildConfig.API_KEY,
        @Query("offset") offset: Int = 0
    ) : Response<MovieReviewsResult>

    companion object {
        private var movieReviewsAPI: MovieReviewsAPI? = null
        fun getInstance(): MovieReviewsAPI {
            if (movieReviewsAPI == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.nytimes.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                movieReviewsAPI = retrofit.create(MovieReviewsAPI::class.java)
            }
            return movieReviewsAPI!!
        }
    }
}