package com.cop4655.isstracker

import retrofit.Call
import retrofit.http.GET

// This interface defines an API
// service for getting info on the ISS.
interface ApiService {
    // This annotation specifies that the HTTP method
    // is GET and the endpoint URL is "iss-now.json".
    @GET("iss-now.json")
    // This method returns a Call object with a generic
    // type of DataModel, which represents
    // the data model for the response.
    fun getLocation(): Call<DataModel>

}