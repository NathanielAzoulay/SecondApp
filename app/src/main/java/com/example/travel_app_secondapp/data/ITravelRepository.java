package com.example.travel_app_secondapp.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.entities.UserLocation;

import java.util.List;
/**
 * interfaces allows replacement of the realization of the bottom layer
 * without having to make any change in the layer above it,
 * which makes use of it and thus realize the principle By Design By Contract.
 **/
public interface ITravelRepository {

    void addTravel(Travel travel);
    void updateTravel(Travel travel);
    void removeTravel(Travel travel);

    LiveData<List<Travel>> getAllTravels(boolean fireBase);
    LiveData<List<Travel>> getAllRegisteredTravels(String userEmail);
    LiveData<List<Travel>> getAllCompanyTravels(UserLocation userLocation, double max);
    LiveData<List<Travel>> getAllHistoryTravels();
    MutableLiveData<Boolean> getIsSuccess();
}
