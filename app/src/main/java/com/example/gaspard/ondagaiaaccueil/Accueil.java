package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.view.View;
import android.widget.Button;

import com.cloudinary.Cloudinary;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.cloudinary.utils.ObjectUtils;

import android.util.Log;

import java.lang.Math;

import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.content.Context;
import android.location.Location;
import android.location.Criteria;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.location.LocationListener;

public class Accueil extends FragmentActivity {


    Button buttonPicture;
    Button buttonContacts;
    GoogleMap map;
    GPSTracker gps;
    //ImageView img;

    double distance(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371; // Radius of the earth in km
        double dLat = lat2 - lat1;  // deg2rad below
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();


        Bundle extras = getIntent().getExtras();
        final String idme = extras.getString("myID");
        final int number = extras.getInt("number");
        final String[] url = extras.getStringArray("url");
        final String[] status = extras.getStringArray("status");
        final String[] name = extras.getStringArray("name");
        final double[] lat = extras.getDoubleArray("lat");
        final double[] lon = extras.getDoubleArray("lon");

        gps = new GPSTracker(Accueil.this);
        double mylat=gps.getLatitude();
        double mylon=gps.getLongitude();
        System.out.println(mylat+" "+mylon);
        //Marker[]m=new Marker[number];

        for (int i = 0; i < number; i++) {
            if (distance(mylat, mylon, lat[i], lon[i]) < 2.0) {
                map.addMarker(new MarkerOptions().position(new LatLng(lat[i], lon[i])).title(name[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            } else {
                map.addMarker(new MarkerOptions().position(new LatLng(lat[i], lon[i])).title(name[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }

        buttonPicture = (Button) findViewById(R.id.take);
        buttonPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Accueil.this, AccueilPicture.class);
                String name = "myID";
                intent.putExtra(name, idme);
                startActivity(intent);
            }
        });

        buttonContacts = (Button) findViewById(R.id.contacts);
        buttonContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWContact backgroundWorker = new BWContact(Accueil.this);
                backgroundWorker.execute(idme);
            }
        });

    }

}

