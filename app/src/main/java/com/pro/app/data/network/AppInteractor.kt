package com.pro.app.data.network

import androidx.lifecycle.MutableLiveData
import com.pro.app.data.Resource
import com.pro.app.data.models.ModelUser
import com.pro.app.data.models.ModelUserData
import retrofit2.Response

open class AppInteractor {

    private val apiService: APIService = AppClient.getClient().create(APIService::class.java)

    fun getUserData(
        login: String,
        viewModel: MutableLiveData<Resource<ModelUserData>>
    ) {

        viewModel.postValue(Resource.loading(null))
        val call = apiService.getUserData(login)
        call.enqueue(object : SuccessCallback<ModelUserData>() {

            override fun onSuccess(response: Response<ModelUserData>) {
                try {
                    viewModel.postValue(Resource.success(response.body()))
                } catch (ex: Exception) {
                    viewModel.postValue(Resource.error(ex.toString(), null))
                }
            }

            override fun onFailure(message: String) {
                viewModel.postValue(Resource.error(message, null))
            }
        })
    }
}


