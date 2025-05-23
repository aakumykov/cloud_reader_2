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


    suspend fun getFileMetadata(absolutePath: String): Result<FileMetadata?>

    suspend fun getFileMetadata(basePath: String, fileName: String): Result<FileMetadata?>


    /**
     * @return Result with:
     * 1) List of FileMetadata
     * 2) NULL if checked fs item is not a dir.
     */
    suspend fun listDir(absolutePath: String): Result<List<FileMetadata>?>

    /**
     * See description of [listDir] (absolutePath: String)
     */
    suspend fun listDir(basePath: String, dirName: String): Result<List<FileMetadata>?>
}