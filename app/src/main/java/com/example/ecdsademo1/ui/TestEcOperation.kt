package com.example.ecdsademo1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecdsademo1.R
import com.example.ecdsademo1.data.Point
import com.example.ecdsademo1.util.EcOperations
import kotlinx.android.synthetic.main.activity_test_ec_operation.*
import java.math.BigInteger

class TestEcOperation : AppCompatActivity() {
    val REQUEST_WRITE_PERMISSION = 167
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_ec_operation)
        supportActionBar?.setTitle("Test EcOperation")
        val n = BigInteger("23")
        val a = BigInteger("1")
        btnAdd.setOnClickListener{
            val p = Point(
                BigInteger(edtXP.text.toString()),
                BigInteger(edtYP.text.toString())
            )
            val q = Point(
                BigInteger(edtXQ.text.toString()),
                BigInteger(edtYQ.text.toString())
            )
            val result = EcOperations.pointAddition(p, q, n, a)
            textResultAdd.text = result.toString()
        }
        btnDoublind.setOnClickListener{
            val p = Point(
                BigInteger(edtXP.text.toString()),
                BigInteger(edtYP.text.toString())
            )
            val q = Point(
                BigInteger(edtXQ.text.toString()),
                BigInteger(edtYQ.text.toString())
            )
            val result1 = EcOperations.pointMDoubling(p, n, a)
            val result2 = EcOperations.pointMDoubling(q, n, a)
            textResultDoubling.text = result1.toString() + " - " + result2.toString()
        }
        btnMulti.setOnClickListener{
            val p = Point(
                BigInteger(edtXP.text.toString()),
                BigInteger(edtYP.text.toString())
            )
            val q = Point(
                BigInteger(edtXQ.text.toString()),
                BigInteger(edtYQ.text.toString())
            )
            val multiConstant = BigInteger(edtMulti.text.toString())
            val result1 = EcOperations.pointMultiply(p, n, a, multiConstant)
            val result2 = EcOperations.pointMultiply(q, n, a, multiConstant)
            textResultDoubling.text = result1.toString() + " - " + result2.toString()
        }
    }
}
