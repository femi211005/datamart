package com.example.datamart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Menghubungkan ID dari XML
        TextInputEditText etEmail = findViewById(R.id.etEmailLogin);
        TextInputEditText etPassword = findViewById(R.id.etPasswordLogin);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvGoToRegister);

        // Aksi ketika tombol Masuk diklik
        btnLogin.setOnClickListener(v -> {
            String emailInput = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String passwordInput = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

            // BUKA BRANKAS UNTUK MENGAMBIL DATA
            SharedPreferences userPrefs = getSharedPreferences("AkunApp", MODE_PRIVATE);
            String savedEmail = userPrefs.getString("email", "");
            String savedPassword = userPrefs.getString("password", "");

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
            // COCOKKAN DATA INPUT DENGAN BRANKAS
            else if (emailInput.equals(savedEmail) && passwordInput.equals(savedPassword)) {
                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                // Pindah ke Beranda
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email atau Password salah! (Atau akun belum didaftarkan)", Toast.LENGTH_SHORT).show();
            }
        });

        // Aksi ketika teks "Daftar sekarang" diklik
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}