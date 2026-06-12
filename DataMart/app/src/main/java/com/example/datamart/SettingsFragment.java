package com.example.datamart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {

    private ImageView ivProfilePhoto;
    private TextView tvName;
    private TextView tvEmail;

    private SharedPreferences userPrefs;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedUri = result.getData().getData();
                    if (selectedUri != null && getContext() != null) {
                        try {
                            getContext().getContentResolver().takePersistableUriPermission(selectedUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (SecurityException e) {
                            Log.e("SettingsFragment", "Permission error", e);
                        }

                        Glide.with(this)
                                .load(selectedUri)
                                .error(android.R.drawable.ic_menu_gallery)
                                .into(ivProfilePhoto);

                        userPrefs.edit().putString("foto_uri", selectedUri.toString()).apply();
                        Toast.makeText(getContext(), "Profil diperbarui!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // FIXED: Diarahkan murni ke fragment_settings sesuai dengan nama file XML kamu
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        tvName = view.findViewById(R.id.tvProfileName);
        tvEmail = view.findViewById(R.id.tvProfileEmail);
        MaterialButton btnEdit = view.findViewById(R.id.btnEditProfile);
        SwitchMaterial switchTheme = view.findViewById(R.id.switchTheme);
        MaterialButton btnLogout = view.findViewById(R.id.btnLogout);

        if (getActivity() != null) {
            userPrefs = getActivity().getSharedPreferences("AkunApp", Context.MODE_PRIVATE);
            SharedPreferences themePrefs = getActivity().getSharedPreferences("TemaApp", Context.MODE_PRIVATE);

            String savedName = userPrefs.getString("active_name", userPrefs.getString("nama", "Pengguna Lumina"));
            String savedEmail = userPrefs.getString("active_email", userPrefs.getString("email", "email@lumina.com"));
            String savedPhotoUri = userPrefs.getString("foto_uri", null);

            if (tvName != null) tvName.setText(savedName);
            if (tvEmail != null) tvEmail.setText(savedEmail);

            if (savedPhotoUri != null && ivProfilePhoto != null && getContext() != null) {
                Glide.with(this)
                        .load(Uri.parse(savedPhotoUri))
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(ivProfilePhoto);
            }

            if (switchTheme != null) {
                boolean isDarkMode = themePrefs.getBoolean("dark_mode", false);
                switchTheme.setChecked(isDarkMode);
                switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    themePrefs.edit().putBoolean("dark_mode", isChecked).apply();
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                });
            }
        }

        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                galleryLauncher.launch(intent);
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                if (userPrefs != null) {
                    userPrefs.edit().putBoolean("isLoggedIn", false).apply();
                }
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            });
        }

        return view;
    }
}