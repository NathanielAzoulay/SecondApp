package com.example.travel_app_secondapp.ui.historyTravels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.travel_app_secondapp.data.ITravelRepository;
import com.example.travel_app_secondapp.data.TravelRepository;
import com.example.travel_app_secondapp.entities.Travel;
import java.util.List;

public class HistoryTravelsViewModel extends AndroidViewModel {

    ITravelRepository travelsRepository;

    public HistoryTravelsViewModel(Application application) {
        super(application);
        travelsRepository =  TravelRepository.getInstance(application);
    }

    void updateTravel(Travel travel)
    {
        travelsRepository.updateTravel(travel);
    }

    void removeTravel(Travel travel)
    {
        travelsRepository.removeTravel(travel);
    }
    
    LiveData<List<Travel>> getAllHistoryTravels() {
        return (LiveData<List<Travel>>)travelsRepository.getAllHistoryTravels();
    }
    MutableLiveData<Boolean> getIsSuccess()
    {
        return travelsRepository.getIsSuccess();
    }
}