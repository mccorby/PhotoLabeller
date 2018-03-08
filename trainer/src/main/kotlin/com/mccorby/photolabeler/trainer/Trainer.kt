package com.mccorby.photolabeler.trainer

import org.deeplearning4j.nn.api.Model
import org.nd4j.linalg.factory.Nd4j

/**
 * Note that for convolutional models, input shape information follows the NCHW convention.
 * So if a model’s input shape default is new int[]{3, 224, 224},
 * this means the model has 3 channels and height/width of 224.
 *
 * https://deeplearning4j.org/transfer-learning
 */
class Trainer {

    private lateinit var model: Model

    fun loadModel(): Unit {
        val features = Nd4j.zeros(3, 224)
    }

    fun predict(input: ByteArray): Int {
        val features = Nd4j.zeros(3, 224)
//        for (i in 0 until input.size) {
//            for (j in 0 until input[i].size)
//            features.putScalar(intArrayOf(0, i), input[i])
//        }
//        val predicted = model.output(features)
        return 0
    }
}