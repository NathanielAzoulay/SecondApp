package com.example.travel_app_secondapp.data;

import androidx.lifecycle.MutableLiveData;

import com.example.travel_app_secondapp.entities.Travel;

import java.util.List;

/**
 * interfaces allows replacement of the realization of the bottom layer
 * without having to make any change in the layer above it,
 * which makes use of it and thus realize the principle By Design By Contract.
 **/
public interface ITravelDataSource {
    void addTravel(Travel travel);
    void updateTravel(Travel travel);
    void removeTravel(String travelId);
    List<Travel> getAllTravels();
    MutableLiveData<Boolean> getIsSuccess();
    interface NotifyToTravelListListener {
        void onTravelsChanged();
    }
    void setNotifyToTravelListListener(NotifyToTravelListListener l);
  }
