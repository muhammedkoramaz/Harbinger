package com.sn.harbinger.data.remote

import com.sn.harbinger.data.model.AddNoteRequestModel
import com.sn.harbinger.data.model.BaseResponse
import com.sn.harbinger.data.model.Note
import com.sn.harbinger.data.model.NoteList
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface NoteService {

    @POST("/note/addNote")
    fun addNote(
        @Header("token") token: String,
        @Body request: AddNoteRequestModel
    ): Single<BaseResponse<Note>>

    @GET("/note/notes")
    fun getNote(
        @Header("token") token: String,
        @Query("projectID") projectID: String
    ): Single<BaseResponse<NoteList>>


}