package com.github.aakumykov.cloud_reader

data class FileMetadata(
    val name: String,
    val absolutePath: String,
    val isDir: Boolean,
    val created: Long,
    val modified: Long,
)