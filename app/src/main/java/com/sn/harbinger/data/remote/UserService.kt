package com.sn.harbinger.data.remote

import com.sn.harbinger.data.model.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface UserService {

    @POST("/user/login")
    fun login(@Body request: AuthRequestModel): Single<BaseResponse<AuthResponseModel>>

    @POST("/user/signup")
    fun signup(@Body request: AuthRequestModel): Single<BaseResponse<AuthResponseModel>>

    @GET("/user/me")
    fun getUser(@Header("token") token: String): Single<BaseResponse<User>>

    @PUT("/user/addImage")
    fun addUserImage(
        @Header("token") token: String,
        @Body request: Image
    ): Single<BaseResponse<Image>>

}
