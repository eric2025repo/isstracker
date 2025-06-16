package com.cop4655.isstracker

import java.io.Serializable

// Model class for ISS location
data class ExpeditionDataModel(
    val number: Int,
    val iss_expedition: Int,
    val expedition_patch: String,
    val expedition_url: String,
    val expedition_image: String,
    val expedition_start_date: String,
    val expedition_end_date: String,
    val people: List<Crew>
) : Serializable

data class Crew(
    val id: Int,
    val name: String,
    val country: String,
    val flag_code: String,
    val agency: String,
    val position: String,
    val spacecraft: String,
    val launched: String,
    val iss: Boolean,
    val days_in_space: Int,
    val url: String,
    val image: String,
    val instagram: String,
    val twitter: String,
    val facebook: String
) : Serializable
