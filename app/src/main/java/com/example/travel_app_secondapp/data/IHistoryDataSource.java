package com.example.travel_app_secondapp.data;

import androidx.lifecycle.LiveData;

import com.example.travel_app_secondapp.entities.Travel;

import java.util.List;

public interface IHistoryDataSource {
    void addTravel(Travel p);

    void addTravel(List<Travel> travelList);

    void editTravel(Travel p);

    void deleteTravel(Travel p);

    void clearTable();

    LiveData<List<Travel>> getTravels();
}
