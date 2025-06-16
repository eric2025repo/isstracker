package com.cop4655.isstracker

import java.io.Serializable

// Model class for ISS location
data class DataModel(
    var message: String,
    var timestamp: Long,
    var iss_position: Iss_position
) : Serializable

data class Iss_position(
    var latitude: Double,
    var longitude: Double
) : Serializable