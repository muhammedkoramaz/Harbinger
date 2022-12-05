package com.sn.harbinger.data.remote

import com.sn.harbinger.data.model.*
import com.sn.harbinger.util.State
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProjectDataSource @Inject constructor(private val projectService: ProjectService) {

    fun getMyProjects(
        token: String,
    ): Observable<State<BaseResponse<ProjectList>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            projectService.getMyProjects(token).subscribeOn(Schedulers.io()).subscribe(
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

    fun getConnectedProjects(
        token: String,
    ): Observable<State<BaseResponse<ProjectList>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            projectService.getConnectedProjects(token).subscribeOn(Schedulers.io()).subscribe(
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

    fun addProjects(
        token: String,
        requestModel: AddProjectRequestModel
    ): Observable<State<BaseResponse<Project>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            projectService.addProject(token, requestModel).subscribeOn(Schedulers.io()).subscribe(
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

    fun deleteProjects(
        token: String,
        projectID: String
    ): Observable<State<BaseResponse<ResponseBody>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            projectService.deleteProject(token, projectID).subscribeOn(Schedulers.io()).subscribe(
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

    fun updateProjects(
        token: String,
        requestModel: UpdateProjectRequestModel
    ): Observable<State<BaseResponse<ResponseBody>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            projectService.updateProject(token, requestModel).subscribeOn(Schedulers.io())
                .subscribe(
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