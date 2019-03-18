package com.mccorby.photolabeller.di

import android.content.Context
import android.os.Environment
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mccorby.photolabeller.BuildConfig
import com.mccorby.photolabeller.config.SharedConfig
import com.mccorby.photolabeller.datasource.AssetDataSource
import com.mccorby.photolabeller.datasource.NetworkDataSource
import com.mccorby.photolabeller.datasource.filemanager.FileManager
import com.mccorby.photolabeller.datasource.network.FederatedApi
import com.mccorby.photolabeller.datasource.network.FederatedService
import com.mccorby.photolabeller.executors.BackgroundExecutionContext
import com.mccorby.photolabeller.executors.UIExecutionContext
import com.mccorby.photolabeller.interactor.GetModel
import com.mccorby.photolabeller.interactor.LabelImage
import com.mccorby.photolabeller.interactor.Predict
import com.mccorby.photolabeller.interactor.Train
import com.mccorby.photolabeller.labeller.LabellingPresenter
import com.mccorby.photolabeller.repository.CifarRepository
import com.mccorby.photolabeller.trainer.ImageProcessorImpl
import com.mccorby.photolabeller.trainer.TrainerImpl
import com.mccorby.photolabeller.trainer.TrainingPresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import space.traversal.kapsule.Injects
import space.traversal.kapsule.required

class MainAppModule(context: Context, private val network: NetworkModule) : NetworkModule by network, AndroidModule {
    private val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    private val modelFileName = "model.zip"
    override val sharedConfig get() = SharedConfig(32, 3, modelFileName, 16)
    override val fileManager: FileManager
        get() {
            return FileManager(storageDir, sharedConfig)
        }
    override val executionContext get() = BackgroundExecutionContext()
    override val postExecutionContext get() = UIExecutionContext()
    override val imageProcessor get() = ImageProcessorImpl(sharedConfig)
    override val trainer
        get() = TrainerImpl.instance.also {
            it.config = sharedConfig
            it.localDataSource = fileManager
            it.imageProcessor = imageProcessor
        }
    override val dataSource get() = NetworkDataSource(network.federatedService, fileManager)
    private val embeddedDataSource = AssetDataSource(context.assets, storageDir, modelFileName)
    override val repository
        get() = CifarRepository(fileManager, dataSource, embeddedDataSource)
}

// This module implementation depends on AndroidModule (provided by MainAppModule)
class MainLabellingModule : LabellingModule, Injects<AndroidModule> {
    private val executionContext by required { executionContext }
    private val postExecutionContext by required { postExecutionContext }
    private val trainer by required { trainer }
    private val repository by required { repository }
    private val getModelUseCase get() = GetModel(repository, trainer, executionContext, postExecutionContext)
    private val predictUseCase get() = Predict(trainer, executionContext, postExecutionContext)
    private val labelImageUseCase get() = LabelImage(repository, executionContext, postExecutionContext)
    override val labellingPresenter
        get() = LabellingPresenter(
                repository,
                getModelUseCase,
                predictUseCase,
                labelImageUseCase)
}

class MainTrainingModule : TrainingModule, Injects<AndroidModule> {
    private val executionContext by required { executionContext }
    private val postExecutionContext by required { postExecutionContext }
    private val config by required { sharedConfig }
    private val trainer by required { trainer }
    private val repository by required { repository }
    private val trainUseCase get() = Train(repository, trainer, executionContext, postExecutionContext)
    override val trainingPresenter: TrainingPresenter
        get() = TrainingPresenter(trainUseCase, config)
}

class MainNetworkModule : NetworkModule {
    override val baseUrl: String
        get() = BuildConfig.BASE_URL

    override val federatedService: FederatedService
        get() = createApiService()

    private fun createApiService(): FederatedService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        return FederatedService(Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(FederatedApi::class.java))
    }
}

