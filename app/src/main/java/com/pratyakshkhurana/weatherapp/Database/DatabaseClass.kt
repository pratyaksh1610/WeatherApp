package com.pratyakshkhurana.weatherapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pratyakshkhurana.weatherapp.Dao.SearchViewHistoryDao
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory

// increase version by 1 when we add other entities in db
@Database(entities = [SearchViewHistory::class], version = 2, exportSchema = false)
abstract class DatabaseClass : RoomDatabase() {
    // to access tables
    abstract fun searchViewHistoryDao(): SearchViewHistoryDao

    companion object {
        @Suppress("ktlint:standard:property-naming")
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DatabaseClass? = null

        // to ensure only one instance of db at time is there
        fun getDatabase(context: Context): DatabaseClass {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseClass::class.java,
                        "all_database",
                    ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
