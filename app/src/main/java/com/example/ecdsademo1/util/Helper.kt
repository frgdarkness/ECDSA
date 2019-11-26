package com.example.ecdsademo1.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.math.BigInteger
import java.util.*

fun getRandomNumberLessThan(max: BigInteger): BigInteger {
    var result = BigInteger("0")
    val rd = Random()
    do {
        result = BigInteger(max.bitLength(), rd)
    } while (result >= max || result <= BigInteger.ONE)
    return result
}

fun isExternalStorageWritable() : Boolean{
    if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
        Log.d("asd", "it is writable")
        return true
    }
    return false
}

fun checkPermission(context: Context, permission : String) : Boolean{
    val check = ContextCompat.checkSelfPermission(context, permission)
    Log.d("asd", "permisstion: $check")
    return check == PackageManager.PERMISSION_GRANTED
}

fun showToast(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
