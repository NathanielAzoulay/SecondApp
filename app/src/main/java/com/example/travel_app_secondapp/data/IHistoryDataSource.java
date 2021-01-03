package com.example.travel_app_secondapp.data;

import androidx.lifecycle.LiveData;

import com.example.travel_app_secondapp.entities.Travel;

import java.util.List;

public interface IHistoryDataSource {
    public void addTravel(Travel p);

    public void addTravel(List<Travel> travelList);

    public void editTravel(Travel p);

    public void deleteTravel(Travel p);

    public void clearTable();

    public LiveData<List<Travel>> getTravels();
}
