package com.wyattconrad.cs_360weighttracker.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.model.Weight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constants for view types
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    // Declare a list of weights
    private List<Weight> weightList = new ArrayList<>();

    public void setWeightList(List<Weight> weightList) {
        // Update the weight list and notify the adapter
        if (this.weightList == null) {
            this.weightList = new ArrayList<>();
        } else {
            this.weightList = weightList;
        }
        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a view holder based on the view type
        // This creates a header view and a weight item view
        // Header view type
        if (viewType == VIEW_TYPE_HEADER) {
            // Inflate header layout
            View headerView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.weight_item_header, parent, false);
            return new HeaderViewHolder(headerView);
        }
        // Weight item view type
        else {
            // Inflate item layout
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.weight_item, parent, false);
            return new WeightViewHolder(itemView);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind data to the view holder based on the view type
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            // Bind weight data to the weight item view (subtract 1 for the header)
            Weight weight = weightList.get(position - 1);
            // Set the weight and date text for the weight item view
            WeightViewHolder weightHolder = (WeightViewHolder) holder;
            // Appending "lbs." to the weight text
            weightHolder.weightText.setText(String.format("%s lbs.", weight.getWeight()));
            // Convert the timestamp to a formatted date and time string
            weightHolder.dateText.setText(formatDate(weight.getDateTimeLogged()));
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the adapter (including the header)
        return weightList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // Return the view type based on the position
        if (position == 0) {
            // First position is the header
            return VIEW_TYPE_HEADER;
        } else {
            // Other positions are weight items
            return VIEW_TYPE_ITEM;
        }
    }

    private String formatDate(long timestamp) {
        // Format the timestamp to a readable date and time string
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault());
        // Return the formatted date and time string
        return simpleDateFormat.format(new Date(timestamp)); // Convert timestamp to a formatted date and time string
    }

    // ViewHolder for header
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
            // Initialize header view components (if any)
        }
    }

    // ViewHolder for weight items
    public static class WeightViewHolder extends RecyclerView.ViewHolder {
        // Declare weight and date text views
        TextView weightText, dateText;

        public WeightViewHolder(View itemView) {
            super(itemView);
            // Initialize weight and date text views
            weightText = itemView.findViewById(R.id.textWeight);
            dateText = itemView.findViewById(R.id.textDate);
        }
    }
}
