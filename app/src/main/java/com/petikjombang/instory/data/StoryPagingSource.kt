package com.petikjombang.instory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.petikjombang.instory.data.local.entity.StoryEntity
import com.petikjombang.instory.data.remote.network.ApiService

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, StoryEntity>() {

    private companion object {
        const val INITIAL_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
        return try {
            val position = params.key ?: INITIAL_INDEX
            val responseData = apiService.getStory(position, params.loadSize).listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}