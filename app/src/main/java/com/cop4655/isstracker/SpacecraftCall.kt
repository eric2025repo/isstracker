package com.cop4655.isstracker

import android.content.Context
import android.widget.Toast
import retrofit.*
import kotlin.jvm.java

class SpacecraftCall {
    // This function takes a Context and callback function
    // as a parameter, which will be called
    // when the API response is received.
    fun getSpacecraft(context: Context, callback: (SpacecraftDataModel) -> Unit) {

        // Create a Retrofit instance with the base URL and
        // a GsonConverterFactory for parsing the response.
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://corquaid.github.io/international-space-station-APIs/JSON/").addConverterFactory(
            GsonConverterFactory.create()).build()

        // Create an SpacecraftService instance from the Retrofit instance.
        val service: SpacecraftService = retrofit.create<SpacecraftService>(SpacecraftService::class.java)

        // Call the getSpacecraft() method of the SpacecraftService
        // to make an API request.
        val call: Call<SpacecraftDataModel> = service.getSpacecraft()

        // Use the enqueue() method of the Call object to
        // make an asynchronous API request.
        call.enqueue(object : Callback<SpacecraftDataModel> {
            // This is an anonymous inner class that implements the Callback interface.
            override fun onResponse(response: Response<SpacecraftDataModel>?, retrofit: Retrofit?) {
                // This method is called when the API response is received successfully.

                if(response!!.isSuccess){
                    // If the response is successful, parse the
                    // response body to a DataModel object.
                    val spacecraft: SpacecraftDataModel = response.body() as SpacecraftDataModel

                    // Call the callback function with the SpacecraftDataModel
                    // object as a parameter.
                    callback(spacecraft)
                }
            }

            override fun onFailure(t: Throwable?) {
                // This method is called when the API request fails.
                Toast.makeText(context, "Request Fail", Toast.LENGTH_SHORT).show()
            }
        })
    }
}