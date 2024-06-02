package com.example.finalproject.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.Adapter.FavoriteAdapter;
import com.example.finalproject.Api.ApiConfig;
import com.example.finalproject.Api.ApiService;
import com.example.finalproject.Model.HadistModel;
import com.example.finalproject.R;
import com.example.finalproject.Response.HadistResponse;
import com.example.finalproject.Sqlite.DbConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {

    private FavoriteAdapter favoriteAdapter;
    private RecyclerView recyclerView;
    private DbConfig dbConfig;
    private ApiService service;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        service = ApiConfig.getClient();

        dbConfig = new DbConfig(requireActivity());
        preferences = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);

        refreshFavoriteResto();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment is resumed
        refreshFavoriteResto();
    }

    private void refreshFavoriteResto() {
        int userId = preferences.getInt("user_id", 0);

        Cursor cursor = dbConfig.getFavoritemHadistByUserId(userId);

        ArrayList<String> favoriteHadist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String restoId = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_HADIST_ID));
                Log.d("kkk", restoId);
                favoriteHadist.add(restoId);
            } while (cursor.moveToNext());
        }

        recyclerView.setVisibility(View.GONE);
        Call<HadistResponse> call = service.getHadist();
        call.enqueue(new Callback<HadistResponse>() {
            @Override
            public void onResponse(@NonNull Call<HadistResponse> call, @NonNull Response<HadistResponse> response) {
                if (response.isSuccessful() && isAdded()) {
                    HadistResponse hadistResponse = response.body();
                    List<HadistModel> hadistModels = hadistResponse.getHadiths();
                    List<HadistModel> hadistFavorite = new ArrayList<>();
                    if (hadistModels != null) {
                        for (HadistModel hadist : hadistModels) {
                            if (favoriteHadist.contains(hadist.get_Id())) {
                                hadistFavorite.add(hadist);
                            }
                        }
                    }

                    favoriteAdapter = new FavoriteAdapter(getParentFragmentManager(), hadistFavorite, userId);
                    recyclerView.setAdapter(favoriteAdapter);

                    if (hadistFavorite.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HadistResponse> call, @NonNull Throwable t) {
                Log.e("FavoriteFragment", "Error fetching favorite restos: " + t.getMessage());
            }
        });
    }
}
