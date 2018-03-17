package com.mccorby.photolabeller.datasource

import com.mccorby.fp.Either
import com.mccorby.photolabeller.filemanager.LocalDataSource
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.File
import java.io.FileNotFoundException

internal class CifarRepositoryTest {

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Mock
    private lateinit var remoteDataSource: FederatedDataSource

    @Mock
    private lateinit var embeddedDataSource: EmbeddedDataSource

    private lateinit var cut: CifarRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        cut = CifarRepository(localDataSource, remoteDataSource, embeddedDataSource)
    }

    @Test
    fun `Given a local model when the repository requests it then no network or embedded file is returned`() {
        // Given

        val file = mock<File>()
        whenever(localDataSource.loadModelFile()).thenReturn(Either.Right(file))

        // When
        cut.openModel()

        // Then
        verify(localDataSource).loadModelFile()
        verifyZeroInteractions(remoteDataSource)
        verifyZeroInteractions(embeddedDataSource)
    }

    @Test
    fun `Given no local model when the repository requests it then a remote model is retrieved`() {
        // Given
        val file = mock<File>()
        whenever(localDataSource.loadModelFile()).thenReturn(Either.Left(FileNotFoundException()))
        whenever(remoteDataSource.updateModel()).thenReturn(Either.Right(file))

        // When
        cut.openModel()

        // Then
        verify(localDataSource).loadModelFile()
        verify(remoteDataSource).updateModel()
        verifyZeroInteractions(embeddedDataSource)
    }

    @Test
    fun `Given no local model and no remote model when the repository requests it then the embedded model is returned`() {
        // Given
        val file = mock<File>()
        whenever(localDataSource.loadModelFile()).thenReturn(Either.Left(FileNotFoundException()))
        whenever(remoteDataSource.updateModel()).thenReturn(Either.Left(FileNotFoundException()))
        whenever(embeddedDataSource.getModel()).thenReturn(file)

        // When
        cut.openModel()

        // Then
        verify(localDataSource).loadModelFile()
        verify(remoteDataSource).updateModel()
        verify(embeddedDataSource).getModel()
    }
}