package com.sn.harbinger.data.remote

import com.sn.harbinger.data.model.*
import com.sn.harbinger.util.State
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDataSource @Inject constructor(private val noteService: NoteService) {

    fun addNote(
        token: String,
        requestModel: AddNoteRequestModel
    ): Observable<State<BaseResponse<Note>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            noteService.addNote(token, requestModel).subscribeOn(Schedulers.io()).subscribe(
                { response ->
                    emitter.onNext(State.Success(response))
                    emitter.onComplete()
                },
                { throwable ->
                    emitter.onNext(State.Error(throwable))
                    emitter.onComplete()
                }
            )
        }

    fun getNotes(
        token: String,
        projectID: String
    ): Observable<State<BaseResponse<NoteList>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            noteService.getNote(token, projectID).subscribeOn(Schedulers.io()).subscribe(
                { response ->
                    emitter.onNext(State.Success(response))
                    emitter.onComplete()
                },
                { throwable ->
                    emitter.onNext(State.Error(throwable))
                    emitter.onComplete()
                }
            )
        }


}