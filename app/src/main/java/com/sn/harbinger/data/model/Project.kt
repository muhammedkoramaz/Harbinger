package com.sn.harbinger.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class ProjectList(var projects: List<Project>)

@Parcelize
data class Project(
    val startDate: String? = null,
    val endDate: String? = null,
    val users: List<String>? = null,
    val projectID: String? = null,
    val projectOwner: String? = null,
    val projectName: String? = null,
    val projectDesc: String? = null,
    val projectProgress: String? = null,
) : Parcelable

