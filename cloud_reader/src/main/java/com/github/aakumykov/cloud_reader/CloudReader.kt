package com.github.aakumykov.cloud_reader

import java.io.InputStream

// TODO: переименовать в StorageReader
interface CloudReader {
    suspend fun getDownloadLink(absolutePath: String): Result<String>

    suspend fun getFileInputStream(absolutePath: String): Result<InputStream>

    /**
     * Checks file or dir exists.
     */
    suspend fun fileExists(absolutePath: String): Result<Boolean>

    /**
     * Checks file or dir exists.
     */
    suspend fun fileExists(basePath: String, fileName: String): Result<Boolean>

    suspend fun dirExists(absolutePath: String): Result<Boolean>

    suspend fun dirExists(basePath: String, fileName: String): Result<Boolean>

    suspend fun getFileMetadata(absolutePath: String): Result<FileMetadata>
}