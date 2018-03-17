package com.mccorby.photolabeller.datasource.network

import com.mccorby.photolabeller.model.TrainingRound
import kotlinx.coroutines.experimental.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface FederatedApi {

    companion object {
        private const val MODEL = "model"
        private const val CURRENT_ROUND = "currentRound"
    }

    @Streaming
    @GET(MODEL)
    fun getModel(): Deferred<ResponseBody>

    @Multipart
    @POST(MODEL)
    fun sendUpdate(@Part file: MultipartBody.Part, @Part("samples") samples: RequestBody): Deferred<Boolean>

    @GET(CURRENT_ROUND)
    fun getCurrentTrainingRound(): Deferred<TrainingRound>
}