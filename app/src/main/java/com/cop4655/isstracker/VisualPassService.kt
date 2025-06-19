package com.cop4655.isstracker

import retrofit.Call
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query

// This interface defines an API
// service for getting ISS info.
interface VisualPassService {
    // This annotation specifies that the HTTP method
    // is GET and the endpoint URL is "visualpasses".
    @GET("visualpasses/25544/{lat}/{long}}/6/1/0/")
    // This method returns a Call object with a generic
    // type of DataModel, which represents
    // the data model for the response.
    fun getVisualPass(
        @Path("lat") lat: Double,
        @Path("long") long: Double,
        @Query("apiKey") n2yoApiKey: String
    ): Call<VisualPassDataModel>
}