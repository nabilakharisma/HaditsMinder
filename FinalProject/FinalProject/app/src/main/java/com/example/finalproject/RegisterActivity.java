package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.Sqlite.DbConfig;

public class RegisterActivity extends AppCompatActivity {

    EditText et_nim;
    EditText et_password;
    Button btn_register;

    DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Correctly initialize views
        et_nim = findViewById(R.id.et_userRegister);
        et_password = findViewById(R.id.et_passwordResgister);
        btn_register = findViewById(R.id.btn_register2);

        dbConfig = new DbConfig(this);

        btn_register.setOnClickListener(v -> {
            String nim = et_nim.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (!nim.isEmpty() && !password.isEmpty()) {
                dbConfig.insertData(nim, password);
                Toast.makeText(RegisterActivity.this, "Register Sukses", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Masukkan Nim dan Password!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
