package com.mccorby.photolabeller.filemanager

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

interface FileManager {
    fun createImageFile(): File
    fun saveLabelImage(currentPhotoPath: String, selectedLabel: String)
    fun loadModelFile(): File
    fun loadFile(name: String): File
    fun rootDir(): String
}

class FileManagerImpl(private val storageDir: File): FileManager {

    override fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"

        // Save a temporary file
        return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
    }

    override fun saveLabelImage(currentPhotoPath: String, selectedLabel: String) {
        // Check directory for label exists
        val dir = File(storageDir.absolutePath, selectedLabel).apply { mkdir() }

        val file = File(currentPhotoPath)
        file.renameTo(File(dir, file.name))
    }

    override fun loadModelFile(): File {
        return File(storageDir.absolutePath, "imdb.zip")
    }

    override fun loadFile(name: String): File {
        return File(storageDir.absolutePath, name)
    }

    override fun rootDir(): String = storageDir.absolutePath

}