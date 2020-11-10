package com.pro.app.data.network

import com.pro.app.data.models.ModelUser
import com.pro.app.data.models.ModelUserData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET(EndPoints.GET_USERS_LIST)
    fun getUsersList(
        @Query("since") since: String,
        @Query("per_page") per_page: String
    ): Call<ArrayList<ModelUser>>

    @GET(EndPoints.GET_USER_DATA)
    fun getUserData(
        @Path("userid") movie_id: String
    ): Call<ModelUserData>
}
