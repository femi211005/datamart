package com.example.datamart;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.datamart.CategoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // --- BACA SHAREDPREFERENCES (Mengingat Tema Terakhir) ---
        SharedPreferences sharedPreferences = getSharedPreferences("TemaApp", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // --------------------------------------------------------

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // PERBAIKAN: Menggunakan ID yang benar dari activity_main.xml
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.menu_beranda) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.menu_pesanan) {
                selectedFragment = new CartFragment();
            } else if (itemId == R.id.menu_kategori) {
                // SEKARANG MEMBUKA HALAMAN KATEGORI
                selectedFragment = new CategoryFragment();
            } else if (itemId == R.id.menu_pengaturan) {
                // SEKARANG MEMBUKA HALAMAN PENGATURAN
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}