package com.example.travel_app_secondapp.ui.historyTravels;

import android.os.Bundle;
import android.util.Log;
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
import com.example.travel_app_secondapp.entities.Travel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HistoryTravelsFragment extends Fragment {

    private final List<Travel> travelList = new ArrayList<>();
    private HistoryTravelsViewModel historyTravelsViewModel;
    private String TAG = "HistoryTravelsFragment";
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

        historyTravelsViewModel.getAllHistoryTravels().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                travelList.clear();
                travelList.addAll(travels);
                for (Travel tmp : travels) {
                    Log.e(TAG, tmp.getClientName() + ":  ");
                    Log.e(TAG, tmp.getTravelId() );
                    Log.e(TAG, tmp.getClientPhone() );
                    Log.e(TAG, tmp.getRequestType().toString() );
                    Log.e(TAG, tmp.getArrivalDate() +"\n");
                    //https://www.callicoder.com/java-hashmap/
                    //HashMap is a hash table based implementation of Java’s Map interface
                    HashMap<String, Boolean> company = tmp.getCompany();
                    if(company != null){
                        Iterator it = tmp.getCompany().entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            System.out.println("HashMap:  " + pair.getKey() + " = " + pair.getValue());
                        }
                    }
                }
            }});

        return root;
    }
}