package com.example.ecdsademo1.data

import android.util.Log
import com.example.ecdsademo1.util.EcOperations
import com.example.ecdsademo1.util.getRandomNumberLessThan
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

object Signature {
    fun messageSign(
        msg: String,
        p: BigInteger,
        n: BigInteger,
        G: Point,
        a: BigInteger,
        privateKey: BigInteger
    ): Pair<BigInteger, BigInteger> {
        var k: BigInteger
        var kInv: BigInteger
        var r: BigInteger
        var s: BigInteger
        var kG: Point
        do {
            do {
                k = getRandomNumberLessThan(n)
                kG = EcOperations.pointMultiply(G, p, a, k)
                r = kG.x.mod(n)
            } while (r.compareTo(BigInteger.ZERO) == 0)

            kInv = k.modInverse(n)
            val e = BigInteger(SHAsum(msg.toByteArray()), 16)
            val z = e.shiftRight(e.bitLength() - n.bitLength())
            //val z = BigInteger("1234567890")
            Log.d("asd", "sign: e = $e")
            Log.d("asd", "sign: z = $z")
            s = (kInv.multiply(z.add(privateKey.multiply(r)))).mod(n)
        } while (s.compareTo(BigInteger.ZERO) == 0)

        return Pair(r, s)
    }

    fun messageVerify(
        msg: String, sign: Pair<BigInteger, BigInteger>, p: BigInteger,
        n: BigInteger, G: Point, a: BigInteger, Q: Point
    ): Boolean {
        val r = sign.first
        val s = sign.second

        if (r.compareTo(n) >= 0 || s.compareTo(n) >= 0)
            return false
            //return Point(BigInteger.ZERO, BigInteger.ZERO)

        val e = BigInteger(SHAsum(msg.toByteArray()), 16)
        val z = e.shiftRight(e.bitLength() - n.bitLength())
        //val z = BigInteger("1234567890")
        Log.d("asd", "verify: e = $e")
        Log.d("asd", "verify: z = $z")
        val w = s.modInverse(n)
        val u1 = z.multiply(w).mod(n)
        val u2 = r.multiply(w).mod(n)
        val X = EcOperations.pointAddition(
            EcOperations.pointMultiply(G, p, a, u1),
            EcOperations.pointMultiply(Q, p, a, u2),
            p,
            a
        )

        if (X.x.equals(BigInteger.ZERO) || X.y.equals(BigInteger.ZERO)) {

        }

        val v = X.x.mod(n)
        if (v.compareTo(r) == 0)
            return true
        //return X
        return false
    }

    private fun SHAsum(msgByte: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-512")
        return byteArray2Hex(md.digest(msgByte))
    }

    private fun byteArray2Hex(hash: ByteArray): String {
        val fomatter = Formatter()
        try {
            for (i in hash) {
                fomatter.format("%02x", i)
            }
            return fomatter.toString()
        } finally {
            fomatter.close()
        }
    }
}