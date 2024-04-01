package com.pratyakshkhurana.weatherapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pratyakshkhurana.weatherapp.Repository.RepositoryClass

class ViewModelFactoryClass(private val repositoryClass: RepositoryClass) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelClass(repositoryClass) as T
    }
}
