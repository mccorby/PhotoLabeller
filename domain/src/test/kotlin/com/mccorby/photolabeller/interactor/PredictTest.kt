package com.mccorby.photolabeller.interactor

import com.mccorby.executors.ExecutionContext
import com.mccorby.photolabeller.model.Trainer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.File

internal class PredictTest {

    @Test
    fun `Should produce a prediction from a file`() {
        // Given
        val trainer = mock<Trainer>()
        val imageFile = mock<File>()

        val executionContext = mock<ExecutionContext>()
        val postExecutionContext = mock<ExecutionContext>()

        whenever(executionContext.getContext()).thenReturn(Dispatchers.Default)
        whenever(postExecutionContext.getContext()).thenReturn(Dispatchers.Default)

        // When
        val cut = Predict(trainer, executionContext, postExecutionContext)
        runBlocking { cut.run(PredictParams(imageFile)) }

        // Then
        verify(trainer).predict(imageFile)
    }
}