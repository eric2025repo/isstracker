package com.cop4655.isstracker

import java.io.Serializable

// Model class for ISS location
data class VisualPassDataModel(
    val info: Info,
    val passes: List<Pass>
) : Serializable

data class Info (
    val satid: Long,
    val satname: String,
    val transactionscount: Int,
    val passescount: Int,
)

data class Pass(
    val startAz: Double,
    val startAzCompass: String,
    val startEl: Double,
    val startUTC: Long,
    val maxAz: Double,
    val maxAzCompass: String,
    val maxEl: Double,
    val maxUTC: Long,
    val endAz: Double,
    val endAzCompass: String,
    val endEl: Double,
    val endUTC: Long,
    val mag: Double,
    val duration: Int,
    val startVisibility: Long
) : Serializable
