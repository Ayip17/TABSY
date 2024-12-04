package com.kel3.tabsy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.kel3.tabsy.ReservasiFragment;
import com.kel3.tabsy.FavoritFragment;
import com.kel3.tabsy.BerandaFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load default fragment (BerandaFragment)
        loadFragment(new BerandaFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_beranda:
                    selectedFragment = new BerandaFragment();
                    break;
                case R.id.nav_reservasi:
                    selectedFragment = new ReservasiFragment();
                    break;
                case R.id.nav_favorit:
                    selectedFragment = new FavoritFragment();
                    break;
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
