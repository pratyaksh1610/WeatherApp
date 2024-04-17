package com.pratyakshkhurana.weatherapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyakshkhurana.weatherapp.DataClass.CurrentWeather
import com.pratyakshkhurana.weatherapp.DataClass.EveryThreeHourWeatherForecast
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
    private val currentWeatherEveryThreeHour: MutableLiveData<EveryThreeHourWeatherForecast> =
        MutableLiveData()

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

    fun deleteAllRecyclerViewItems() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryClass.deleteAllRecyclerViewItems()
        }
    }

    fun getAllWeatherData(
        city: String,
        key: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryClass.getCurrentWeather(city, key).apply {
                currentWeather.postValue(this.body())
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repositoryClass.getCurrentWeatherEveryThreeHour(city, key).apply {
                currentWeatherEveryThreeHour.postValue(this.body())
            }
        }
    }

    fun getCurrentWeatherLiveData() = currentWeather

    fun getCurrentWeatherEveryThreeHourLiveData() = currentWeatherEveryThreeHour
}
