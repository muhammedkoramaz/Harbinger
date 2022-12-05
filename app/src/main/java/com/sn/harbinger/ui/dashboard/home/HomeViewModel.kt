package com.sn.harbinger.ui.dashboard.home

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sn.harbinger.data.model.BaseResponse
import com.sn.harbinger.data.model.Project
import com.sn.harbinger.data.model.ProjectList
import com.sn.harbinger.data.remote.ProjectDataSource
import com.sn.harbinger.di.SharedPreferencesModule

import com.sn.harbinger.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val projectDataSource: ProjectDataSource,
    private val sharedPreferencesModule: SharedPreferencesModule
) :
    ViewModel() {

    private val token = sharedPreferencesModule.getToken()
    var myProjectsError = ObservableBoolean(false)
    var connectedProjectsError = ObservableBoolean(false)

    private val _myProjectsLiveData: MutableLiveData<State<BaseResponse<ProjectList>>> =
        MutableLiveData()
    val myProjectsLiveData: LiveData<State<BaseResponse<ProjectList>>> get() = _myProjectsLiveData

    private val _connectedProjectsLiveData: MutableLiveData<State<BaseResponse<ProjectList>>> =
        MutableLiveData()
    val connectedProjectsLiveData: LiveData<State<BaseResponse<ProjectList>>> get() = _connectedProjectsLiveData

    private val _deletedProjectsLiveData: MutableLiveData<State<BaseResponse<ResponseBody>>> =
        MutableLiveData()
    val deletedProjectsLiveData: LiveData<State<BaseResponse<ResponseBody>>> get() = _deletedProjectsLiveData

    fun projectsRequests() {
        getMyProjects()
        getConnectedProjects()
    }

    private fun getMyProjects() {
        projectDataSource.getMyProjects(token).observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                _myProjectsLiveData.postValue(response)
            }
    }

    private fun getConnectedProjects() {
        projectDataSource.getConnectedProjects(token).observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                _connectedProjectsLiveData.postValue(response)
            }
    }

    fun deleteProject(projectID: String) {
        projectDataSource.deleteProjects(token, projectID).observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                _deletedProjectsLiveData.postValue(response)
            }
    }


    fun removeToken() {
        sharedPreferencesModule.removeToken()
    }

    fun returnFullList(list: List<Project>?): List<Any> {
        val fullList = mutableListOf<Any>()
        fullList.addAll(list!!)
        fullList.add(0, "header")
        fullList.add(fullList.lastIndex + 1, "footer")
        return fullList
    }

}