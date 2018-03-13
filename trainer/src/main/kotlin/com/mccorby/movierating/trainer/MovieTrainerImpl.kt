package com.mccorby.movierating.trainer

import com.mccorby.movierating.model.Stats
import com.mccorby.movierating.model.Trainer
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
        return predict("I went and saw this movie last night after being coaxed to by a few friends of mine. I'll admit that I was reluctant to see it because from what I knew of Ashton Kutcher he was only able to do comedy. I was wrong. Kutcher played the character of Jake Fischer very well, and Kevin Costner played Ben Randall with such professionalism. The sign of a good movie is that it can toy with our emotions. This one did exactly that. The entire theater (which was sold out) was overcome by laughter during the first half of the movie, and were moved to tears during the second half. While exiting the theater I not only saw many women in tears, but many full grown men as well, trying desperately not to let anyone see them crying. This movie was great, and I suggest that you go see it before you judge.")
    }

    override fun isModelLoaded(): Boolean {
        return model != null
    }
}