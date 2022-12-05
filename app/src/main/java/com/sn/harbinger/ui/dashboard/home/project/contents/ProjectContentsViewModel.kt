package com.sn.harbinger.ui.dashboard.home.project.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sn.harbinger.data.model.BaseResponse
import com.sn.harbinger.data.model.Note
import com.sn.harbinger.data.model.NoteList
import com.sn.harbinger.data.remote.NoteDataSource
import com.sn.harbinger.di.SharedPreferencesModule
import com.sn.harbinger.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class ProjectContentsViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    sharedPreferencesModule: SharedPreferencesModule
) : ViewModel() {
    private val token = sharedPreferencesModule.getToken()

    private val _getNoteLiveData: MutableLiveData<State<BaseResponse<NoteList>>> =
        MutableLiveData()
    val getNoteLiveDate: LiveData<State<BaseResponse<NoteList>>> get() = _getNoteLiveData

    fun getNote(projectID: String) {
        noteDataSource.getNotes(token, projectID).observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                _getNoteLiveData.postValue(response)
            }
    }

    fun calculateTaskSize(list: List<Note>): String {
        var remainingTask = 0
        list.forEach {
            if (!it.completed) remainingTask++
        }
        return remainingTask.toString()
    }


}