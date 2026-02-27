package com.team.chord

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(500L)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
            overridePendingTransition(0, 0)
        }
    }
}
