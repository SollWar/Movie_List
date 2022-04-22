package com.example.sollwar.movielist.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sollwar.movielist.data.model.Result

typealias MoviePageLoader = suspend (offset: Int) -> List<Result>

class MoviePagingSource(
    private val loader: MoviePageLoader
) : PagingSource<Int, Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val pageIndex = params.key ?: 0

        return try {
            val movies = loader.invoke(pageIndex * 20)
            return LoadResult.Page(
                data = movies,
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (params.loadSize < 20) null else pageIndex + 1
            )
        } catch (e: Exception) {
            Log.d("loadStateFlow", e.toString())
            LoadResult.Error(
                throwable = e
            )
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}