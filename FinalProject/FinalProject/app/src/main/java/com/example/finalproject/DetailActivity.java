package com.example.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.Model.HadistModel;
import com.example.finalproject.Sqlite.DbConfig;

public class DetailActivity extends AppCompatActivity {
    private boolean isFavorite = false;
    TextView tv_judulArab, tv_judulInggris, tv_hadistArah, tv_hadistInggirs;
    ImageView iv_favorite;
    DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_judulArab = findViewById(R.id.tv_judulhadistarab);
        tv_judulInggris = findViewById(R.id.tv_judulhadistinggris);
        tv_hadistArah = findViewById(R.id.tv_isihadistarab);
        tv_hadistInggirs = findViewById(R.id.tv_isihadistinggris);
        iv_favorite = findViewById(R.id.iv_favorite);

        dbConfig = new DbConfig(this);

        iv_favorite.setOnClickListener(v -> {
            toggleFavorite();
        });

        HadistModel hadistModel = getIntent().getParcelableExtra("hadistModel");
        if (hadistModel != null) {
            displayHadistDetails(hadistModel);
            int loggedInUserId = getLoggedInUserId();
            isFavorite = isHadistFavorite(loggedInUserId, hadistModel.get_Id());
            updateFavoriteIcon();
        }
    }

    private void displayHadistDetails(HadistModel hadistModel) {
        tv_judulInggris.setText(hadistModel.getChapterEnglish());
        tv_judulArab.setText(hadistModel.getChapterArabic());
        tv_hadistInggirs.setText(hadistModel.getEnglishHadith());
        tv_hadistArah.setText(hadistModel.getArabicHadith());
    }

    private void toggleFavorite() {
        int loggedInUserId = getLoggedInUserId();
        HadistModel hadistModel = getIntent().getParcelableExtra("hadistModel");

        if (hadistModel != null) {
            if (isFavorite) {
                dbConfig.deleteFavorite(loggedInUserId, hadistModel.get_Id());
                iv_favorite.setImageResource(R.drawable.bookmark);
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                dbConfig.insertFavorite(loggedInUserId, hadistModel.get_Id());
                iv_favorite.setImageResource(R.drawable.bookmark1);
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            isFavorite = !isFavorite;
        }
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            iv_favorite.setImageResource(R.drawable.bookmark1);
        } else {
            iv_favorite.setImageResource(R.drawable.bookmark);
        }
    }

    private boolean isHadistFavorite(int userId, String hadistId) {
        return dbConfig.isFavorite(userId, hadistId);
    }

    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", 0);
    }
}
