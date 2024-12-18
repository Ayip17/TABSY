package com.kel3.tabsy;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.kel3.tabsy.ReservasiFragment;
import com.kel3.tabsy.FavoritFragment;
import com.kel3.tabsy.BerandaFragment;

import com.kel3.tabsy.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.menu.bottom_nav_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load default fragment (BerandaFragment)
        loadFragment(new BerandaFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {

//            int itemId = item.getItemId();
//            Log.d("DEBUG", "Clicked item ID: " + itemId);
//            return true;

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
                case R.id.nav_profil:
                    selectedFragment = new ProfilFragment();
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
