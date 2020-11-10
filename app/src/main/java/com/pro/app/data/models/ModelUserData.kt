package com.pro.app.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelUserData(
        var name: String = "",
        var public_repos: String = "",
        var public_gists: String = "",
        var followers: String = "",
        var following: String = "",
        var location: String = "",
        var bio: String = ""
):Parcelable