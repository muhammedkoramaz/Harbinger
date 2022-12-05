package com.sn.harbinger.data.model

class AddNoteRequestModel(
    val projectID: String,
    val title: String,
    val description: String,
    val endDate: String,
    val priority: String,
    val completed: Boolean
)