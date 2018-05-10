package com.mccorby.photolabeller.trainer

import com.mccorby.photolabeller.config.SharedConfig
import com.mccorby.photolabeller.model.IterationLogger
import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.model.Trainer
import com.mccorby.photolabeller.repository.LocalDataSource
import org.deeplearning4j.nn.api.Model
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.transferlearning.TransferLearning
import org.deeplearning4j.optimize.api.IterationListener
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.factory.Nd4j
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * This is a singleton object. We don't want to create a trainer each time we need it
 * The trainer holds a reference to the model that is the biggest object in the app and the
 * most costly to build
 * The trainer does a transfer learning process during training, taking the images in the directories
 * and using them to update the model
 *
 */

class TrainerImpl: Trainer {

    companion object {
        var instance: TrainerImpl = TrainerImpl()
    }

    lateinit var config: SharedConfig
    lateinit var localDataSource: LocalDataSource
    lateinit var imageProcessor: ImageProcessorImpl
    var iterationLogger: IterationLogger? = null

    private val iterationListener = object : IterationListener {
        private var invoked = false
        override fun iterationDone(model: Model?, iteration: Int) {
                iterationLogger?.onIterationDone("Score at iteration $iteration: ${model!!.score()}")
        }

        override fun invoked() = invoked

        override fun invoke() {
            invoked = true
        }
    }

    private var model: MultiLayerNetwork? = null

    private var samplesUsedInTraining: Int = 0

    override fun loadModel(location: File): Stats {
        // Load model
        model = ModelSerializer.restoreMultiLayerNetwork(location)

        println(model.toString())

        return Stats("Model loaded")
    }

    override fun train(numSamples: Int, epochs: Int): Stats {
        model ?: return Stats("Model not ready")

        val imageLoader = ClientCifarLoader(localDataSource, imageProcessor, config.labels)

        val dataSetIterator = ClientCifarDataSetIterator(
                imageLoader,
                config.batchSize,
                1,
                config.labels.size,
                numSamples)

        // Freeze all layers until the layer indexed in the config file
        // This index must match the index in the model created in the server
        val newModel = TransferLearning.Builder(model)
                .setFeatureExtractor(config.featureLayerIndex)
                .build()
        // A second model is generated with TransferLearning. We cannot afford having two!
        model = newModel

        model!!.setListeners(iterationListener)

        for (i in 0 until epochs) {
            println("Epoch=====================$i")
            model!!.fit(dataSetIterator)
        }
        samplesUsedInTraining = Math.min(imageLoader.totalExamples(), config.maxSamples)
        // Empty listeners
        model!!.listeners = listOf()

        return Stats("Model trained")
    }

    override fun isModelLoaded() = model != null

    override fun predict(file: File): Stats {
        return model?.let {
            val image = imageProcessor.processImage(file)
            // We need to have a [batch_size, channels, width, height] array
            val result = it.predict(image)
            val output = it.output(image)

            val message = result.joinToString(", ", prefix = "[", postfix = "]")
            println(output)
            Stats(message, result[0], output.data().asDouble().asList())
        } ?: Stats("Model not ready")
    }

    override fun saveModel(file: File): File {
        ModelSerializer.writeModel(model!!, file, true)
        return file
    }

    override fun getSamplesInTraining() = samplesUsedInTraining

    override fun getUpdateFromLayer(): ByteArray {
        val weights = model!!.getLayer(config.featureLayerIndex).params()
        val outputStream = ByteArrayOutputStream()
        Nd4j.write(outputStream, weights)
        outputStream.flush()
        val bytes = outputStream.toByteArray()
        outputStream.close()
        return bytes
    }
}