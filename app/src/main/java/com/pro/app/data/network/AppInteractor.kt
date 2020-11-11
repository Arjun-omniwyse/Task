package com.pro.app.data.network

import androidx.lifecycle.MutableLiveData
import com.pro.app.data.Resource
import com.pro.app.data.models.ModelUser
import com.pro.app.data.models.ModelUserData
import retrofit2.Response

open class AppInteractor {

    private val apiService: APIService = AppClient.getClient().create(APIService::class.java)

    fun getUsersList(
        viewModel: MutableLiveData<Resource<ArrayList<ModelUser>>>, since: String, per_page: String
    ) {

        viewModel.postValue(Resource.loading(null))

        //val call = apiService.getUsersList(since.toInt(),per_page.toInt())
        /*call.enqueue(object : SuccessCallback<ArrayList<ModelUser>>() {

            override fun onSuccess(response: Response<ArrayList<ModelUser>>) {
                try {
                    viewModel.postValue(Resource.success(response.body()))
                } catch (ex: Exception) {
                    viewModel.postValue(Resource.error(ex.toString(), null))
                }
            }

            override fun onFailure(message: String) {
                viewModel.postValue(Resource.error(message, null))
            }
        })*/
    }

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


