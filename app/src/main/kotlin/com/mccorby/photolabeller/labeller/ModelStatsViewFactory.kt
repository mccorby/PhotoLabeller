package com.mccorby.photolabeller.labeller

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mccorby.photolabeller.interactor.GetModel

class ModelStatsViewFactory(private val getModelUseCase: GetModel): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ModelStatsViewModel(getModelUseCase) as T
    }
}