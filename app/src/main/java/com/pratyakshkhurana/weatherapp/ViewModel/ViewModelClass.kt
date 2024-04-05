package com.pratyakshkhurana.weatherapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyakshkhurana.weatherapp.DataClass.CurrentWeather
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory
import com.pratyakshkhurana.weatherapp.Repository.RepositoryClass
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ViewModelClass(
    private val repositoryClass: RepositoryClass,
) : ViewModel() {
    private val currentWeather: MutableLiveData<CurrentWeather> = MutableLiveData()

    fun insertSearchViewHistoryItem(search: SearchViewHistory) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryClass.insertSearchViewHistoryItem(search)
        }
    }

    fun deleteSearchViewHistoryItem(search: SearchViewHistory) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryClass.deleteSearchViewHistoryItem(search)
        }
    }

    fun getSearchViewHistoryItems(): LiveData<List<SearchViewHistory>> {
        return repositoryClass.getSearchViewHistoryItems()
    }

    fun getCurrentWeather(
        city: String,
        key: String,
    ): MutableLiveData<CurrentWeather> {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repositoryClass.getCurrentWeather(city, key)
            currentWeather.postValue(response.body())
        }
        return currentWeather
    }

    fun isPresent(t: String): Int {
        val job: Deferred<Int> =
            viewModelScope.async(Dispatchers.IO) {
                repositoryClass.isPresent(t)
            }
        val res =
            runBlocking {
                job.await()
            }
        return res
    }
}
