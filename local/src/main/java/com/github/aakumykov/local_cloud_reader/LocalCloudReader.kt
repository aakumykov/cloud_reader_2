package com.github.aakumykov.local_cloud_reader

import com.github.aakumykov.cloud_reader.CloudReader
import com.github.aakumykov.cloud_reader.FileMetadata
import com.github.aakumykov.cloud_reader.absolutePathFrom
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class LocalCloudReader : CloudReader {

    override suspend fun getDownloadLink(absolutePath: String): Result<String> {
        return if (fileExistsSimple(absolutePath)) Result.success(absolutePath)
        else Result.failure(FileNotFoundException("File not found '$absolutePath'"))
    }

    override suspend fun getFileInputStream(absolutePath: String): Result<FileInputStream> {
        return try {
            return Result.success(File(absolutePath).inputStream())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fileExists(absolutePath: String): Result<Boolean> {
        return try {
            Result.success(fileExistsSimple(absolutePath))
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun fileExists(basePath: String, fileName: String): Result<Boolean> {
        return fileExists(absolutePathFrom(basePath,fileName))
    }

    override suspend fun dirExists(absolutePath: String): Result<Boolean> {
        return try {
            Result.success(File(absolutePath).let { it.isDirectory && it.exists() })
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun dirExists(basePath: String, fileName: String): Result<Boolean> {
        return dirExists(absolutePathFrom(basePath,fileName))
    }

    override suspend fun getFileMetadata(absolutePath: String): Result<FileMetadata?> {
        return try {
            File(absolutePath).let { file ->
                if (file.exists()) {
                    FileMetadata(
                        name = file.name,
                        absolutePath = file.absolutePath,
                        size = if (file.isDirectory) 0L else file.length(),
                        isDir = file.isDirectory,
                        created = file.lastModified(),
                        modified = file.lastModified(),
                        childCount = file.listFiles()?.size
                    ).let {
                        Result.success(it)
                    }
                } else {
                    Result.success(null)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFileMetadata(basePath: String, fileName: String): Result<FileMetadata?> {
        return getFileMetadata(absolutePathFrom(basePath, fileName))
    }

    override suspend fun listDir(absolutePath: String): Result<List<FileMetadata>?> {
        return try {
            Result.success(
                File(absolutePath).listFiles()?.map { file: File ->
                    FileMetadata(
                        name = file.name,
                        absolutePath = file.absolutePath,
                        size = file.length(),
                        isDir = file.isDirectory,
                        created = file.lastModified(),
                        modified = file.lastModified(),
                        childCount = null
                    )
                }
            )
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun listDir(basePath: String, dirName: String): Result<List<FileMetadata>?> {
        return listDir(absolutePathFrom(basePath,dirName))
    }

    private fun fileExistsSimple(absolutePath: String): Boolean = File(absolutePath).exists()
}