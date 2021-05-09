package com.mahdikaseatashin.shotshot.utils

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mahdikaseatashin.shotshot.R
import com.mahdikaseatashin.shotshot.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //Image by mode in system
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> iv_logo.setImageResource(R.drawable.shot_logo_loos_tp)
            Configuration.UI_MODE_NIGHT_YES -> iv_logo.setImageResource(R.drawable.shot_logo_loos_tp)
            Configuration.UI_MODE_NIGHT_UNDEFINED -> iv_logo.setImageResource(R.drawable.shot_logo_loos_tp)
        }

        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000L)
    }

}
