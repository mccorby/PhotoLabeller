package com.mccorby.photolabeller.trainer

import com.mccorby.executors.ExecutionContext
import com.mccorby.photolabeller.interactor.Train
import com.mccorby.photolabeller.interactor.TrainParams
import com.mccorby.photolabeller.model.Trainer
import com.mccorby.photolabeller.repository.FederatedRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import java.io.File

internal class TrainTest {

    @Test
    fun `Should update local model and start training and model is saved and sent upstream`() {
        // Given
        val maxSamples = 32
        val epochs = 2
        val repository = mock<FederatedRepository>()
        val trainer = mock<Trainer>()
        val executionContext = mock<ExecutionContext>()
        val postExecutionContext = mock<ExecutionContext>()
        val modelFile = mock<File>()
        val updateAsByteArray = byteArrayOf()

        whenever(executionContext.getContext()).thenReturn(CommonPool)
        whenever(postExecutionContext.getContext()).thenReturn(CommonPool)
        whenever(repository.createModelFile()).thenReturn(modelFile)
        whenever(trainer.saveModel(modelFile)).thenReturn(modelFile)
        whenever(trainer.getUpdateFromLayer()).thenReturn(updateAsByteArray)
        whenever(trainer.getSamplesInTraining()).thenReturn(maxSamples)

        // When
        val cut = Train(repository, trainer, executionContext, postExecutionContext)

        runBlocking { cut.run(TrainParams(maxSamples, epochs)) }

        // Then
        verify(trainer).train(maxSamples, epochs)
        verify(trainer).saveModel(modelFile)
        verify(repository).sendModelUpdate(updateAsByteArray, maxSamples)
    }
}