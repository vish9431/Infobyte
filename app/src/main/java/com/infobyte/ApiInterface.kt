package com.infobyte

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("exec?action=getdata")
    fun getData():Call<DataModel>

}