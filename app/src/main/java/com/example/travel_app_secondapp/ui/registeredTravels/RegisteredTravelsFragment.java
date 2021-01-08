package com.example.travel_app_secondapp.ui.registeredTravels;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.adapters.AdapterViewBindingAdapter;
import androidx.databinding.adapters.ViewBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app_secondapp.R;
import com.example.travel_app_secondapp.adapters.registeredAdapter;
import com.example.travel_app_secondapp.databinding.FragmentTravelsRegisteredBinding;
import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.ui.MainActivity;

import com.example.travel_app_secondapp.adapters.registeredAdapter.IRegistered;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class RegisteredTravelsFragment extends Fragment implements IRegistered{//, AdapterView.OnItemSelectedListener {

    private final List<Travel> travelList = new ArrayList<>();
    private RegisteredTravelsViewModel registeredTravelsViewModel;
    private String TAG = "RegisteredTravelsFragment";
    private MainActivity parentActivity;
    FragmentTravelsRegisteredBinding registeredBinding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        parentActivity = (MainActivity) getActivity();
        // TODO: add catch exception for this if fail
        registeredTravelsViewModel = new ViewModelProvider(this).get(RegisteredTravelsViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_travels_registered, container, false);

        registeredBinding = FragmentTravelsRegisteredBinding.inflate(inflater,container,false);
        registeredAdapter adapter = new registeredAdapter(travelList,this);
        registeredBinding.registeredRecyclerView.setAdapter(adapter);

        registeredTravelsViewModel.getAllRegisteredTravels(parentActivity.getUserEmail()).observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                travelList.clear();
                travelList.addAll(travels);
                adapter.notifyDataSetChanged();
                for (Travel tmp : travels) {
                    Log.e(TAG, tmp.getClientName() + ":  ");
                    Log.e(TAG, tmp.getTravelId());
                    Log.e(TAG, tmp.getClientPhone());
                    Log.e(TAG, tmp.getRequestType().toString());
                    Log.e(TAG, tmp.getArrivalDate() + "\n");
                    //https://www.callicoder.com/java-hashmap/
                    //HashMap is a hash table based implementation of Java’s Map interface
                    HashMap<String, Boolean> company = tmp.getCompany();
                    if (company != null) {
                        Iterator it = tmp.getCompany().entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            System.out.println("HashMap:  " + pair.getKey() + " = " + pair.getValue());
                        }
                    }
                }
            }
        });
        return registeredBinding.getRoot();
    }


    // TODO: logical business should be at least in viewModel
    public void UpdateCompanyTravel() throws NullPointerException {
        //get position from recyclerView
        //get position from spinner
        Travel travel = travelList.get(0);
        String companyID = "??";
        Iterator it = travel.getCompany().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            pair.setValue(false);
        }
        travel.getCompany().put(companyID, true);
        registeredTravelsViewModel.updateTravel(travel);
    }

    // TODO: logical business should be at least in viewModel
    public void TravelAccepted() {
        //get position from recyclerView
        Travel travel = travelList.get(0);
        travel.setRequestType(Travel.RequestType.accepted);
        registeredTravelsViewModel.updateTravel(travel);
    }


    @Override
    public void accept(String selectedItem, Travel travel) {
        Toast.makeText(parentActivity.getBaseContext(),selectedItem , Toast.LENGTH_LONG).show();
        travel.getCompany().clear();
        travel.getCompany().put(selectedItem, true);
        travel.setRequestType(Travel.RequestType.accepted);
        registeredTravelsViewModel.updateTravel(travel);
    }

}