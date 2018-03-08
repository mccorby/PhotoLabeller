package com.mccorby.photolabeler.trainer

import com.mccorby.photolabeler.config.SharedConfig
import org.datavec.api.io.labels.ParentPathLabelGenerator
import org.datavec.image.recordreader.ImageRecordReader
import java.io.File

class ImageProcessor(val rootDir: File, private val config: SharedConfig) {

    fun readImage() {
        val labelMaker = ParentPathLabelGenerator()
        val recordReader = ImageRecordReader(config.imageSize, config.imageSize, config.channels, labelMaker)
    }
}