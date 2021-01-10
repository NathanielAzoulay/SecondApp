package com.example.travel_app_secondapp.ui.companyTravels;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app_secondapp.R;
import com.example.travel_app_secondapp.adapters.companyAdapter;
import com.example.travel_app_secondapp.adapters.registeredAdapter;
import com.example.travel_app_secondapp.databinding.FragmentTravelsCompanyBinding;
import com.example.travel_app_secondapp.databinding.FragmentTravelsRegisteredBinding;
import com.example.travel_app_secondapp.entities.Travel;
import com.example.travel_app_secondapp.entities.UserLocation;
import com.example.travel_app_secondapp.ui.MainActivity;
import com.example.travel_app_secondapp.ui.TravelViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Thread.sleep;

public class CompanyTravelsFragment extends Fragment  implements companyAdapter.ICompany {

    private final List<Travel> travelList = new ArrayList<>();
    private TravelViewModel companyTravelsViewModel;
    private String TAG = "CompanyTravelsFragment";
    double curLatitude = 0, curLongitude = 0;
    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    // Define a listener that responds to location updates
    LocationListener locationListener;
    MainActivity parentActivity;
    String companyName;
    UserLocation userLocation;
    private final double MAX_DIST = 8000.0;
    double max_distance = 0;
    FragmentTravelsCompanyBinding companyBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        parentActivity = (MainActivity) getActivity();
        // takes only the name of the email address (example: monalisa@gmail.com -> monalisa)
        companyName = parentActivity.getUserEmail().replaceAll("@[a-z]+\\.+[a-z]+", "");
        locationManager = (LocationManager) parentActivity.getSystemService(Context.LOCATION_SERVICE);
        userLocation = new UserLocation(curLatitude, curLongitude);

        companyTravelsViewModel = new ViewModelProvider(this).get(TravelViewModel.class);

        companyBinding = FragmentTravelsCompanyBinding.inflate(inflater,container,false);
        companyAdapter adapter = new companyAdapter(travelList,this);
        companyBinding.companyRecyclerView.setAdapter(adapter);

        companyTravelsViewModel.getAllCompanyTravels(userLocation, max_distance).observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                travelList.clear();
                travelList.addAll(travels);
                adapter.notifyDataSetChanged();
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
                companyBinding.progressbar.setVisibility(View.GONE);
                max_distance = MAX_DIST;
                // TODO: check if last location different enough from the current (example: 100 meters)
                // Called when a new location is found by the network location provider.
                userLocation.setLat(location.getLatitude());
                userLocation.setLon(location.getLongitude());
                // TODO:loader.visibility.GONE or something like that...
                //textView.setText(getPlace(location));

                if (companyTravelsViewModel != null)
                    companyTravelsViewModel.getAllCompanyTravels(userLocation, MAX_DIST);
                adapter.notifyDataSetChanged();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        getLocation();

        return companyBinding.getRoot();
    }

    private void getLocation() {
        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20*1000, 0, locationListener);
            companyBinding.progressbar.setVisibility(View.VISIBLE);
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


    @Override
    public String getPlace(UserLocation location) {
        Geocoder geocoder = new Geocoder(parentActivity.getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLat(), location.getLon(), 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0);
            }
            return "unknown place: \n ("+location.getLat()+", "+location.getLon()+")";
        }
        catch(
                IOException e)
        {
            e.printStackTrace();
        }
        return "IOException ...";
    }


    @Override
    public void send(Travel travel) {
        if (travel.getCompany() == null)
            travel.setCompany(new HashMap<>());
        travel.getCompany().put(companyName, false);
        companyTravelsViewModel.updateTravel(travel);
    }

    @Override
    public void sendEmail(String emailAddress){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Travel Application Invitation For Your Requested Travel");
        if (intent.resolveActivity(parentActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void phoneCall(String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        startActivity(intent);
    }


    @Override
    public List<String> getPlaces(List<UserLocation> locations) {
        List<String> places = new ArrayList<>();
        for (UserLocation location : locations){
            places.add(getPlace(location));
        }
        return places;
    }

    @Override
    public boolean isSendButtonEnabled(Travel travel){
        if (travel.getCompany() != null){
            return (travel.getCompany().get(companyName) == null);
        }
        return (travel.getRequestType() == Travel.RequestType.sent);
    }

}