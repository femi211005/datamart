package com.example.datamart;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class SuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        MaterialButton btnBackToHome = findViewById(R.id.btnBackToHome);
        if (btnBackToHome != null) {
            btnBackToHome.setOnClickListener(v -> {
                // Kembali ke halaman beranda utama dan hapus seluruh tumpukan halaman checkout lama
                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}