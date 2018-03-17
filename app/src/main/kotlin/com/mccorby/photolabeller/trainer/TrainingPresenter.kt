package com.mccorby.photolabeller.trainer

import com.mccorby.photolabeller.config.SharedConfig
import com.mccorby.photolabeller.interactor.Train
import com.mccorby.photolabeller.interactor.TrainParams

class TrainingPresenter(private val train: Train, private val config: SharedConfig) {

    private var view: TrainingView? = null

    fun train() {
        view?.onStartTraining(config)
        train.execute({view?.showTrainingStats(it)}, TrainParams(config.maxSamples, config.maxEpochs))
    }

    fun attach(view: TrainingView) {
        this.view = view
    }
}