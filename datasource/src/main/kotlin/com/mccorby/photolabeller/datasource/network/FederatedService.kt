package com.mccorby.photolabeller.datasource.network

import com.mccorby.photolabeller.model.TrainingRound
import kotlinx.coroutines.experimental.Deferred
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class FederatedService(private val federatedApi: FederatedApi) {

    fun sendUpdate(update: ByteArray, samples: Int): Deferred<Boolean>  {
        val reqFile = RequestBody.create(MediaType.parse("/*"), update)
        val requestBody = MultipartBody.Part.createFormData("file", "update", reqFile)
        val samplesField = RequestBody.create(MediaType.parse("text/plain"), samples.toString())

        return federatedApi.sendUpdate(requestBody, samplesField)
    }

    fun getModel(): Deferred<ResponseBody> = federatedApi.getModel()

    fun getCurrentTrainingRound(): Deferred<TrainingRound> = federatedApi.getCurrentTrainingRound()
}