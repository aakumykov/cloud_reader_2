package com.github.aakumykov.cloud_reader_2

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.github.aakumykov.cloud_reader.CloudReader
import com.github.aakumykov.cloud_reader_2.databinding.ActivityMainBinding
import com.github.aakumykov.local_saf.LocalCloudReaderForStorageAccessFramework
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dirSelectionLauncher: ActivityResultLauncher<Uri?>
    private lateinit var fileSelectionLauncher: ActivityResultLauncher<Array<String>>

    private var selectedDir: Uri? = null
    private var selectedFile: Uri? = null

    private val cloudReader: CloudReader by lazy { LocalCloudReaderForStorageAccessFramework(applicationContext) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dirSelectionLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree(), ::onDirSelected)
        fileSelectionLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument(), ::onFileSelected)

        binding.selectDirButton.setOnClickListener { dirSelectionLauncher.launch(null) }
        binding.selectFileButton.setOnClickListener { fileSelectionLauncher.launch(arrayOf("*/*")) }

        binding.readDirButton.setOnClickListener { readSelectedDir() }
        binding.readFileButton.setOnClickListener { readSelectedFile() }
    }


    private fun readSelectedDir() {
        selectedDir?.also {

            lifecycleScope.launch {
                try {
                    appendInfo("Каталог существует: ${cloudReader.fileExists(selectedDir.toString()).getOrThrow()}")
                    appendInfo("Ссылка для 'скачивания': ${cloudReader.getDownloadLink(selectedDir.toString()).getOrThrow()}")
                    appendInfo("Поток чтения: ${cloudReader.getFileInputStream(selectedDir.toString()).getOrThrow()}")

                } catch (t: Throwable) {
                    showError(t.message ?: t.javaClass.simpleName)
                }
            }

        } ?: showError(R.string.error_no_dir_selected)
    }


    private fun readSelectedFile() {
        selectedFile?.also {

            lifecycleScope.launch {
                try {
                    appendInfo("Файл существует: ${cloudReader.fileExists(selectedDir.toString()).getOrThrow()}")
                    appendInfo("Ссылка для 'скачивания': ${cloudReader.getDownloadLink(selectedDir.toString()).getOrThrow()}")
                    appendInfo("Поток чтения: ${cloudReader.getFileInputStream(selectedDir.toString()).getOrThrow()}")

                } catch (t: Throwable) {
                    showError(t.message ?: t.javaClass.simpleName)
                }
            }

        } ?: showError(R.string.error_no_file_selected)
    }


    private fun onDirSelected(uri: Uri?) {
        selectedDir = uri
        hideMessages()
        showInfo(R.string.dir_is_selected, uri.toString())

        /*uri?.also {
            Log.d(TAG, "onDirSelected(), uri: $uri")

            LocalCloudReaderForStorageAccessFramework(applicationContext).also { cloudReader ->
                val filePath = uri.toString()
                lifecycleScope.launch (Dispatchers.IO) {
                    cloudReader.fileExists(filePath).also { isEsists ->
                        Log.d(TAG, "Файл '$filePath' существует: $isEsists")
                    }
                }
            }

            DocumentFile.fromTreeUri(this, uri)?.apply {

               listFiles().forEachIndexed { index, documentFile ->
                    Log.i(TAG, "$index) ${documentFile.name}")
               }

            } ?: { Log.e(TAG, "Ошибка получения DocumentFile из '$uri'") }

        } ?: { showToast(R.string.error_selecting_dir) }*/
    }

    private fun onFileSelected(uri: Uri?) {
        selectedFile = uri
        hideMessages()
        showInfo("Выбран файл: $selectedFile")

        /*uri?.also {

            showInfo("Выбран файл: $uri")

            DocumentFile.fromSingleUri(this, uri)?.also { documentFile ->

                lifecycleScope.launch {
                    localCloudReaderSAF.fileExists(uri.toString())
                        .onSuccess {
                            appendInfo("...СУЩЕСТВУЕТ")
                        }.onFailure {
                            appendInfo("...не СУЩЕСТВУЕТ")
                        }
                }

            } ?: {
                showError(R.string.error_creating_document_file_from_uri, uri.toString())
            }

        } ?: {
            showError(R.string.error_selecting_file)
        }*/
    }

    private fun hideMessages() {
        hideError()
        hideInfo()
    }


    private fun showInfo(text: String) {
        binding.infoView.text = text
    }

    private fun showInfo(@StringRes msgTemplate: Int, vararg arguments: Any) {
        binding.infoView.setText(getString(msgTemplate, arguments))
    }

    private fun appendInfo(newText: String) {
        binding.infoView.apply {
            text = "$text\n$newText"
        }
    }

    private fun hideInfo() {
        binding.infoView.setText(R.string.empty_string)
    }

    private fun showError(@StringRes errorMsgTemplate: Int, vararg arguments: Any) {
        binding.errorView.setText(getString(errorMsgTemplate, arguments))
    }

    private fun showError(@StringRes errorMsg: Int) {
        showError(getString(errorMsg))
    }

    private fun showError(errorMsg: String) {
        binding.errorView.text = errorMsg
    }



    private fun hideError() {
        binding.errorView.setText(R.string.empty_string)
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}