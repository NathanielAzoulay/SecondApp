package com.example.travel_app_secondapp.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.travel_app_secondapp.data.ITravelRepository;
import com.example.travel_app_secondapp.data.TravelRepository;
import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.entities.UserLocation;

import java.util.List;

public class TravelViewModel extends AndroidViewModel {

    ITravelRepository travelsRepository;
    public TravelViewModel(Application application) {
        super(application);
        travelsRepository =  TravelRepository.getInstance(application);
    }

    public void addTravel(Travel travel)
    {
        travelsRepository.addTravel(travel);
    }
    public void updateTravel(Travel travel)
    {
        travelsRepository.updateTravel(travel);
    }
    public void removeTravel(Travel travel)
    {
        travelsRepository.removeTravel(travel);
    }


    public LiveData<List<Travel>> getAllRegisteredTravels(String userEmail) {
        return travelsRepository.getAllRegisteredTravels(userEmail);
    }
    public LiveData<List<Travel>> getAllCompanyTravels(UserLocation userLocation, double maxDist) {
        return (LiveData<List<Travel>>)travelsRepository.getAllCompanyTravels(userLocation, maxDist);
    }
    public LiveData<List<Travel>> getAllHistoryTravels() {
        return (LiveData<List<Travel>>)travelsRepository.getAllHistoryTravels();
    }


    MutableLiveData<Boolean> getIsSuccess()
    {
        return travelsRepository.getIsSuccess();
    }

}