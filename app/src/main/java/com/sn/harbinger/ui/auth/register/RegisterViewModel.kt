package com.sn.harbinger.ui.auth.register

import android.util.Patterns
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
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    private val resourcesModule: ResourcesModule
) :
    ViewModel() {

    private val _signupLiveData: MutableLiveData<State<BaseResponse<AuthResponseModel>>> =
        MutableLiveData()
    val signupLiveData: LiveData<State<BaseResponse<AuthResponseModel>>> get() = _signupLiveData


    fun signupRequest(email: String, pass: String) {
        val request = AuthRequestModel(email, pass)
        userDataSource.signup(request).observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                _signupLiveData.postValue(response)
            }
    }

    fun validate(email: String, pass: String): String {
        if (email.isEmpty())
            return resourcesModule.getString(R.string.check_email)
        if (pass.isEmpty())
            return resourcesModule.getString(R.string.check_pass)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return resourcesModule.getString(R.string.check_email_validate)
        if (pass.length < 6)
            return resourcesModule.getString(R.string.check_pass_length)

        return ""
    }

}