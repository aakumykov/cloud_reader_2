package com.github.aakumykov.cloud_reader

data class FileMetadata(
    val name: String,
    val absolutePath: String,
    val size: Long,
    val created: Long,
    val modified: Long,
    val isDir: Boolean,
    val children: Iterable<FileMetadata>?,
)