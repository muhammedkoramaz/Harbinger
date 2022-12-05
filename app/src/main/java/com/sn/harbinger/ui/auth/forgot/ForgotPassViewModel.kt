package com.sn.harbinger.ui.auth.forgot

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ForgotPassViewModel : ViewModel() {

    private val _emailValidateLiveData = MutableLiveData<Boolean>()
    val emailValidateLiveData: LiveData<Boolean>
        get() = _emailValidateLiveData

    fun sendEmail(email: String) {
        //Todo implement service
    }

    fun validate(email: String) {
        _emailValidateLiveData.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}