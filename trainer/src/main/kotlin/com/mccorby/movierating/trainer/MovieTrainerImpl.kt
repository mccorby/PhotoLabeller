package com.mccorby.movierating.trainer

import com.mccorby.photolabeller.model.Stats
import com.mccorby.photolabeller.model.Trainer
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.indexing.NDArrayIndex
import java.io.File

class MovieTrainerImpl: Trainer<String> {

    companion object {
        var instance: Trainer<String> = MovieTrainerImpl()
    }
    private var model: MultiLayerNetwork? = null

    lateinit var wordVectorsPath: File
    lateinit var dataDir: String

    val truncateReviewsToLength = 256
    val batchSize = 64

    override fun loadModel(location: File): Stats {
        // Load model
        model = ModelSerializer.restoreMultiLayerNetwork(location)

        println(model.toString())

        // Transfer learning using existing model and images in folders or just train directly

        return Stats("Model loaded")
    }

    override fun predict(text: String): Stats {
        val wordVectors = WordVectorSerializer.loadStaticModel(wordVectorsPath)
        val train = SentimentExampleIterator(dataDir, wordVectors, batchSize, truncateReviewsToLength, true)

        val features = train.loadFeaturesFromString(text, truncateReviewsToLength)
        val networkOutput = model!!.output(features)
        val timeSeriesLength = networkOutput.size(2)
        val probabilitiesAtLastWord = networkOutput.get(NDArrayIndex.point(0), NDArrayIndex.all(),
                NDArrayIndex.point(timeSeriesLength - 1))
        val sb = StringBuilder()
        sb.append("p(positive): " + probabilitiesAtLastWord.getDouble(0))
        sb.append("\n")
        sb.append("p(negative): " + probabilitiesAtLastWord.getDouble(1))

        return Stats(sb.toString())
    }

    override fun train(): Stats {
        val wordVectors = WordVectorSerializer.loadStaticModel(wordVectorsPath)
        val train = SentimentExampleIterator(dataDir, wordVectors, batchSize, truncateReviewsToLength, true)

        model!!.fit(train)
        return Stats("Model train locally!")
    }

    override fun isModelLoaded(): Boolean {
        return model != null
    }
}