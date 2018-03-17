package com.mccorby.photolabeller.trainer

import com.mccorby.photolabeller.filemanager.LocalDataSource
import org.datavec.image.loader.CifarLoader
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.util.FeatureUtil
import java.io.File

data class FileData(val label: String, val file: File)

class ClientCifarLoader(private val localDataSource: LocalDataSource,
                        private val imageProcessor: ImageProcessor,
                        private val labels: List<String>) {

    private lateinit var trainingFiles: MutableList<FileData>

    init {
        shuffleFiles()
    }

    fun createDataSet(batchSize: Int, fromIndex: Int = 0): DataSet {
        val dataSets = mutableListOf<DataSet>()
        val maxIndex = minOf(fromIndex + batchSize, trainingFiles.size)
        println("Creating dataset for indexes $fromIndex to $maxIndex")
        for (i in fromIndex until maxIndex) {
            println("Processing ${trainingFiles[i].file}")
            val label = FeatureUtil.toOutcomeVector(labelToIndex(trainingFiles[i].label), CifarLoader.NUM_LABELS)
            dataSets.add(DataSet(imageProcessor.processImage(trainingFiles[i].file), label))
        }
        return DataSet.merge(dataSets)
    }

    private fun shuffleFiles() {
        val trainingFilesMap: Map<String, List<File>> = localDataSource.loadTrainingFiles()
        trainingFiles = trainingFilesMap.flatMap { (key, values) -> values.map {filePath -> FileData(key, filePath)} }.toMutableList()
        trainingFiles.shuffle()
        println(trainingFiles)
    }

    private fun labelToIndex(label: String): Int {
        return labels.indexOf(label)
    }

    fun totalExamples(): Int = trainingFiles.size
}