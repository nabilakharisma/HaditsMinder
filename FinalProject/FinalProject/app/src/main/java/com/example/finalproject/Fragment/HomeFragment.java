package com.example.finalproject.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.finalproject.Adapter.HadistAdapter;
import com.example.finalproject.Api.ApiConfig;
import com.example.finalproject.Api.ApiService;
import com.example.finalproject.Model.HadistModel;
import com.example.finalproject.R;
import com.example.finalproject.Response.HadistResponse;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ApiService apiService;
    private HadistAdapter hadistAdapter;
    private RecyclerView recyclerView;
    private Button btn_retry;
    private ArrayList<HadistModel> hadistModels = new ArrayList<>();
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btn_retry = view.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retry button clicked, reload data
                loadHadist();
            }
        });

        apiService = ApiConfig.getClient();

        hadistAdapter = new HadistAdapter(hadistModels, getContext());
        recyclerView.setAdapter(hadistAdapter);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        loadHadist();
    }

    private void loadHadist() {
        btn_retry.setVisibility(View.GONE);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Call<HadistResponse> call = apiService.getHadist();
                call.enqueue(new Callback<HadistResponse>() {
                    @Override
                    public void onResponse(Call<HadistResponse> call, Response<HadistResponse> response) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()) {
                                    HadistResponse data = response.body();
                                    if (data != null && data.getHadiths() != null && !data.getHadiths().isEmpty()) {
                                        hadistModels.addAll(data.getHadiths());
                                        hadistAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getContext(), "Tidak ada data yang tersedia", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                                    btn_retry.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<HadistResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Gagal mengambil data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        btn_retry.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

}
