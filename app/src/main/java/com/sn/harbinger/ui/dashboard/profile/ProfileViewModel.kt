package com.sn.harbinger.ui.dashboard.profile

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sn.harbinger.data.model.BaseResponse
import com.sn.harbinger.data.model.Image
import com.sn.harbinger.data.model.User
import com.sn.harbinger.data.remote.UserDataSource
import com.sn.harbinger.di.SharedPreferencesModule
import com.sn.harbinger.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    val sharedPreferencesModule: SharedPreferencesModule
) : ViewModel() {
    val token = sharedPreferencesModule.getToken()

    private val _userInfoLiveData: MutableLiveData<State<BaseResponse<User>>> = MutableLiveData()
    val userInfoLiveData: LiveData<State<BaseResponse<User>>> get() = _userInfoLiveData

    private val _imageUploadLiveData: MutableLiveData<State<BaseResponse<Image>>> =
        MutableLiveData()
    val imageUploadLiveData: LiveData<State<BaseResponse<Image>>> get() = _imageUploadLiveData

    val noProfileImage = ObservableBoolean(true)

    init {
        getUser()
    }

    fun removeToken() {
        sharedPreferencesModule.removeToken()
    }

    fun setNoImageData(image: String?) {
        if (image.isNullOrEmpty())
            noProfileImage.set(true)
        else
            noProfileImage.set(false)
    }

    private fun getUser() {
        userDataSource.getUser(token).observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                _userInfoLiveData.postValue(response)
            }
    }

    fun addUserImage(base64: Image) {
        userDataSource.addUserImage(token, base64).observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                _imageUploadLiveData.postValue(response)
            }
    }

}