package com.pratyakshkhurana.weatherapp.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// ,indices = {@Index(value = {"history"},
//    unique = true)

@Entity(tableName = "search_view_history_items")
data class SearchViewHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 1,
    val history: String,
)
