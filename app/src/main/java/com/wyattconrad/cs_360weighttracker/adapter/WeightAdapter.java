package com.wyattconrad.cs_360weighttracker.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.model.Weight;
import com.wyattconrad.cs_360weighttracker.viewmodel.WeightListViewModel;

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

    WeightListViewModel weightListViewModel;

    public WeightAdapter(Application application) {
        weightListViewModel = new WeightListViewModel(application);
    }

    // Setter for weight list
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
        // If the view type is the header, do nothing
        if (holder.getItemViewType() != VIEW_TYPE_ITEM) { return; }

        // Bind weight data to the weight item view (subtract 1 for the header)
        Weight weight = weightList.get(position - 1);

        // Create a weight view holder from the provided view holder
        WeightViewHolder weightHolder = (WeightViewHolder) holder;

        // Appending "lbs." to the weight text
        weightHolder.weightText.setText(String.format("%s lbs.", weight.getWeight()));

        // Convert the timestamp to a formatted date and time string
        weightHolder.dateText.setText(weight.getDateTimeLogged().toString());

        // Set up the edit button click listener
        weightHolder.editButton.setOnClickListener(v -> {

            // Create a dialog to get the new weight value
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Edit Weight");

            // Create an input field for the new weight value and format it
            final EditText input = new EditText(v.getContext());
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            input.setText(String.valueOf(weight.getWeight()));
            input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            input.setHint("Enter new weight");
            input.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);

            // Set the view
            builder.setView(input);

            // Set up the save button click listener
            builder.setPositiveButton("Save", null);

            // Set up the cancel button click listener
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            // Create the dialog and show it on screen
            AlertDialog dialog = builder.create();
            dialog.show();

            // Setup a listener for the positive button and validate the input
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                // Get the input text and trim any leading or trailing whitespace
                String weightText = input.getText().toString().trim();

                // Validate decimals
                if (weightText.contains(".")) {
                    int index = weightText.indexOf(".");
                    if (index >= 0 && index < weightText.length() - 1) {
                        int decimals = weightText.length() - index - 1;
                        if (decimals > 2) {
                            String newText = weightText.substring(0, index + 3);
                            input.setText(newText);
                            input.setSelection(newText.length());
                            weightText = newText;
                        }
                    }
                }

                // Validate weight range
                try {
                    double newValue = Double.parseDouble(weightText);
                    if (newValue <= 0) {
                        input.setError("Weight must be greater than 0");
                        return;
                    } else if (newValue > 999) {
                        input.setError("Weight must be less than 1000");
                        return;
                    }

                    // Update the weight since no errors were found
                    input.setError(null);
                    weight.setWeight(newValue);
                    weightListViewModel.updateWeight(weight);
                    // Close the dialog
                    dialog.dismiss();

                } catch (NumberFormatException e) {
                    // Handle invalid input
                    input.setError("Invalid number format");
                }
            });


        });

        // Set up the delete button click listener
        weightHolder.deleteButton.setOnClickListener(v -> {

            // Create a confirmation dialog
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this weight entry?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Call ViewModel or Repository to delete
                        weightListViewModel.deleteWeight(weight);

                        // Update the list and notify adapter
                        weightList.remove(position - 1);
                        notifyItemRemoved(position - 1);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

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
        ImageButton deleteButton;
        ImageButton editButton;

        public WeightViewHolder(View itemView) {
            super(itemView);
            // Initialize weight and date text views
            weightText = itemView.findViewById(R.id.textWeight);
            dateText = itemView.findViewById(R.id.textDate);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }

}
