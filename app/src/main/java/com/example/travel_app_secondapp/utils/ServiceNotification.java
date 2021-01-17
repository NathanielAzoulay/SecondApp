package com.example.travel_app_secondapp.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.travel_app_secondapp.data.TravelFirebaseDataSource;
import com.example.travel_app_secondapp.entities.Travel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class ServiceNotification extends Service {
    String companyName;
    boolean isThreadOn = false;
    public final String TAG = "Service Notification";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isThreadOn && intent != null) // don't know why intent is null when destroy application
        {
            Bundle extras = intent.getExtras();
            companyName = extras.getString("companyName");
            isThreadOn = true;
            FireBaseListener fireBaseListener = new FireBaseListener();
            fireBaseListener.run();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public class FireBaseListener extends Thread {

        public void run() {
            //TODO 1 notification on start
            TravelFirebaseDataSource travelFirebaseDataSource = TravelFirebaseDataSource.getInstance();
            travelFirebaseDataSource.setServiceListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    Travel travel = dataSnapshot.getValue(Travel.class);
                    boolean check = travel.getCompany() == null ? false :
                            travel.getCompany().get(companyName) == null ? false : travel.getCompany().get(companyName);
                    if (travel.getRequestType() == Travel.RequestType.accepted && check) {
                        Intent intent = new Intent("testIntent");
                        intent.putExtra("ClientName", travel.getClientName());
                        intent.setAction("com.android.ACCEPTED_OFFER");
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


            isThreadOn = false;
        }
    }
}