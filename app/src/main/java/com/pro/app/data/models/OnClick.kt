package com.pro.app.data.models

interface OnClick {
    fun onMovieClicked(modelNowPlaying: ModelNowPlaying){}
    fun onCreditClicked(modelCredit: ModelCredit){}
}