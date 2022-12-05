package com.sn.harbinger.data.remote

import com.sn.harbinger.data.model.*
import com.sn.harbinger.util.State
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(private val userService: UserService) {
    fun login(requestModel: AuthRequestModel): Observable<State<BaseResponse<AuthResponseModel>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            userService.login(requestModel).subscribeOn(Schedulers.io()).subscribe(
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

    fun signup(requestModel: AuthRequestModel): Observable<State<BaseResponse<AuthResponseModel>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            userService.signup(requestModel).subscribeOn(Schedulers.io()).subscribe(
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

    fun getUser(token: String): Observable<State<BaseResponse<User>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            userService.getUser(token).subscribeOn(Schedulers.io()).subscribe(
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

    fun addUserImage(
        token: String,
        base64: Image
    ): Observable<State<BaseResponse<Image>>> =
        Observable.create { emitter ->
            emitter.onNext(State.Loading)
            userService.addUserImage(token, base64).subscribeOn(Schedulers.io()).subscribe(
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