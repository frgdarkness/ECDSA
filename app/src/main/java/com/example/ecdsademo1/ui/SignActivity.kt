package com.example.ecdsademo1.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.ecdsademo1.R
import com.example.ecdsademo1.data.Signature
import com.example.ecdsademo1.util.Constants
import com.example.ecdsademo1.util.isExternalStorageWritable
import com.example.ecdsademo1.util.showToast
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_sig.*
import kotlinx.android.synthetic.main.activity_sign.*
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.*
import java.lang.Exception
import java.math.BigInteger
import java.util.*
import java.util.regex.Pattern

class SignActivity : AppCompatActivity() {

    val PICK_DOCX_FILE = 111
    val PICK_PRIVATE_KEY = 222
    var docxFilePath = ""
    var privateKeyFilePath = ""
    var contentDocx = ""
    lateinit var privateKey: BigInteger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        supportActionBar?.setTitle("Signature")
        pgBarSign.visibility = View.GONE
        btnPickDocx.setOnClickListener{
            pickFile(PICK_DOCX_FILE)
        }

        btnPickPrivateKey.setOnClickListener{
            pickFile(PICK_PRIVATE_KEY)
        }

        btnSignature.setOnClickListener{
            pgBarSign.visibility = View.VISIBLE
            if(privateKeyFilePath.isNotEmpty() && docxFilePath.isNotEmpty()) {
                openDocxFile()
                openPrivateKeyFile()
                signature()
            }else{
                showToast(this,"Pick the Docx and PrivateKey File First!!!")
            }
        }

    }

    fun pickFile(requestCode: Int){
        var type = ""
        if(requestCode == PICK_PRIVATE_KEY)
            type = ".*\\.txt\$"
        else
            type = ".*\\.docx\$"
        MaterialFilePicker()
            .withActivity(this)
            .withRequestCode(requestCode)
            .withFilter(Pattern.compile(type)) // Filtering files and directories by file name using regexp
            .withFilterDirectories(false) // Set directories filterable (false by default)
            .withHiddenFiles(true) // Show hidden files and folders
            .start()
    }

    fun openDocxFile(){
        val docxFile = File(docxFilePath)
        val fis = FileInputStream(docxFile)
        val document = XWPFDocument(fis)
        val paragraphList = document.paragraphs
        var result = ""
        for(i in paragraphList){
            result += i.getText()
        }
        //textSign.text = "docx Lenght: ${result.length}"
        contentDocx = result
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show()
    }

    fun openPrivateKeyFile(){
        if(isExternalStorageWritable() && com.example.ecdsademo1.util.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ){
            val textFile = File(privateKeyFilePath)
            val sc = Scanner(textFile)
            var value = sc.nextLine()
            privateKey = BigInteger(value)
            //textSign.append("\nprivatekey: $privateKey")
        }
    }

    fun signature(){
        val signature = Signature.messageSign(contentDocx, Constants.P, Constants.N, Constants.G, Constants.A, privateKey)
        Handler().postDelayed(Runnable {
            pgBarSign.visibility = View.GONE
            textSign.text = "Signature:\nr = ${signature.first}\ns = ${signature.second}"
        },5000)

        val signContent = "${signature.first}\n${signature.second}"
        Log.d("asd", signContent)
        val signatureFileName = edtSignatureFileName.text.toString()
        if(signatureFileName.isNotEmpty()){
            if(isExternalStorageWritable() && com.example.ecdsademo1.util.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ){
                val textFile = File(Environment.getExternalStorageDirectory(), signatureFileName+".txt")
                try {
                    val fos = FileOutputStream(textFile)
                    fos.write(signContent.toByteArray())
                    fos.close()
                    Toast.makeText(this, "Signature File saved", Toast.LENGTH_SHORT).show()
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }else{
                Toast.makeText(this, "Cant write to the external storage", Toast.LENGTH_SHORT).show()
            }
        }else{
            showToast(this,"Fill Signature File Name!!!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            PICK_DOCX_FILE -> {
                if(resultCode == Activity.RESULT_OK){
                    val path = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                    docxFilePath = path!!
                    textDocxFile.text = path
                    //openDocxFile()
                }
            }
            PICK_PRIVATE_KEY -> {
                if(resultCode == Activity.RESULT_OK){
                    val path = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                    privateKeyFilePath = path!!
                    textPrivateKey.text = path
                    //openPrivateKeyFile()
                }
            }
        }
    }
}
