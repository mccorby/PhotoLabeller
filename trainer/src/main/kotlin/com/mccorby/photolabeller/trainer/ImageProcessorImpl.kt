package com.mccorby.photolabeller.trainer

import com.mccorby.photolabeller.config.SharedConfig
import org.bytedeco.javacpp.opencv_core
import org.datavec.image.loader.NativeImageLoader
import org.nd4j.linalg.api.ndarray.INDArray
import java.io.File

interface ImageProcessor {
    fun processImage(file: File): INDArray
}

class ImageProcessorImpl(private val config: SharedConfig): ImageProcessor {

    override fun processImage(file: File): INDArray {
        val resizedImage = opencv_core.Mat()
        val sz = opencv_core.Size(config.imageSize, config.imageSize)
        val opencvImage = org.bytedeco.javacpp.opencv_imgcodecs.imread(file.absolutePath)
        org.bytedeco.javacpp.opencv_imgproc.resize(opencvImage, resizedImage, sz)

        val nil = NativeImageLoader()
        val image = nil.asMatrix(resizedImage)
        return image.reshape(intArrayOf(1, config.channels, config.imageSize, config.imageSize))
    }
}