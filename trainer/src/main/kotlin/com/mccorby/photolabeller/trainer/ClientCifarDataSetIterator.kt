package com.mccorby.photolabeller.trainer

import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.nd4j.linalg.dataset.DataSet

class ClientCifarDataSetIterator(private val imageLoader: ClientCifarLoader,
                                 batchSize: Int,
                                 labelIndex: Int,
                                 numLabels: Int,
                                 private val numSamples: Int)
    : RecordReaderDataSetIterator(null, batchSize, labelIndex, numLabels) {

    private var counter: Int = 0

    override fun next(batchSize: Int): DataSet {
        println("next batch ${counter + batchSize}")
        val dataSet = imageLoader.createDataSet(batchSize, counter)
        counter += batchSize
        return dataSet
    }

    override fun reset() {
        counter = 0
    }

    override fun hasNext(): Boolean {
        return counter < numSamples
    }

    override fun asyncSupported(): Boolean = false
}