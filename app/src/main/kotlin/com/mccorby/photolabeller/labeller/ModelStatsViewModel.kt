package com.mccorby.photolabeller.labeller

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mccorby.photolabeller.interactor.GetModel
import com.mccorby.photolabeller.interactor.NoParams
import com.mccorby.photolabeller.model.Stats

class ModelStatsViewModel(private val getModel: GetModel) : ViewModel() {

    private val modelStatusData = MutableLiveData<Stats>()
    private var modelA = Stats("")

    fun loadModel() = getModel.execute({ modelA = it }, NoParams)

    fun getModelStatusData(): LiveData<Stats>  {
        modelStatusData.value = modelA
        return modelStatusData
    }

}