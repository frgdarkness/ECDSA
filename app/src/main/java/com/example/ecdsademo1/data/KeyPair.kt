package com.example.ecdsademo1.data

import android.util.Log
import com.example.ecdsademo1.util.EcOperations
import com.example.ecdsademo1.util.getRandomNumberLessThan
import java.math.BigInteger

class KeyPair(val point: Point,p: BigInteger, n: BigInteger, a: BigInteger) {
    var PrivateKey: BigInteger
    var PublicKey: Point
    init {
        PrivateKey = getRandomNumberLessThan(n)
        Log.d("asd", "privateKey: $PrivateKey")
        //privateKey = BigInteger("3")
        PublicKey = EcOperations.pointMultiply(point, p, a, PrivateKey)
    }
}