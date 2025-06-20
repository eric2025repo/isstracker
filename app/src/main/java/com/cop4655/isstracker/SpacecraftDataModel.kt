package com.cop4655.isstracker

import java.io.Serializable

// Model class for ISS location
data class SpacecraftDataModel(
    var number: Int,
    val spacecraft: List<Spacecraft>
) : Serializable

data class Spacecraft(
    var id: Int,
    var name: String,
    var country: String,
    var flag_code: String,
    var operator: String,
    var manufacturer: String,
    var spacecraft_name: String,
    var launched: Long,
    var launch_site: String,
    var iss: Boolean,
    var docked: Long,
    var docking_port: String,
    var launch_mass: Double,
    var payload_mass: Double,
    var launch_vehicle: String,
    var launch_vehicle_name: String,
    var mission_type: String,
    var crew: List<String>,
    var mission_patch: String,
    var url: String,
    var image: String
) : Serializable
