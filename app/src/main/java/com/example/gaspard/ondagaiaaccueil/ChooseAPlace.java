package com.example.gaspard.ondagaiaaccueil;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.location.LocationListener;
import android.location.Location;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;

public class ChooseAPlace extends FragmentActivity {
    private GoogleMap map;
    Button send;
    public Marker m;
    private double longitude;
    private double latitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueilchooseaplace);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        final String idme=getIntent().getExtras().getString("myID");
        final String IDfriend=getIntent().getExtras().getString("IDfriend");
        final String picturepath=getIntent().getExtras().getString("picture");
        System.out.println(idme);

        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnMarkerDragListener(new OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
            }
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                latitude = m.getPosition().latitude;
                longitude = m.getPosition().longitude;
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });
        m= map.addMarker(new MarkerOptions().position(new LatLng(48.856614, 2.3522219)).draggable(true));
        send= (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWSend backgroundWorker = new BWSend(ChooseAPlace.this);
                backgroundWorker.execute(idme,IDfriend,picturepath,Float.toString((float)latitude),Float.toString((float)longitude));
            }
        });
    }
}
