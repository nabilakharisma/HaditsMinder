package com.example.finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.DetailActivity;
import com.example.finalproject.Model.HadistModel;
import com.example.finalproject.R;

import java.util.ArrayList;

public class HadistAdapter extends RecyclerView.Adapter<HadistAdapter.ViewHolder> {

    private ArrayList<HadistModel> hadistModels;
    Context context;

    public HadistAdapter(ArrayList<HadistModel> hadistModels, Context context) {
        this.hadistModels = hadistModels;
        this.context = context;
    }

    @NonNull
    @Override
    public HadistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hadist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HadistAdapter.ViewHolder holder, int position) {
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
            tv_judulInggris = itemView.findViewById(R.id.tv_judulhadistinggris0);
            tv_judulArab = itemView.findViewById(R.id.tv_judulhadistarab0);
        }

        public void bind(HadistModel hadistModel) {
            tv_judulInggris.setText(hadistModel.getChapterEnglish());
            tv_judulArab.setText(hadistModel.getChapterArabic());
        }
    }
}
