package com.mccorby.photolabeller.interactor

import com.mccorby.executors.ExecutionContext
import com.mccorby.photolabeller.model.Trainer
import com.mccorby.photolabeller.repository.FederatedRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import java.io.File

internal class GetModelTest {

    @Test
    fun `Should load model from local data source`() {
        // Given
        val modelFile = mock<File>()
        val repository = mock<FederatedRepository>()
        val trainer = mock<Trainer>()
        val executionContext = mock<ExecutionContext>()
        val postExecutionContext = mock<ExecutionContext>()

        whenever(executionContext.getContext()).thenReturn(CommonPool)
        whenever(postExecutionContext.getContext()).thenReturn(CommonPool)
        whenever(repository.openModel()).thenReturn(modelFile)

        // When
        val cut = GetModel(repository, trainer, executionContext, postExecutionContext)
        runBlocking { cut.run(NoParams()) }

        // Then
        verify(repository).openModel()
        verify(trainer).loadModel(modelFile)
    }
}