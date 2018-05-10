package com.mccorby.photolabeller.datasource.filemanager

import com.mccorby.fp.Either
import com.mccorby.photolabeller.config.SharedConfig
import com.mccorby.photolabeller.repository.LocalDataSource
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOCase
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.SuffixFileFilter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class FileManager(private val storageDir: File, private val config: SharedConfig) : LocalDataSource {

    override fun createTempFile(type: String): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "${type.toUpperCase()}_" + timeStamp + "_"

        // Save a temporary file
        return File.createTempFile(
                imageFileName, /* prefix */
                ".$type", /* suffix */
                storageDir      /* directory */
        )
    }

    override fun saveLabelImage(currentPhotoPath: String, selectedLabel: String): File {
        // Check directory for label exists
        val dir = File(storageDir.absolutePath, selectedLabel).apply { mkdir() }

        val file = File(currentPhotoPath)
        file.renameTo(File(dir, file.name))
        return file
    }

    override fun loadModelFile(): Either<Exception, File> {
        return when (File(storageDir.absolutePath, config.modelFilename).exists()) {
            true -> Either.Right(File(storageDir.absolutePath, config.modelFilename))
            false -> Either.Left(FileNotFoundException())
        }
    }

    override fun loadTrainingFiles(): Map<String, List<File>> {
        val mapFiles = mutableMapOf<String, MutableList<File>>()
        val filter = SuffixFileFilter(listOf(".png", ".jpeg", ".jpg"), IOCase.INSENSITIVE)
        val allFiles = FileUtils
                .listFilesAndDirs(storageDir, filter, DirectoryFileFilter.DIRECTORY)
                .filter { file -> file.isFile && file.parent != storageDir.absolutePath }

        allFiles.forEach { addFileToMap(mapFiles, it) }

        return mapFiles
    }

    // TODO Can this be kotlinised?
    private fun addFileToMap(mapFiles: MutableMap<String, MutableList<File>>, file: File) {
        val fileList = mapFiles.getOrDefault(file.parentFile.name, mutableListOf())
        fileList.add(file)
        mapFiles[file.parentFile.name] = fileList
    }

    override fun serializeModel(initialStream: InputStream?): File {
        val file = File(storageDir.absolutePath, config.modelFilename)
        initialStream?.let {
            val fileOutputStream = FileOutputStream(file)

            val b = ByteArray(1024)
            var len = initialStream.read(b, 0, 1024)
            while (len > 0) {
                fileOutputStream.write(b, 0, len)
                len = initialStream.read(b, 0, 1024)
            }
            fileOutputStream.close()
            initialStream.close()
        }
        return file
    }

}