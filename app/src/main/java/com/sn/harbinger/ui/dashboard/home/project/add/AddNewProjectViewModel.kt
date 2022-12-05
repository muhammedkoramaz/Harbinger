package com.sn.harbinger.ui.dashboard.home.project.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sn.harbinger.data.model.AddProjectRequestModel
import com.sn.harbinger.data.model.BaseResponse
import com.sn.harbinger.data.model.Project
import com.sn.harbinger.data.remote.ProjectDataSource
import com.sn.harbinger.di.SharedPreferencesModule
import com.sn.harbinger.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class AddNewProjectViewModel @Inject constructor(
    private val projectDataSource: ProjectDataSource,
    sharedPreferencesModule: SharedPreferencesModule
) :
    ViewModel() {
    private val token = sharedPreferencesModule.getToken()
    private val _addProjectLiveData = MutableLiveData<State<BaseResponse<Project>>>()
    val addProjectLiveData: LiveData<State<BaseResponse<Project>>> get() = _addProjectLiveData

    fun addProject(projectName: String, projectDesc: String, startDate: String, endDate: String) {
        val requestModel = AddProjectRequestModel(projectName, projectDesc, "0", startDate, endDate)
        projectDataSource.addProjects(token, requestModel).observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                _addProjectLiveData.postValue(response)
            }
    }

}