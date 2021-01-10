package com.example.travel_app_secondapp.ui.historyTravels;

import android.content.Intent;
import android.net.Uri;
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
import com.example.travel_app_secondapp.adapters.historyAdapter;
import com.example.travel_app_secondapp.adapters.registeredAdapter;
import com.example.travel_app_secondapp.databinding.FragmentTravelsHistoryBinding;
import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.ui.MainActivity;
import com.example.travel_app_secondapp.ui.TravelViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HistoryTravelsFragment extends Fragment implements historyAdapter.IHistory {

    private final List<Travel> travelList = new ArrayList<>();
    private TravelViewModel historyTravelsViewModel;
    private String TAG = "HistoryTravelsFragment";
    FragmentTravelsHistoryBinding historyBinding;
    MainActivity parentActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyTravelsViewModel =
                new ViewModelProvider(this).get(TravelViewModel.class);

        historyBinding = FragmentTravelsHistoryBinding.inflate(inflater,container,false);
        historyAdapter adapter = new historyAdapter(travelList,this);
        historyBinding.registeredRecyclerView.setAdapter(adapter);

        parentActivity = (MainActivity) getActivity();

        historyTravelsViewModel.getAllHistoryTravels().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                travelList.clear();
                travelList.addAll(travels);
                adapter.notifyDataSetChanged();
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
        return historyBinding.getRoot();
    }

    @Override
    public void send(Travel travel) {
        travel.setRequestType(Travel.RequestType.paid);
        historyTravelsViewModel.updateTravel(travel);
    }

    @Override
    public void sendEmail(String emailAddress){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Demand a commission in exchange for using \"Travel App\"");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello, thanks for using our application \"Travel App\"!\n we notify you that you need to pay the commission for using our application.");
        if (intent.resolveActivity(parentActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}