package com.example.travel_app_secondapp.ui.historyTravels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoryTravelsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HistoryTravelsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is history travels fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}