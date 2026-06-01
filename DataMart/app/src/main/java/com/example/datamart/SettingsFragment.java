package com.example.datamart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // 1. Menghubungkan ID dari XML
        SwitchMaterial switchTheme = view.findViewById(R.id.switchTheme);
        MaterialButton btnLogout = view.findViewById(R.id.btnLogout);
        TextView tvName = view.findViewById(R.id.tvProfileName);
        TextView tvEmail = view.findViewById(R.id.tvProfileEmail);
        TextView tvEdit = view.findViewById(R.id.tvEditProfile);

        // 2. Mengambil Data Akun dari Brankas (SharedPreferences)
        SharedPreferences userPrefs = requireActivity().getSharedPreferences("AkunApp", Context.MODE_PRIVATE);
        String savedName = userPrefs.getString("nama", "Pengguna Lumina");
        String savedEmail = userPrefs.getString("email", "email@lumina.com");

        tvName.setText(savedName);
        tvEmail.setText(savedEmail);

        // 3. Logika Edit Profil dengan Pop-up (AlertDialog)
        tvEdit.setOnClickListener(v -> {
            // Membuat wadah untuk inputan pop-up
            android.widget.LinearLayout layout = new android.widget.LinearLayout(getContext());
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(60, 40, 60, 20);

            // Kolom Input Nama
            android.widget.EditText editName = new android.widget.EditText(getContext());
            editName.setText(tvName.getText().toString()); // Mengisi otomatis dengan nama saat ini
            layout.addView(editName);

            // Kolom Input Email
            android.widget.EditText editEmail = new android.widget.EditText(getContext());
            editEmail.setText(tvEmail.getText().toString()); // Mengisi otomatis dengan email saat ini
            layout.addView(editEmail);

            // Membuat dan menampilkan Pop-up
            new android.app.AlertDialog.Builder(getContext())
                    .setTitle("Edit Profil")
                    .setView(layout)
                    .setPositiveButton("Simpan", (dialog, which) -> {
                        // Mengambil teks baru yang diketik
                        String newName = editName.getText().toString();
                        String newEmail = editEmail.getText().toString();

                        // Menyimpan kembali ke "Brankas" SharedPreferences
                        SharedPreferences.Editor editor = userPrefs.edit();
                        editor.putString("nama", newName);
                        editor.putString("email", newEmail);
                        editor.apply();

                        // Memperbarui tampilan teks di layar secara langsung
                        tvName.setText(newName);
                        tvEmail.setText(newEmail);
                        Toast.makeText(getContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });

        // 4. Logika Mode Gelap Terang
        SharedPreferences themePrefs = requireActivity().getSharedPreferences("TemaApp", Context.MODE_PRIVATE);
        boolean isDarkMode = themePrefs.getBoolean("dark_mode", false);
        switchTheme.setChecked(isDarkMode);

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = themePrefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // 5. Logika Logout
        btnLogout.setOnClickListener(v -> {
            // Opsional: Hapus data sesi login jika ingin akun benar-benar terhapus saat keluar
            // SharedPreferences.Editor editor = userPrefs.edit();
            // editor.clear();
            // editor.apply();

            // Kembali ke halaman Login
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}