package com.cop4655.isstracker

import retrofit.Call
import retrofit.http.GET

// This interface defines an API
// service for getting random jokes.
interface VisualPassService {
    // This annotation specifies that the HTTP method
    // is GET and the endpoint URL is "jokes/random".
    @GET("visualpasses/25544/28.2423/-80.6/6/1/0/&apiKey=F3HE6G-2EW2CJ-T9QXPA-5I70")
    // This method returns a Call object with a generic
    // type of DataModel, which represents
    // the data model for the response.
    fun getVisualPass(): Call<VisualPassDataModel>

}