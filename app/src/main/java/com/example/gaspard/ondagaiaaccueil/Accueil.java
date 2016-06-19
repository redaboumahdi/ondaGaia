package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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


public class Accueil extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button buttonPicture;
    Button buttonContacts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*
        Cloudinary cloudinary = new Cloudinary();
        File file = new File("/Users/Gaspard/Desktop/ImageAppli/paris");
        try {
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            String paris_url = (String) uploadResult.get("url");
            Log.i("didilo",paris_url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        Bundle extras = getIntent().getExtras();
        final String idme=extras.getString("myID");

        buttonPicture=(Button)findViewById(R.id.take);
        buttonPicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(Accueil.this, AccueilPicture.class);
                String name= "myID";
                intent.putExtra(name, idme);
                startActivity(intent);
            }
        });

        buttonContacts=(Button)findViewById(R.id.contacts);
        buttonContacts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                BWContact backgroundWorker = new BWContact(Accueil.this);
                backgroundWorker.execute(idme);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng paris = new LatLng(48.856614, 2.3522219);
        mMap.addMarker(new MarkerOptions().position(paris).title("PARIS").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        LatLng lyon = new LatLng(45.764043, 4.8356589);
        mMap.addMarker(new MarkerOptions().position(lyon).title("LYON").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        LatLng tours = new LatLng(47.394143, 0.6848400);
        mMap.addMarker(new MarkerOptions().position(tours).title("TOURS").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris));
    }

}