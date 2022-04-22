package com.example.sollwar.movielist.data.model

data class MovieReviewsResult(
    val copyright: String,
    val has_more: Boolean,
    val num_results: Int,
    val results: List<Result>,
    val status: String
)