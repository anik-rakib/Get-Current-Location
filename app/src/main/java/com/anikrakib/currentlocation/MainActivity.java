package com.anikrakib.currentlocation;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    TextView address,latitude,longitude,tvHint,localArea,subLocalArea;
    FusedLocationProviderClient fusedLocationProviderClient;
    LinearLayout currentLocationInformation;
    Button pickerLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        address = findViewById(R.id.tvAddress);
        latitude = findViewById(R.id.latitudeTextView);
        longitude = findViewById(R.id.longitudeTextView);
        subLocalArea = findViewById(R.id.subLocalAreaTextView);
        localArea = findViewById(R.id.localAreaTextView);
        tvHint = findViewById(R.id.tvHints);
        pickerLocation = findViewById(R.id.pickerLocation);
        currentLocationInformation = findViewById(R.id.currentLocationInformation);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        pickerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLocationInformation.getVisibility() == View.GONE){
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        statusCheck();
                        getCurrentLocation();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    }
                }else {
                    statusCheck();
                    getCurrentLocation();
                }
            }
        });

    }

    private void getCurrentLocation() {
        tvHint.setVisibility(View.GONE);
        currentLocationInformation.setVisibility(View.VISIBLE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this,
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        address.setText(Html.fromHtml("<font color='#62000EE'><b>Address: </b><br></font>" + addresses.get(0).getAddressLine(0)));
                        latitude.setText(Html.fromHtml("<font color='#62000EE'><b>Latitude: </b><br></font>" + addresses.get(0).getLatitude()));
                        longitude.setText(Html.fromHtml("<font color='#62000EE'><b>Longitude: </b><br></font>" + addresses.get(0).getLongitude()));
                        localArea.setText(Html.fromHtml("<font color='#62000EE'><b>Local Area: </b><br></font>" + addresses.get(0).getLocality()));
                        subLocalArea.setText(Html.fromHtml("<font color='#62000EE'><b>Sub Local Area: </b><br></font>" + addresses.get(0).getSubLocality()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Location seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        currentLocationInformation.setVisibility(View.GONE);
                        tvHint.setVisibility(View.VISIBLE);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
