package com.cmteam4.throughoutportugal.card_view_regions;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmteam4.throughoutportugal.InfoRegion;
import com.cmteam4.throughoutportugal.R;

import java.util.List;

public class AdapterRegion extends RecyclerView.Adapter<AdapterRegion.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<String> data;

    AdapterRegion(Context context, List<String> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.region_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String selectedRegion = data.get(position);

        holder.textViewName.setText(selectedRegion);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLICOU NA REGIAO", selectedRegion);
                Intent intent = new Intent(v.getContext(), InfoRegion.class);
                intent.putExtra("REGION_POINT", selectedRegion);
                v.getContext().startActivity(intent);
            }
        });

        //viewAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.txtNameRegion);

        }

    }

}
