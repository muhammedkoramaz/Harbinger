package com.sn.harbinger.data.remote

import com.sn.harbinger.data.model.*
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface ProjectService {

    @GET("/project/myProjects")
    fun getMyProjects(@Header("token") token: String): Single<BaseResponse<ProjectList>>

    @GET("/project/connectedProjects")
    fun getConnectedProjects(@Header("token") token: String): Single<BaseResponse<ProjectList>>

    @POST("/project/add")
    fun addProject(
        @Header("token") token: String,
        @Body request: AddProjectRequestModel
    ): Single<BaseResponse<Project>>

    @DELETE("/project/deleteProject")
    fun deleteProject(
        @Header("token") token: String,
        @Query("projectID") projectID: String
    ): Single<BaseResponse<ResponseBody>>

    @PUT("/project/update")
    fun updateProject(
        @Header("token") token: String,
        @Body requestModel: UpdateProjectRequestModel
    ): Single<BaseResponse<ResponseBody>>

}