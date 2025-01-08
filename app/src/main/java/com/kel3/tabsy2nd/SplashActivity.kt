package com.kel3.tabsy2nd

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Memainkan jingle dari raw folder
        val mediaPlayer = MediaPlayer.create(this, R.raw.jingle)
        mediaPlayer.start()

        // Delay selama durasi jingle sebelum pindah ke LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            mediaPlayer.release()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, mediaPlayer.duration.toLong()) // Durasi jingle otomatis
    }
}
