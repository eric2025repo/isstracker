package com.cop4655.isstracker

import retrofit.Call
import retrofit.http.GET

// This interface defines an API
// service for getting ISS info.
interface ExpeditionService {
    // This annotation specifies that the HTTP method
    // is GET and the endpoint URL is "people-in-space.json".
    @GET("people-in-space.json")
    // This method returns a Call object with a generic
    // type of DataModel, which represents
    // the data model for the response.
    fun getExpedition(): Call<ExpeditionDataModel>

}