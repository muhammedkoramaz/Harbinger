package com.sn.harbinger.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sn.harbinger.R
import com.sn.harbinger.util.State
import com.sn.harbinger.data.model.AuthRequestModel
import com.sn.harbinger.data.model.AuthResponseModel
import com.sn.harbinger.data.model.BaseResponse
import com.sn.harbinger.data.remote.UserDataSource
import com.sn.harbinger.di.ResourcesModule
import com.sn.harbinger.di.SharedPreferencesModule
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    private val sharedPreferencesModule: SharedPreferencesModule,
    private val resourcesModule: ResourcesModule
) :
    ViewModel() {

    private val _loginLiveData: MutableLiveData<State<BaseResponse<AuthResponseModel>>> =
        MutableLiveData()
    val loginLiveData: LiveData<State<BaseResponse<AuthResponseModel>>> get() = _loginLiveData

    fun loginRequest(email: String, pass: String) {
        val request = AuthRequestModel(email, pass)
        userDataSource.login(request).observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                _loginLiveData.postValue(response)
            }
    }

    fun setToken(token: String) {
        sharedPreferencesModule.setToken(token)
    }

    fun validate(email: String, pass: String): String {
        if (email.isEmpty())
            return resourcesModule.getString(R.string.check_email)
        if (pass.isEmpty())
            return resourcesModule.getString(R.string.check_pass)

        return ""
    }

}