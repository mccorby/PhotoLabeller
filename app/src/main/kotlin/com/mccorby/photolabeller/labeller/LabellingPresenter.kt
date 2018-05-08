package com.mccorby.photolabeller.labeller

import com.mccorby.photolabeller.interactor.LabelImage
import com.mccorby.photolabeller.interactor.LabelImageParams
import com.mccorby.photolabeller.interactor.Predict
import com.mccorby.photolabeller.interactor.PredictParams
import com.mccorby.photolabeller.repository.FederatedRepository
import java.io.File

class LabellingPresenter(private val repository: FederatedRepository,
                         private val predict: Predict,
                         private val labelImage: LabelImage) {

    private var view: LabellingView? = null


    fun saveLabelledImage(photoPath: String, label: String) = labelImage.execute({}, LabelImageParams(photoPath, label))

    fun createImageFile(): File = repository.createImage()

    fun predict(image: File) = predict.execute({view?.onPrediction(it)}, PredictParams(image))

    fun attach(view: LabellingView) { this.view = view }

    fun detach() { view = null }
}

