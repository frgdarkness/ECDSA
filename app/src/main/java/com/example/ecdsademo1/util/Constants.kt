package com.example.ecdsademo1.util

import com.example.ecdsademo1.data.Point
import java.math.BigInteger

class Constants {
    companion object{
        const val CURVE_NAME = "p192"
        val P = BigInteger("6277101735386680763835789423207666416083908700390324961279")
        val N = BigInteger("6277101735386680763835789423176059013767194773182842284081")
        val X_G = BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16)
        val Y_G = BigInteger("07192b95ffc8da78631011ed6b24cdd573f977a11e794811", 16)
        val G = Point(X_G, Y_G)
        val A = BigInteger("-3")

//        val P = BigInteger("23")
//        val N = BigInteger("7")
//        val A = BigInteger.valueOf(1)
//        val G = Point(BigInteger("17"), BigInteger("3"))
    }
}