package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.Sqlite.DbConfig;

public class LoginActivity extends AppCompatActivity {

    EditText et_username;
    EditText et_password;
    Button btn_login;
    Button btn_register;
    DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing the views
        et_username = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        dbConfig = new DbConfig(this);

        btn_register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btn_login.setOnClickListener(v -> {
            String username = et_username.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (username.isEmpty()) {
                et_username.setError("Masukkan Username!");
            } else if (password.isEmpty()) {
                et_password.setError("Masukkan Password!");
            } else {
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        SQLiteDatabase db = dbConfig.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    DbConfig.TABLE_NAME,
                    new String[]{DbConfig.COLUMN_ID},
                    DbConfig.COLUMN_USERNAME + "=? AND " + DbConfig.COLUMN_PASSWORD + "=?",
                    new String[]{username, password},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex(DbConfig.COLUMN_ID);
                if (idColumnIndex != -1) {
                    int userId = cursor.getInt(idColumnIndex);
                    loginSuccess(userId);
                    updateLoginStatus(username, true);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private void updateLoginStatus(String username, boolean isLoggedIn) {
        SQLiteDatabase db = dbConfig.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(DbConfig.COLUMN_IS_LOGGED_IN, isLoggedIn ? 1 : 0);
            db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_USERNAME + " = ?", new String[]{username});
        } finally {
            db.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        SQLiteDatabase db = dbConfig.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    DbConfig.TABLE_NAME,
                    new String[]{DbConfig.COLUMN_ID},
                    DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                    new String[]{"1"},
                    null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private void loginSuccess(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", userId);
        editor.apply();
    }
}
