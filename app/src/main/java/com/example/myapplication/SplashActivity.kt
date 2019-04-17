package com.example.myapplication

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Exception

class SplashActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        hideSystemUI()

        val backgrond=object : Thread() {

            override fun run() = try {
                Thread.sleep(3000)
                val intent= Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        backgrond.start()


    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        View.SYSTEM_UI_FLAG_FULLSCREEN
        View.SYSTEM_UI_FLAG_IMMERSIVE
    }


}


