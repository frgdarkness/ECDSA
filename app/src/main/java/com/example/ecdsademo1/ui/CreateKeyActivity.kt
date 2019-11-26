package com.example.ecdsademo1.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.ecdsademo1.R
import com.example.ecdsademo1.util.Constants
import com.example.ecdsademo1.util.checkPermission
import com.example.ecdsademo1.util.isExternalStorageWritable
import com.example.ecdsademo1.util.showToast
import kotlinx.android.synthetic.main.activity_create_key.*
import kotlinx.android.synthetic.main.activity_create_key.btnCreateKeyPair
import kotlinx.android.synthetic.main.activity_sig.*
import java.io.File
import java.io.FileOutputStream
import java.security.KeyPair

class CreateKeyActivity : AppCompatActivity() {

    lateinit var keyPair: com.example.ecdsademo1.data.KeyPair
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_key)
        supportActionBar?.setTitle("Create KeyPair")
        btnCreateKeyPair.setOnClickListener{
            keyPair = com.example.ecdsademo1.data.KeyPair(Constants.G, Constants.P, Constants.N, Constants.A)
            textShowKeyPair.text = "PublicKey\nxG:\n${keyPair.PublicKey.x}\nyG:\n${keyPair.PublicKey.y}\n\nPrivateKey:\n${keyPair.PrivateKey}"
        }

        btnSaveKeyPair.setOnClickListener{
            if(textShowKeyPair.text.isNotEmpty()){
                if(edtPublicKeyFileName.text.isNotEmpty() && edtPrivateKeyFileName.text.isNotEmpty()){
                    savePublicKey()
                    savePrivateKey()
                }
                showToast(this, "Fill Key File Name")
            }
            showToast(this, "Click Create Keypair Fisrt!")
        }
    }

    fun savePublicKey(){
        if(isExternalStorageWritable() && checkPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val fileName = edtPublicKeyFileName.text.toString() + ".txt"
            //val textFile = File(Environment.getExternalStorageDirectory(), edtTitle.text.toString()+".txt")
            val contentString = keyPair.PublicKey.x.toString() + "\n" + keyPair.PublicKey.y.toString()
            val textFile = File(Environment.getExternalStorageDirectory(), fileName)
            try {
                val fos = FileOutputStream(textFile)
                fos.write(contentString.toByteArray())
                fos.close()
                Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }else{
            Toast.makeText(this, "not accept to write file", Toast.LENGTH_SHORT).show()
        }
    }

    fun savePrivateKey(){
        if(isExternalStorageWritable() && checkPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val fileName = edtPrivateKeyFileName.text.toString() + ".txt"
            val textFile = File(Environment.getExternalStorageDirectory(), fileName)
            val contentString = keyPair.PrivateKey.toString()
            try {
                val fos = FileOutputStream(textFile)
                fos.write(contentString.toByteArray())
                fos.close()
                Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }else{
            Toast.makeText(this, "not accept to write file", Toast.LENGTH_SHORT).show()
        }
    }

//    fun isExternalStorageWritable() : Boolean{
//        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
//            Log.d("asd", "it is writable")
//            return true
//        }
//        return false
//    }
//
//    fun checkPermission(permission : String) : Boolean{
//        val check = ContextCompat.checkSelfPermission(this, permission)
//        Log.d("asd", "permisstion: $check")
//        return check == PackageManager.PERMISSION_GRANTED
//    }
}
