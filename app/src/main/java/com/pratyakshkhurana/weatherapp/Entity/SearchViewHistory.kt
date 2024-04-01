package com.pratyakshkhurana.weatherapp.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_view_history_items")
data class SearchViewHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val history: String,
)
