package com.example.travel_app_secondapp.ui.companyTravels;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app_secondapp.R;
import com.example.travel_app_secondapp.data.UserViewModelFactory;
import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.entities.UserLocation;
import com.example.travel_app_secondapp.ui.MainActivity;
import com.example.travel_app_secondapp.ui.registeredTravels.RegisteredTravelsViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Thread.sleep;

public class CompanyTravelsFragment extends Fragment {

    private final List<Travel> travelList = new ArrayList<>();
    private CompanyTravelsViewModel companyTravelsViewModel;
    private String TAG = "CompanyTravelsFragment";

    double curLatitude = 0, curLongitude = 0;
    String userEmail;
    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    // Define a listener that responds to location updates
    LocationListener locationListener;
    MainActivity parentActivity;
    UserLocation userLocation;
    private final double MAX_DIST = 800.0;

    private FusedLocationProviderClient fusedLocationClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        parentActivity = (MainActivity) getActivity();
        locationManager = (LocationManager) parentActivity.getSystemService(Context.LOCATION_SERVICE);
        userLocation = new UserLocation(curLatitude, curLongitude);

        userEmail = parentActivity.getUserEmail();
        companyTravelsViewModel = new ViewModelProvider(this).get(CompanyTravelsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_travels_registered, container, false);
        final TextView textView = root.findViewById(R.id.text_home);



        companyTravelsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        companyTravelsViewModel.getAllCompanyTravels(userLocation, MAX_DIST).observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                travelList.clear();
                travelList.addAll(travels);
                for (Travel tmp : travels) {
                    Log.e(TAG, tmp.getClientName() + ":  ");
                    Log.e(TAG, tmp.getTravelId());
                    Log.e(TAG, tmp.getClientPhone());
                    Log.e(TAG, tmp.getRequestType().toString());
                    Log.e(TAG, tmp.getArrivalDate() + "\n");
                    //https://www.callicoder.com/java-hashmap/
                    //HashMap is a hash table based implementation of Java’s Map interface
                    HashMap<String, Boolean> company = tmp.getCompany();
                    if (company != null) {
                        Iterator it = tmp.getCompany().entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            System.out.println("HashMap:  " + pair.getKey() + " = " + pair.getValue());
                        }
                    }
                }
            }
        });


        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // TODO: check if last location different enough from the current (example: 100 meters)
                // Called when a new location is found by the network location provider.
                Toast.makeText(parentActivity.getBaseContext(), Double.toString(location.getLatitude()) + " : " + Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
                userLocation.setLat(location.getLatitude());
                userLocation.setLon(location.getLongitude());
                // TODO:loader.visibility.GONE or something like that...
                textView.setText(getPlace(location));

                if (companyTravelsViewModel != null)
                    companyTravelsViewModel.getAllCompanyTravels(userLocation, MAX_DIST);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        getLocation();
//        Thread timer = new Thread() {
//            public void run(){
//                try {
//                    sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        timer.start();

        return root;
    }

    private void getLocation() {
        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20*1000, 0, locationListener);

                // TODO: for getting first location when creating the fragment
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient(parentActivity);
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                // Logic to handle location object
//                            }
//                        }
//                    });
            //textView.setText(getPlace(new Location()));

        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                //Toast.makeText(this, "Until you grant the permission, we cannot display the location", Toast.LENGTH_SHORT).show();
            }
        }

    }



    public String getPlace(Location location) {

        Geocoder geocoder = new Geocoder(parentActivity.getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0);
            }

            return "unknown place: \n ("+location.getLongitude()+" , "+location.getLatitude()+")";
        }
        catch(
                IOException e)
        {
            e.printStackTrace();
        }
        return "IOException ...";
    }



    // TODO: logical business should be at least in viewModel
    public void AddCompanyToTravel(){
        //get position from recyclerView
        Travel travel = travelList.get(0); // ..get(position)
        HashMap<String, Boolean> company;
        if(travel.getCompany() == null) {
            company = new HashMap<>();
            travel.setCompany(company);
        }
        else{
            company = travel.getCompany();
        }
        company.put(userEmail, false);
        travel.setCompany(company);
        companyTravelsViewModel.updateTravel(travel);
    }


}