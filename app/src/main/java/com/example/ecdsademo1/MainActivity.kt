package com.example.ecdsademo1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ecdsademo1.data.Point
import com.example.ecdsademo1.ui.*
import com.example.ecdsademo1.util.EcOperations
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_verify.*
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    val REQUEST_WRITE_PERMISSION = 167

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askPermission(REQUEST_WRITE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        setPropertyPOI()
        btnGoCreateKeyPair.setOnClickListener{
            startActivity(Intent(this, CreateKeyActivity::class.java))
        }
        btnGoSignature.setOnClickListener{
            startActivity(Intent(this, SignActivity::class.java))
        }
        btnGoVerify.setOnClickListener{
            startActivity(Intent(this, VerifyActivity::class.java))
        }
    }

    fun setPropertyPOI(){
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.optionTestEcOperation ->{
                startActivity(Intent(this, TestEcOperation::class.java))
            }
        }
        return true
    }

    fun askPermission(requestID : Int, permissionName: String) : Boolean{
        if(android.os.Build.VERSION.SDK_INT >= 23){
            val permission = ActivityCompat.checkSelfPermission(this, permissionName)
            if(permission != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(permissionName), requestID)
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(grantResults.size > 0) {
            when (requestCode) {
                REQUEST_WRITE_PERMISSION -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("asd", "accept write permission")
                    }
                }
            }
        }else{
            Toast.makeText(this, "permission cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
