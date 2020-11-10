package com.pro.app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.pro.app.data.Resource
import com.pro.app.data.models.ModelUser
import com.pro.app.data.models.ModelUserData
import com.pro.app.ui.base.BaseViewModel

class MainViewModel(application: Application) : BaseViewModel(application) {

    var usersListLiveData: MutableLiveData<Resource<ArrayList<ModelUser>>> = MutableLiveData()
    var userDataLiveData: MutableLiveData<Resource<ModelUserData>> = MutableLiveData()

    fun getUsersList(since:String,per_page:String): MutableLiveData<Resource<ArrayList<ModelUser>>> {
        appInteractor.getUsersList(usersListLiveData,since,per_page)
        return usersListLiveData
    }

    fun getUserData(login: String): MutableLiveData<Resource<ModelUserData>> {
        appInteractor.getUserData(login, userDataLiveData)
        return userDataLiveData
    }
}