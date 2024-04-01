package com.pratyakshkhurana.weatherapp.Repository

import androidx.lifecycle.LiveData
import com.pratyakshkhurana.weatherapp.Database.DatabaseClass
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory

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
}
