package com.pratyakshkhurana.weatherapp.Repository

import androidx.lifecycle.LiveData
import com.pratyakshkhurana.weatherapp.Api.RetrofitInstance
import com.pratyakshkhurana.weatherapp.DataClass.CurrentWeather
import com.pratyakshkhurana.weatherapp.Database.DatabaseClass
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory
import retrofit2.Response

class RepositoryClass(private val db: DatabaseClass) {
    suspend fun insertSearchViewHistoryItem(search: SearchViewHistory) {
        db.searchViewHistoryDao().insertSearchViewHistoryItem(search)
    }

    suspend fun deleteSearchViewHistoryItem(search: SearchViewHistory) {
        db.searchViewHistoryDao().deleteSearchViewHistoryItem(search)
    }

    fun getSearchViewHistoryItems(): LiveData<List<SearchViewHistory>> {
        return db.searchViewHistoryDao().getSearchViewHistoryItems()
    }

    suspend fun getCurrentWeather(
        city: String,
        key: String,
    ): Response<CurrentWeather> {
        return RetrofitInstance.api.getCurrentWeather(city, key)
    }

    suspend fun isPresent(t: String): Int {
        return db.searchViewHistoryDao().isPresent(t)
    }

    suspend fun deleteAllRecyclerViewItems()  {
        db.searchViewHistoryDao().deleteAllRecyclerViewItems()
    }
}
