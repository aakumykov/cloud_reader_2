package com.github.aakumykov.local_cloud_reader

import com.github.aakumykov.cloud_reader.CloudReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class LocalCloudReader : CloudReader {

    // FIXME: FileInputStream --> InputStream

    override fun getDownloadLinkSimple(absolutePath: String): Result<String> {
        return if (fileExistsSimple(absolutePath).getOrThrow()) Result.success(absolutePath)
        else Result.failure(FileNotFoundException("File not found '$absolutePath'"))
    }

    override suspend fun getDownloadLink(absolutePath: String): Result<String> {
        return getDownloadLinkSimple(absolutePath)
    }

    override fun getFileInputStreamSimple(absolutePath: String): Result<FileInputStream> {
        return try {
            return Result.success(File(absolutePath).inputStream())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFileInputStream(absolutePath: String): Result<FileInputStream> {
        return getFileInputStreamSimple(absolutePath)
    }


    override suspend fun fileExists(absolutePath: String): Result<Boolean> {
        return fileExistsSimple(absolutePath)
    }

    override fun fileExistsSimple(absolutePath: String): Result<Boolean>
        = Result.success(File(absolutePath).exists())
}