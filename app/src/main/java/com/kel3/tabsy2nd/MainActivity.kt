package com.kel3.tabsy2nd

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val berandaFragment = BerandaFragment()
        val reservasiFragment = PesananFragment()
        val favoritFragment =FavoritFragment()
        val profilFragment = ProfilFragment()

        setCurrentFragment(berandaFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.icBeranda -> setCurrentFragment(berandaFragment)
                R.id.icPesanan -> setCurrentFragment(reservasiFragment)
                R.id.icFavorit-> setCurrentFragment(favoritFragment)
                R.id.icProfil -> setCurrentFragment(profilFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment : Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}