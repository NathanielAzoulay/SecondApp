package com.example.travel_app_secondapp.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app_secondapp.entities.Travel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TravelFirebaseDataSource implements  ITravelDataSource{

    private static final String TAG = "Firebase";

    private MutableLiveData<Boolean> isSuccess= new MutableLiveData<>();
    private List<Travel> allTravelsList;

    private NotifyToTravelListListener notifyToTravelListListener;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference travels = firebaseDatabase.getReference("ExistingTravels");

    private static TravelFirebaseDataSource instance;

    ValueEventListener valueEventListener = new ValueEventListener() {
        // for each change in the reference to travelRequests this event will activate
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            allTravelsList.clear(); // clear the old one
            if (dataSnapshot.exists()) { // there is any thing there
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Travel travel = snapshot.getValue(Travel.class); //brings all the items
                    // TODO: check if //
                    travel.setTravelId(snapshot.getKey());
                    allTravelsList.add(travel); // to the list to be presented
                }
            }
            if (notifyToTravelListListener != null) // if there was something that change there
                notifyToTravelListListener.onTravelsChanged(); // notify the listener
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /**
     * singleton attribute
     * @return the instance
     */
    public static TravelFirebaseDataSource getInstance() {
        if (instance == null)
            instance = new TravelFirebaseDataSource();
        return instance;
    }


    /**
     * Although we need to do the filtering in the firebase's server instead of here
     * this requirement is out side of our course frames
     */
    private TravelFirebaseDataSource() {
        allTravelsList = new ArrayList<>();
        travels.addValueEventListener(valueEventListener);
    }


      public void setNotifyToTravelListListener(NotifyToTravelListListener l) {
        notifyToTravelListListener = l;
    }


    @Override
    public void addTravel(Travel p) {
        String id = travels.push().getKey();
        p.setTravelId(id);
        travels.child(id).setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "Travel Added");
                isSuccess.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isSuccess.setValue(false);
            }
        });
    }

    @Override
    public  void removeTravel(String id) {
          travels.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                       Log.e(TAG, "Travel Removed");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Failure removing Travel");
                                }
                            });
    }


    /**
     * function that updates the travel in the realtime database (by remove and add)
     * @param toUpdate
     */
    @Override
    public void updateTravel(final Travel toUpdate) {
             removeTravel(toUpdate.getTravelId());
             addTravel(toUpdate);
    }

    @Override
    public List<Travel> getAllTravels() {
        return allTravelsList;
    }

    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }




}
