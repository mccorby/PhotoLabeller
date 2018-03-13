package com.mccorby.movierating.filemanager

import java.io.File

interface FileManager {
    fun loadModelFile(): File
    fun loadFile(name: String): File
    fun rootDir(): String
}

class FileManagerImpl(private val storageDir: File): FileManager {

    override fun loadModelFile(): File {
        return File(storageDir.absolutePath, "imdb.zip")
    }

    override fun loadFile(name: String): File {
        return File(storageDir.absolutePath, name)
    }

    override fun rootDir(): String = storageDir.absolutePath

}