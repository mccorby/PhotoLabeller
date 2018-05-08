package com.mccorby.photolabeller.labeller

import com.mccorby.photolabeller.interactor.GetModel
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ModelStatsViewModelTest {

    @Mock
    private lateinit var getModel: GetModel

    private lateinit var cut: ModelStatsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        cut = ModelStatsViewModel(getModel)
    }

    @Test
    fun `When model is loaded then the use case is executed`() {
        // When
        cut.loadModel()

        // Then
        verify(getModel).execute(any(), any())
    }

//    @Test
//    fun `When model is loaded then the live data is updated`() = runBlocking {
//        // Given
//        val expected = Stats("any")
//        val dataBefore = cut.getModelStatusData().value
//
//        whenever(getModel.run(NoParams)).thenReturn(expected)
//
//        // When
//        cut.loadModel()
//
//        // Then
//        val dataAfter = cut.getModelStatusData().value
//        assertNotEquals(dataBefore, dataAfter)
//    }
}