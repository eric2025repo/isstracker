package com.cop4655.isstracker

import android.content.Context
import android.widget.Toast
import retrofit.*
import kotlin.jvm.java

class ExpeditionCall {
    // This function takes a Context and callback function
    // as a parameter, which will be called
    // when the API response is received.
    fun getExpedition(context: Context, callback: (ExpeditionDataModel) -> Unit) {

        // Create a Retrofit instance with the base URL and
        // a GsonConverterFactory for parsing the response.
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://corquaid.github.io/international-space-station-APIs/JSON/").addConverterFactory(
            GsonConverterFactory.create()).build()

        // Create an ExpeditionService instance from the Retrofit instance.
        val service: ExpeditionService = retrofit.create<ExpeditionService>(ExpeditionService::class.java)

        // Call the getExpedition() method of the ExpeditionService
        // to make an API request.
        val call: Call<ExpeditionDataModel> = service.getExpedition()

        // Use the enqueue() method of the Call object to
        // make an asynchronous API request.
        call.enqueue(object : Callback<ExpeditionDataModel> {
            // This is an anonymous inner class that implements the Callback interface.
            override fun onResponse(response: Response<ExpeditionDataModel>?, retrofit: Retrofit?) {
                // This method is called when the API response is received successfully.

                if(response!!.isSuccess){
                    // If the response is successful, parse the
                    // response body to a DataModel object.
                    val expedition: ExpeditionDataModel = response.body() as ExpeditionDataModel

                    // Call the callback function with the ExpeditionDataModel
                    // object as a parameter.
                    callback(expedition)
                }
            }

            override fun onFailure(t: Throwable?) {
                // This method is called when the API request fails.
                Toast.makeText(context, "Request Fail", Toast.LENGTH_SHORT).show()
            }
        })
    }
}