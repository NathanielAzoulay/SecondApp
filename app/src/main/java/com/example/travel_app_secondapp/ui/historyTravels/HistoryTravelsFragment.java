package com.example.travel_app_secondapp.ui.historyTravels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app_secondapp.R;

public class HistoryTravelsFragment extends Fragment {

    private HistoryTravelsViewModel historyTravelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyTravelsViewModel =
                new ViewModelProvider(this).get(HistoryTravelsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_travels_history, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        historyTravelsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}