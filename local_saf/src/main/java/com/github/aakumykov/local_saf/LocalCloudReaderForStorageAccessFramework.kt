package com.github.aakumykov.local_saf

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.github.aakumykov.cloud_reader.CloudReader
import java.io.InputStream

/**
 * @param applicationContext Документация рекомендует использовать Application Context...
 */
class LocalCloudReaderForStorageAccessFramework(private val applicationContext: Context) : CloudReader {

    /**
     * @return Uri of file system item from Storage Access Framework
     */
    override suspend fun getDownloadLink(absolutePath: String): Result<String> {
        return try {
            absolutePathToDocumentFile(absolutePath)
                .getOrThrow()
                ?.uri
                ?.let { Result.success(it.toString()) }
                    ?: Result.failure(AbsolutePathConversionException(absolutePath))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getFileInputStream(absolutePath: String): Result<InputStream> {
        return try {
            val documentUri = absolutePathToDocumentFile(absolutePath)
                .getOrThrow()
                ?.uri

            val inputStream = if (null != documentUri) applicationContext.contentResolver.openInputStream(documentUri)
            else null

            if (null != inputStream) Result.success(inputStream)
            else Result.failure(Exception("Cannot open input stream for file '$absolutePath"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun fileExists(absolutePath: String): Result<Boolean> {
        return absolutePathToDocumentFile(absolutePath)
            .map {
                it?.exists()
                    ?: throw AbsolutePathConversionException(absolutePath)
            }
    }


    private fun absolutePathToDocumentFile(absolutePath: String): Result<DocumentFile?> {
        return try {
            DocumentFile.fromSingleUri(
                applicationContext,
                Uri.parse(absolutePath)
            ).let {
                Result.success(it)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    class AbsolutePathConversionException(absolutePath: String): Exception("Error getting DocumentFile from absolute path '$absolutePath'")
}