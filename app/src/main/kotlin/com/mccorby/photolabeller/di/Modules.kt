package com.mccorby.photolabeller.di

import com.mccorby.photolabeller.config.SharedConfig
import com.mccorby.photolabeller.datasource.network.FederatedService
import com.mccorby.photolabeller.executors.BackgroundExecutionContext
import com.mccorby.photolabeller.executors.UIExecutionContext
import com.mccorby.photolabeller.datasource.filemanager.FileManager
import com.mccorby.photolabeller.labeller.LabellingPresenter
import com.mccorby.photolabeller.model.Trainer
import com.mccorby.photolabeller.repository.FederatedDataSource
import com.mccorby.photolabeller.repository.FederatedRepository
import com.mccorby.photolabeller.trainer.ImageProcessorImpl
import com.mccorby.photolabeller.trainer.TrainingPresenter
import space.traversal.kapsule.HasModules

class Modules(
        android: AndroidModule,
        labelling: LabellingModule,
        training: TrainingModule,
        network: NetworkModule) :
        AndroidModule by android,
        LabellingModule by labelling,
        TrainingModule by training,
        NetworkModule by network,
        HasModules {
    override val modules = setOf(android, labelling, training, network)
}

interface AndroidModule {
    val sharedConfig: SharedConfig
    val fileManager: FileManager
    val executionContext: BackgroundExecutionContext
    val postExecutionContext: UIExecutionContext
    val imageProcessor: ImageProcessorImpl
    val trainer: Trainer
    val dataSource: FederatedDataSource
    val repository: FederatedRepository
}

interface LabellingModule {
    val labellingPresenter: LabellingPresenter
}

interface TrainingModule {
    val trainingPresenter: TrainingPresenter
}

interface NetworkModule {
    val baseUrl: String
    val federatedService: FederatedService
}