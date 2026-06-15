package com.example.datamart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences themePrefs = getSharedPreferences("TemaApp", MODE_PRIVATE);
        boolean isDarkMode = themePrefs.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        SharedPreferences userPrefs = getSharedPreferences("AkunApp", MODE_PRIVATE);
        boolean isLoggedIn = userPrefs.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_login);

        TextInputEditText etEmail = findViewById(R.id.etEmailLogin);
        TextInputEditText etPassword = findViewById(R.id.etPasswordLogin);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String emailInput = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String passwordInput = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

            String savedName = userPrefs.getString("name", "Pengguna Datamart");
            String savedEmail = userPrefs.getString("email", "");
            String savedPassword = userPrefs.getString("password", "");

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            else if (emailInput.equals(savedEmail) && passwordInput.equals(savedPassword)) {

                SharedPreferences.Editor editor = userPrefs.edit();
                editor.putBoolean("isLoggedIn", true);

                editor.putString("active_name", savedName);
                editor.putString("active_email", savedEmail);

                editor.apply();

                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email atau Password salah! (Atau akun belum didaftarkan)", Toast.LENGTH_SHORT).show();
            }
        });
        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}