package com.mccorby.photolabeler.trainer

import kotlinx.coroutines.experimental.async
import org.deeplearning4j.nn.api.Model
import org.deeplearning4j.zoo.PretrainedType
import org.deeplearning4j.zoo.model.VGG16
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
        async {
            System.out.print("Getting zoo model VGG16")
            val zooModel = VGG16()
            model = zooModel.initPretrained(PretrainedType.CIFAR10)
            System.out.print("Model loaded $model")
        }
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