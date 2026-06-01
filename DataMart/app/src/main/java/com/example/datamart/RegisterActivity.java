package com.example.datamart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Menghubungkan ID dari XML
        TextInputEditText etName = findViewById(R.id.etNameReg);
        TextInputEditText etEmail = findViewById(R.id.etEmailReg);
        TextInputEditText etPassword = findViewById(R.id.etPasswordReg);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        TextView tvLogin = findViewById(R.id.tvLogin);
        CheckBox cbTerms = findViewById(R.id.cbTerms);

        // Aksi ketika tombol Daftar diklik
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show();
            } else if (!cbTerms.isChecked()) {
                Toast.makeText(this, "Harap setujui Syarat & Ketentuan", Toast.LENGTH_SHORT).show();
            } else {
                // SIMPAN DATA KE BRANKAS (SharedPreferences)
                SharedPreferences userPrefs = getSharedPreferences("AkunApp", MODE_PRIVATE);
                SharedPreferences.Editor editor = userPrefs.edit();
                editor.putString("nama", name);
                editor.putString("email", email);
                editor.putString("password", password);
                editor.apply();

                Toast.makeText(this, "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_SHORT).show();

                // Kembali ke halaman Login
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                startActivity(intent);
                finish();
            }
        });

        // Aksi ketika teks "Masuk di sini" diklik
        tvLogin.setOnClickListener(v -> {
            finish();
        });
    }
}