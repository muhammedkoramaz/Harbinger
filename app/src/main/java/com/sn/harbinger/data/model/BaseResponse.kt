package com.sn.harbinger.data.model

open class BaseResponse<T>(
    val responseCode: Int? = null,
    val responseMessage: String? = null,
    val result: T? = null
)