package com.pro.app.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelUserData(
        var login: String = "",
        var name: String = "",
        var public_repos: String = "",
        var public_gists: String = "",
        var followers: String = "",
        var following: String = "",
        var location: String = "",
        var bio: String = "",
        var avatar_url: String = "",
        var html_url: String = ""
):Parcelable