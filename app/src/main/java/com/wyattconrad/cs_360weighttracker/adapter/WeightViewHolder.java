package com.wyattconrad.cs_360weighttracker.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.wyattconrad.cs_360weighttracker.R;

public class WeightViewHolder extends RecyclerView.ViewHolder {
    private final TextView weightTextView;
    private final TextView dateTextView;

    public WeightViewHolder(View itemView) {
        super(itemView);
        weightTextView = itemView.findViewById(R.id.textWeight);
        dateTextView = itemView.findViewById(R.id.textDate);
    }
}
