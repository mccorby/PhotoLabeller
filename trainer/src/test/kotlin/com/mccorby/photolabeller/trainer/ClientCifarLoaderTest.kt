package com.mccorby.photolabeller.trainer

import com.mccorby.photolabeller.filemanager.LocalDataSource
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.File

internal class ClientCifarLoaderTest {

    @Mock
    private lateinit var localDataSource: LocalDataSource
    @Mock
    private lateinit var imageProcessor: ImageProcessor

    private lateinit var cut: ClientCifarLoader

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val labels = listOf("airplane", "automobile", "bird", "cat", "deer", "dog", "frog", "horse", "ship", "truck")
        cut = ClientCifarLoader(localDataSource, imageProcessor, labels)
    }

    @Test
    fun `Given a map of labels and files when cut is initialised it has a list of FileData objects`() {
        // Given
        val filesMap = mapOf(
                "cat" to listOf(File("file1"), File("file2")),
                "dog" to listOf(File("file3"), File("file4")))

        whenever(localDataSource.loadTrainingFiles()).thenReturn(filesMap)

        // When/Then

    }

}