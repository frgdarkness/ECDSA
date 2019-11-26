package com.example.ecdsademo1.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import com.example.ecdsademo1.R
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_choose_file.*
import java.io.File
import java.util.regex.Pattern

class ChooseFileActivity : AppCompatActivity() {

    val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
    var filePath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_file)

        btnChooseFile.setOnClickListener{
//            fileIntent.setType("*/*")
//            startActivityForResult(fileIntent, 10)
            pickFile2()
        }

        btnOpenFile.setOnClickListener{
            val targetFile = File(filePath)
            val targetUri = Uri.fromFile(targetFile)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(targetUri, "application/msword")
            startActivity(intent)
        }

    }

    fun pickFile2(){
        MaterialFilePicker()
            .withActivity(this)
            .withRequestCode(1000)
            .withFilter(Pattern.compile(".*\\.docx$")) // Filtering files and directories by file name using regexp
            .withFilterDirectories(false) // Set directories filterable (false by default)
            .withHiddenFiles(true) // Show hidden files and folders
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("asd", "requestCode = $requestCode, resultCold = $resultCode" )
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            10 -> {
                if(resultCode == Activity.RESULT_OK){
                    val path = data?.data?.path
                    path?.let {
                        filePath = it
                    }
                    textFilePath.text = path
               }
             }
            1000 -> {
                if(resultCode == Activity.RESULT_OK){
                    val path = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                    textFilePath.append("\n$path")
                    filePath = path!!
                }
            }
        }

    }
}
