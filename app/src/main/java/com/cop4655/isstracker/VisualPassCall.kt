package com.cop4655.isstracker

import android.content.Context
import android.widget.Toast
import retrofit.*
import kotlin.jvm.java

class VisualPassCall {
    // This function takes a Context and callback function
    // as a parameter, which will be called
    // when the API response is received.
    fun getVisualPass(context: Context, callback: (VisualPassDataModel) -> Unit) {

        // Create a Retrofit instance with the base URL and
        // a GsonConverterFactory for parsing the response.
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://api.n2yo.com/rest/v1/satellite/").addConverterFactory(
            GsonConverterFactory.create()).build()

        // Create an VisualPassService instance from the Retrofit instance.
        val service: VisualPassService = retrofit.create<VisualPassService>(VisualPassService::class.java)

        // Call the getVisualPass() method of the VisualPassService
        // to make an API request.
        val call: Call<VisualPassDataModel> = service.getVisualPass()

        // Use the enqueue() method of the Call object to
        // make an asynchronous API request.
        call.enqueue(object : Callback<VisualPassDataModel> {
            // This is an anonymous inner class that implements the Callback interface.
            override fun onResponse(response: Response<VisualPassDataModel>?, retrofit: Retrofit?) {
                // This method is called when the API response is received successfully.

                if(response!!.isSuccess){
                    // If the response is successful, parse the
                    // response body to a DataModel object.
                    val visualPass: VisualPassDataModel = response.body() as VisualPassDataModel

                    // Call the callback function with the VisualPassDataModel
                    // object as a parameter.
                    callback(visualPass)
                }
            }

            override fun onFailure(t: Throwable?) {
                // This method is called when the API request fails.
                Toast.makeText(context, "Request Fail", Toast.LENGTH_SHORT).show()
            }
        })
    }
}