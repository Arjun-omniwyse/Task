package com.pro.app.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.pro.app.data.network.AppInteractor

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    var appInteractor: AppInteractor=AppInteractor()
}