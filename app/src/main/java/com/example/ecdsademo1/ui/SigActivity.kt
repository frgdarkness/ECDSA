package com.example.ecdsademo1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecdsademo1.R
import com.example.ecdsademo1.data.KeyPair
import com.example.ecdsademo1.data.Signature
import com.example.ecdsademo1.util.Constants
import com.example.ecdsademo1.util.EcOperations
import kotlinx.android.synthetic.main.activity_sig.*
import java.math.BigInteger

class SigActivity : AppCompatActivity() {

    lateinit var keyPair: KeyPair

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sig)
        btnCreateKeyPair.setOnClickListener{
            keyPair = KeyPair(Constants.G, Constants.P, Constants.N, Constants.A)
            textKeyPair.text = "xG:\n${keyPair.PublicKey.x}\nyG:\n${keyPair.PublicKey.y}\nprivateKey:\n${keyPair.PrivateKey}"
        }

        val msg = "Hoc Vien Cong Nghe Buu Chinh Vien Thong"
        val msg2 = "HVCNBCVT"
        btnSign.setOnClickListener{
            val signature = Signature.messageSign(msg, Constants.P, Constants.N, Constants.G, Constants.A, keyPair.PrivateKey)
            val verified = Signature.messageVerify(msg, signature, Constants.P, Constants.N, Constants.G, Constants.A, keyPair.PublicKey)
            textResultSign.text = "\nSignature verification status: $verified"
//            textResultSign.text = "sign: r = \n${signature.first}\nverify:xX mod n =\n${verified.x.mod(Constants.N)} "
//            if(verified.x.mod(Constants.N).compareTo(signature.first) == 0){
//                textResultSign.append("\n(xX mod n) = r => true")
//            }else
//                textResultSign.append("\n(xX mod n) != r => false")

        }
    }
}
