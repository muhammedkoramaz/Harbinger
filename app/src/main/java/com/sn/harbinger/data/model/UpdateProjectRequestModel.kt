package com.sn.harbinger.data.model

data class UpdateProjectRequestModel(
    val projectID: String,
    val projectName: String,
    val projectDesc: String,
    val startDate: String,
    val endDate: String
)
