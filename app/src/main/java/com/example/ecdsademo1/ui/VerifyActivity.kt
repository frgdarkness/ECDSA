package com.example.ecdsademo1.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.ecdsademo1.R
import com.example.ecdsademo1.data.Signature
import com.example.ecdsademo1.util.Constants
import com.example.ecdsademo1.util.isExternalStorageWritable
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_sign.*
import kotlinx.android.synthetic.main.activity_verify.*
import kotlinx.android.synthetic.main.activity_verify.btnPickDocx
import kotlinx.android.synthetic.main.activity_verify.textDocxFile
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.util.*
import java.util.regex.Pattern

class VerifyActivity : AppCompatActivity() {
    val PICK_DOCX_FILE = 112
    val PICK_PUBLIC_KEY = 221
    val PICK_SIGNATURE = 333
    var docxFilePath = ""
    var publicKeyFilePath = ""
    var signatureFilePath = ""
    var contentDocx = ""
    var verifyStatus = false
    lateinit var publicKey: com.example.ecdsademo1.data.Point
    lateinit var signature: Pair<BigInteger, BigInteger>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
        supportActionBar?.setTitle("Verify")
        pgBarCircle.visibility = View.GONE
        imgResult.visibility = View.GONE

        textSignatureFile.setOnClickListener{
            Toast.makeText(this, "verify: $verifyStatus", Toast.LENGTH_SHORT).show()
            imgResult.visibility = View.VISIBLE
            pgBarCircle.visibility = View.GONE
            if(verifyStatus){
                imgResult.setImageResource(R.drawable.ic_check)
            }else
                imgResult.setImageResource(R.drawable.ic_warn)
        }

        btnPickDocx.setOnClickListener{
            pickFile(PICK_DOCX_FILE)
        }
        btnPickPublicKey.setOnClickListener{
            pickFile(PICK_PUBLIC_KEY)
        }
        btnPickSignatureFile.setOnClickListener{
            pickFile(PICK_SIGNATURE)
        }
        btnVerify.setOnClickListener{
            openDocxFile()
            openPublicKeyFile()
            openSignatureFile()
            verifySignature()
        }
    }

    fun verifySignature(){
        imgResult.visibility = View.GONE
        pgBarCircle.visibility = View.VISIBLE
        verifyStatus = Signature.messageVerify(contentDocx, signature, Constants.P, Constants.N, Constants.G, Constants.A, publicKey)
        //val x = verified.x.mod(Constants.N)
        //Log.d("asd", "x = $x")
        Handler().postDelayed(Runnable {
            pgBarCircle.visibility = View.GONE
            if(verifyStatus)
                imgResult.setImageResource(R.drawable.ic_check)
            else
                imgResult.setImageResource(R.drawable.ic_warn)
            imgResult.visibility = View.VISIBLE
        },5000)
        Log.d("asd","result = $verifyStatus")

    }

    fun pickFile(requestCode: Int){
        var type = ""
        if(requestCode == PICK_PUBLIC_KEY || requestCode == PICK_SIGNATURE)
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

    fun openPublicKeyFile(){
        if(isExternalStorageWritable() && com.example.ecdsademo1.util.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ){
            val textFile = File(publicKeyFilePath)
            val sc = Scanner(textFile)
            var value1 = sc.nextLine().toString()
            var value2 = sc.nextLine().toString()
            publicKey = com.example.ecdsademo1.data.Point(BigInteger(value1), BigInteger(value2))
            Log.d("asd", "publicKey: $publicKey")
        }
    }

    fun openSignatureFile(){
        if(isExternalStorageWritable() && com.example.ecdsademo1.util.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ){
            val textFile = File(signatureFilePath)
            val sc = Scanner(textFile)
            var value1 = sc.nextLine().toString()
            var value2 = sc.nextLine().toString()
            signature = Pair(BigInteger(value1), BigInteger(value2))
            Log.d("asd", "signature: $signature")
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
            PICK_PUBLIC_KEY -> {
                if(resultCode == Activity.RESULT_OK){
                    val path = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                    publicKeyFilePath = path!!
                    textPublicKey.text = path
                    //openPrivateKeyFile()
                }
            }
            PICK_SIGNATURE ->{
                if(resultCode == Activity.RESULT_OK){
                    val path = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                    signatureFilePath = path!!
                    textSignatureFile.text = path
                    //openPrivateKeyFile()
                }
            }
        }
    }
}
