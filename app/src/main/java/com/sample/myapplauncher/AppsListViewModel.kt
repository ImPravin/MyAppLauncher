package com.sample.myapplauncher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.appsmodule.AppInfo
import com.sample.appsmodule.AppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppsListViewModel(private val appsRepository: AppsRepository) : ViewModel() {

    private val _appsList: MutableLiveData<List<AppInfo>> = MutableLiveData()
    val appsList: LiveData<List<AppInfo>> = _appsList

    private val _showLoader: MutableLiveData<Boolean> = MutableLiveData()
    val showLoader: LiveData<Boolean> get() = _showLoader

    fun fetchAppsList(){
        viewModelScope.launch(Dispatchers.Default) {
            // Coroutine that will be canceled when the ViewModel is cleared.
            _appsList.postValue(appsRepository.getInstalledAppList())
        }
    }

    fun showLoader() {
        _showLoader.value = true
    }

    fun hideLoader() {
        _showLoader.value = false
    }
}