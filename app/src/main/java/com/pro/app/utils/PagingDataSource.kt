package com.pro.app.utils

import androidx.paging.PagingSource
import com.pro.app.data.models.ModelUser
import com.pro.app.data.network.APIService
import com.pro.app.extensions.showLog

class PagingDataSource(private val networkService: APIService) : PagingSource<Int, ModelUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ModelUser> {

        return try {
            val nextPageNumber = params.key ?: 1

            val response = networkService.getUsersList(0, 20)
            LoadResult.Page(
                data = response!!,
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = 20
            )

        } catch (e: Exception) {
            "Error: " + e.toString().showLog()
            LoadResult.Error(e)
        }
    }

}