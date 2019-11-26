package com.example.ecdsademo1.util

import android.util.Log
import com.example.ecdsademo1.data.Point
import java.math.BigInteger

object EcOperations {
    fun getNegationPoint(p: Point) =
         Point(
             p.x,
             p.y.multiply(BigInteger.valueOf(-1))
        )

    fun pointAddition(p1: Point, p2: Point, n: BigInteger, constA: BigInteger) : Point {
        var a = p2.y.subtract(p1.y)
        Log.d("asd","a = $a")
        var b = p2.x.subtract(p1.x)
        Log.d("asd", "b = $b")
        if(a == BigInteger.ZERO && b == BigInteger.ZERO)
            return pointMDoubling(p1, n, constA)
        b = b.modInverse(n)
        Log.d("asd", "b modI n = $b")
        a = a.multiply(b).mod(n)     // delta
        Log.d("asd","delta = $a")
        b = a.multiply(a)                   //delta^2
        Log.d("asd","delta^2 = $b")
        val xR = b.subtract(p1.x).subtract(p2.x).mod(n)
        val yR = a.multiply(p1.x.subtract(xR)).subtract(p1.y).mod(n)

        return Point(xR, yR)
    }

    fun pointMDoubling(p1: Point, n: BigInteger, a: BigInteger) : Point{
        var i = p1.x.multiply(p1.x).multiply(BigInteger.valueOf(3)).add(a)
        Log.d("asd", "ts = $i")
        val j = p1.y.multiply(BigInteger.valueOf(2)).modInverse(n)
        Log.d("asd", "ms modI = $j")
        val d = i.multiply(j).mod(n)    //delta
        Log.d("asd", "delta = $d")
        val xR = d.multiply(d).subtract(p1.x.multiply(BigInteger.valueOf(2))).mod(n)
        val yR = d.multiply(p1.x.subtract(xR)).subtract(p1.y).mod(n)
        return Point(xR, yR)
    }

    fun pointMultiply(p1: Point, n: BigInteger, a: BigInteger, count: BigInteger) : Point{
        Log.d("asd","multi: $p1 * $count mod $n with $a")
        var result = Point(BigInteger("0"), BigInteger("0"))
        var doubleTemp = p1
        var set = false
        val binaryCount = count.toString(2)
        Log.d("asd", "binaryCount: $binaryCount")
        val binaryCountReverse = binaryCount.reversed()
        Log.d("asd", "binaryCountReversed: $binaryCountReverse")
        for(i in binaryCountReverse){
            Log.d("asd", "i = $i ----------------------------------------")
            Log.d("asd", "result1 = $result - double1 = $doubleTemp")
            if(i == '1'){
                if(set){
                    result = pointAddition(
                        result,
                        doubleTemp,
                        n,
                        a
                    )
                }else{
                    result = doubleTemp
                    set = true
                }
                Log.d("asd", "result2 = $result")
            }
            doubleTemp = pointMDoubling(doubleTemp, n, a)
            Log.d("asd", "double: $doubleTemp")
        }
        Log.d("asd", "Result multi: $result")
        return result
    }

}