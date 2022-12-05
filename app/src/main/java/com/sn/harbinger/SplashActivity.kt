package com.sn.harbinger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sn.harbinger.databinding.ActivitySplashBinding
import com.sn.harbinger.di.SharedPreferencesModule
import com.sn.harbinger.ui.auth.AuthActivity
import com.sn.harbinger.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity() : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesModule

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (sharedPreferences.getToken().isEmpty())
            navigateScreen(AuthActivity::class.java)
        else
            navigateScreen(DashboardActivity::class.java)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun navigateScreen(screen: Class<*>) {
        Handler().postDelayed({
            val intent = Intent(applicationContext, screen)
            startActivity(intent)
        }, 2000)
    }

}