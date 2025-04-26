package com.github.aakumykov.cloud_reader

/**
 * @param childCount null if not checked, integer if checked.
 */
data class FileMetadata(
    val name: String,
    val absolutePath: String,
    val size: Long,
    val isDir: Boolean,
    val created: Long,
    val modified: Long,
    val childCount: Int?,
)