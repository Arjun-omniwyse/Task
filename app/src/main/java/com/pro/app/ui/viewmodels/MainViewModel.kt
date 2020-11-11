package com.pro.app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pro.app.data.Resource
import com.pro.app.data.models.ModelUser
import com.pro.app.data.models.ModelUserData
import com.pro.app.data.network.APIService
import com.pro.app.data.network.AppClient
import com.pro.app.ui.base.BaseViewModel
import com.pro.app.utils.PagingDataSource

class MainViewModel(application: Application) : BaseViewModel(application) {

    var userDataLiveData: MutableLiveData<Resource<ModelUserData>> = MutableLiveData()

    val users = Pager(PagingConfig(pageSize = 20,prefetchDistance = 5)) {
        PagingDataSource(AppClient.getClient().create(APIService::class.java))
    }.flow.cachedIn(viewModelScope)

    fun getUserData(login: String): MutableLiveData<Resource<ModelUserData>> {
        appInteractor.getUserData(login, userDataLiveData)
        return userDataLiveData
    }
}