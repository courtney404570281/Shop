package com.courtney

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_nickname.*

class NicknameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        btn_finish.setOnClickListener {
            val nickname = edt_nickname.text.toString()
            getSharedPreferences("shop", Context.MODE_PRIVATE)
                .edit()
                .putString("NICKNAME", nickname)
                .apply()
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
