package com.pratyakshkhurana.weatherapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory
import com.pratyakshkhurana.weatherapp.Repository.RepositoryClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelClass(
    private val repositoryClass: RepositoryClass,
) : ViewModel() {
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
}
