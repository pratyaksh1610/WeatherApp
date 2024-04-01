package com.pratyakshkhurana.weatherapp.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory

@Dao
interface SearchViewHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchViewHistoryItem(search: SearchViewHistory)

    @Delete
    suspend fun deleteSearchViewHistoryItem(search: SearchViewHistory)

    @Query("SELECT * FROM search_view_history_items")
    fun getSearchViewHistoryItems(): LiveData<List<SearchViewHistory>>
}
