package com.cop4655.isstracker

import java.io.Serializable

// Model class for ISS location
data class DataModel(
    var message: String,
    var timestamp: Long,
    var issPosition: IssPosition
) : Serializable

data class IssPosition(
    var latitude: Double,
    var longitude: Double
) : Serializable