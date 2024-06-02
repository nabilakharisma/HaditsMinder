package com.example.finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.DetailActivity;
import com.example.finalproject.Model.HadistModel;
import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final FragmentManager fragmentManager;
    private List<HadistModel> hadistModels;
    private Context context;
    private final int userId;

    public FavoriteAdapter(FragmentManager fragmentManager, List<HadistModel> hadistModels, int userId) {
        this.fragmentManager = fragmentManager;
        this.hadistModels = hadistModels;
        this.userId = userId;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        HadistModel hadistModel = hadistModels.get(position);
        holder.bind(hadistModel);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("hadistModel", hadistModel);
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return hadistModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_judulInggris, tv_judulArab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_judulInggris = itemView.findViewById(R.id.tv_judulhadistinggris1);
            tv_judulArab = itemView.findViewById(R.id.tv_judulhadistarab1);
        }

        public void bind(HadistModel hadistModel) {
            tv_judulInggris.setText(hadistModel.getChapterEnglish());
            tv_judulArab.setText(hadistModel.getChapterArabic());
        }
    }
}
