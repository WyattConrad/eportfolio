package com.wyattconrad.cs_360weighttracker.ui.addweight;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyattconrad.cs_360weighttracker.R;

public class AddWeightFragment extends Fragment {

    private AddWeightViewModel mViewModel;

    public static AddWeightFragment newInstance() {
        return new AddWeightFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_weight, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddWeightViewModel.class);
        // TODO: Use the ViewModel
    }

}