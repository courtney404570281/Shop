package com.courtney

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_signup.setOnClickListener {
            var sEmail = edt_email.text.toString()
            var sPassword = edt_password.text.toString()
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
        }

    }
}
