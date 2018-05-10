package com.mccorby.photolabeller.datasource

import android.content.res.AssetManager
import com.mccorby.photolabeller.repository.EmbeddedDataSource
import java.io.File
import java.io.FileOutputStream

class AssetDataSource(private val assetManager: AssetManager,
                      private val storageDir: File,
                      private val modelFileName: String): EmbeddedDataSource {

    override fun getModel(): File {
        val inputStream = assetManager.open(modelFileName)
        val file = File(storageDir, modelFileName)
        val fos = FileOutputStream(file)
        inputStream.copyTo(fos)
        inputStream.close()
        fos.close()
        return file
    }
}