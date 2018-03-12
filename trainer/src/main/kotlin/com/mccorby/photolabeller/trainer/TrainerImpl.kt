package com.mccorby.photolabeller.trainer

import com.mccorby.photolabeller.config.SharedConfig
import com.mccorby.photolabeller.model.Stats
import org.datavec.image.loader.NativeImageLoader
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.api.ndarray.INDArray
import java.io.File

/**
 * Note that for convolutional models, input shape information follows the NCHW convention.
 * So if a model’s input shape default is new int[]{3, 224, 224},
 * this means the model has 3 channels and height/width of 224.
 *
 * https://deeplearning4j.org/transfer-learning
 */

// TODO Move interface out to domain models
interface Trainer {
    fun loadModel(location: File): Stats
    fun train(): Stats
    fun predict(input: File): Stats
    fun isModelLoaded(): Boolean
}


class TrainerImpl: Trainer {

    companion object {
        var instance: TrainerImpl = TrainerImpl()
    }

    lateinit var config: SharedConfig
    private var model: MultiLayerNetwork? = null

    override fun loadModel(location: File): Stats {
        // Load model
        model = ModelSerializer.restoreMultiLayerNetwork(location)

        println(model.toString())

        // Transfer learning using existing model and images in folders or just train directly

        return Stats("Model loaded")
    }

    override fun train(): Stats {
        if (model == null) {
            return Stats("Model not ready")
        }
        // Transfer learning using existing model and images in folders or just train directly

        return Stats("Model loaded")
    }

    override fun predict(input: File): Stats {
        if (model == null) {
            return Stats("No active model")
        }

        val loader = NativeImageLoader(config.imageSize, config.imageSize, config.channels)
        // Get the image into an INDarray
        var image: INDArray? = null
        try {
            image = loader.asMatrix(input)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        /* DataNormalization scaler = new ImagePreProcessingScaler(0,1);
                        scaler.transform(image);*/
        val output = model!!.output(image)

//        log.info("## The Neural Nets Pediction ##")
//        log.info("## list of probabilities per label ##")
//        //log.info("## List of Labels in Order## ");
//        // In new versions labels are always in order
//        log.info(output.toString())
//
//        var modelResult = output.toString()
//
//        val predict = model.predict(image)
//        modelResult += "===" + Arrays.toString(predict)
//        jta.append("the file chosen:")
//        jta.append("\n")
//        jta.append(files[i].getAbsolutePath())
//        jta.append("\n")
//        jta.append("the  identification result :$modelResult")
//        jta.append("\n")

        return Stats(output.toString())
    }

    override fun isModelLoaded(): Boolean {
        return model != null
    }
}