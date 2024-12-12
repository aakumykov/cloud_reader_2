package com.github.aakumykov.cloud_reader

import java.io.InputStream

// TODO: переименовать в StorageReader
interface CloudReader {

    fun getDownloadLinkSimple(absolutePath: String): Result<String>
    suspend fun getDownloadLink(absolutePath: String): Result<String>

    fun getFileInputStreamSimple(absolutePath: String): Result<InputStream>
    suspend fun getFileInputStream(absolutePath: String): Result<InputStream>

    fun fileExistsSimple(absolutePath: String): Result<Boolean>
    suspend fun fileExists(absolutePath: String): Result<Boolean>
}