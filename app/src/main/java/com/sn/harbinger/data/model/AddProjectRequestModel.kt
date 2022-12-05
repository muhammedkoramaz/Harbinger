package com.sn.harbinger.data.model

data class AddProjectRequestModel(
    val projectName: String,
    val projectDesc: String,
    val projectProgress: String,
    val startDate: String,
    val endDate: String
)
