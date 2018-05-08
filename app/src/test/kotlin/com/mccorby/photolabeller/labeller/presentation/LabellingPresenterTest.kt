package com.mccorby.photolabeller.labeller.presentation

import com.mccorby.photolabeller.interactor.GetModel
import com.mccorby.photolabeller.interactor.LabelImage
import com.mccorby.photolabeller.interactor.Predict
import com.mccorby.photolabeller.labeller.LabellingPresenter
import com.mccorby.photolabeller.labeller.LabellingView
import com.mccorby.photolabeller.repository.FederatedRepository
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class LabellingPresenterTest {

    private lateinit var cut: LabellingPresenter

    @Mock
    private lateinit var view: LabellingView

    @Mock
    private lateinit var repository: FederatedRepository

    @Mock
    private lateinit var predictUseCase: Predict

    @Mock
    private lateinit var labelUseCase: LabelImage

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        cut = LabellingPresenter(repository, predictUseCase, labelUseCase)
        cut.attach(view)
    }

//    @Test
//    fun `Given a photo is taken when label is applied photo is saved in label directory`() {
//        // Given
//        val file = mock<File>()
//        val photoPath = "photoPath"
//        val label = "photoLabel"
//        val params = LabelImageParams(photoPath, label)
//        runBlocking { whenever(labelUseCase.run(params)).thenReturn(file) }
//
//        // When
//        cut.saveLabelledImage(photoPath, label)
//
//        // Then
//        verify(labelUseCase).execute({}, params)
//    }

    // TODO This test belongs to the use case
/*    @Test
    fun `Given a model is not yet loaded When load model is invoked the trainer loads the model`() = runBlocking {
        // Given
        val file = mock<File>()
        val stats = Stats("")
        whenever(localDataSource.loadModelFile()).thenReturn(file)
        whenever(trainer.loadModel(file)).thenReturn(stats)
        whenever(trainer.isModelLoaded()).thenReturn(false)

        // When
        cut.loadModel()

        // Then
        verify(trainer).loadModel(file)
        view.onModelLoaded(stats)
    }*/
}