package com.sn.harbinger.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sn.harbinger.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}