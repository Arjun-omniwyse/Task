package com.pro.app.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelUser(
        var login: String = "",
        var avatar_url: String = ""
):Parcelable