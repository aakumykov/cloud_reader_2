package com.github.aakumykov.cloud_reader_2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.documentfile.provider.DocumentFile
import com.github.aakumykov.cloud_reader_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dirSelectionLauncher: ActivityResultLauncher<Uri?>

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

        binding.readDirButton.setOnClickListener { dirSelectionLauncher.launch(null) }
    }

    private fun onDirSelected(uri: Uri?) {
        uri?.also {
            Log.d(TAG, "onDirSelected(), uri: $uri")

            DocumentFile.fromTreeUri(this, uri)?.apply {

               listFiles().forEachIndexed { index, documentFile ->
                    Log.i(TAG, "$index) ${documentFile.name}")
               }

            } ?: { Log.e(TAG, "Ошибка получения DocumentFile из '$uri'") }

        } ?: { showToast(R.string.error_selecting_dir) }
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes text: Int) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}