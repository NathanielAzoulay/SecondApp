package com.example.travel_app_secondapp.ui.registeredTravels;

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
import com.example.travel_app_secondapp.data.UserViewModelFactory;
import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.ui.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RegisteredTravelsFragment extends Fragment {
    private final List<Travel> travelList = new ArrayList<>();
    private RegisteredTravelsViewModel registeredTravelsViewModel;
    private String TAG = "RegisteredTravelsFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        MainActivity parentActivity = (MainActivity) getActivity();
        // TODO: add catch exception for this if fail
        registeredTravelsViewModel =
                new ViewModelProvider(this, new UserViewModelFactory(parentActivity.getApplication(), parentActivity.getUserEmail())).get(RegisteredTravelsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_travels_registered, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UpdateForRoomDataBase();
            }
        });
        registeredTravelsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        registeredTravelsViewModel.getAllTravels().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
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



    public void getTravelRequests(){

    }

    public void UpdateForRoomDataBase() {
        Travel travel = travelList.get(0);
        travel.setRequestType(Travel.RequestType.close);
        registeredTravelsViewModel.updateTravel(travel);
    }



}