package com.sn.harbinger.ui.dashboard.home.project.details

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sn.harbinger.data.model.BaseResponse
import com.sn.harbinger.data.model.UpdateProjectRequestModel
import com.sn.harbinger.data.remote.ProjectDataSource
import com.sn.harbinger.di.SharedPreferencesModule
import com.sn.harbinger.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.ResponseBody
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val projectDataSource: ProjectDataSource,
    sharedPreferencesModule: SharedPreferencesModule
) :
    ViewModel() {
    private val token = sharedPreferencesModule.getToken()
    private val _updateProjectLiveData = MutableLiveData<State<BaseResponse<ResponseBody>>>()
    val updateProjectLiveData: LiveData<State<BaseResponse<ResponseBody>>> get() = _updateProjectLiveData
    val isEnabled = ObservableBoolean(false)

    private val now = Calendar.getInstance()
    val currentYear: Int = now.get(Calendar.YEAR)
    val currentMonth: Int = now.get(Calendar.MONTH)
    val currentDay: Int = now.get(Calendar.DAY_OF_MONTH)

    lateinit var startDate: String
    lateinit var endDate: String

    enum class PickerType {
        START, END
    }

    fun updateProject(
        projectID: String,
        projectName: String,
        projectDesc: String,
        startDate: String,
        endDate: String
    ) {
        val request =
            UpdateProjectRequestModel(projectID, projectName, projectDesc, startDate, endDate)

        projectDataSource.updateProjects(token, request)
            .observeOn(AndroidSchedulers.mainThread()).subscribe { response ->
                _updateProjectLiveData.postValue(response)
            }
    }

    companion object {
        var pickerType: PickerType? = null
    }

}