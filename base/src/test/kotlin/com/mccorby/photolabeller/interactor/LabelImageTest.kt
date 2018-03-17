package com.mccorby.photolabeller.interactor

import com.mccorby.executors.ExecutionContext
import com.mccorby.photolabeller.repository.FederatedRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

internal class LabelImageTest {

    @Test
    fun `Given a file and a label when the use case is executed the file is moved to the label folder`() {
        // Given
        val imageFile = "aPath"
        val label = "aLabel"
        val params = LabelImageParams(imageFile, label)
        val repository = mock<FederatedRepository>()
        val executionContext = mock<ExecutionContext>()
        val postExecutionContext = mock<ExecutionContext>()

        whenever(executionContext.getContext()).thenReturn(CommonPool)
        whenever(postExecutionContext.getContext()).thenReturn(CommonPool)
        
        // When
        val cut = LabelImage(repository, executionContext, postExecutionContext)
        runBlocking { cut.run(params) }
        
        // Then
        verify(repository).saveLabelImage(imageFile, label)
    }
}