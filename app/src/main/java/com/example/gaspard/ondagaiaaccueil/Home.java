package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.view.View;
import android.widget.Button;
import java.lang.Math;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

public class Home extends FragmentActivity {

    Button TakeAPicture;
    Button Contacts;
    Button Disconnect;
    Button Home;
    GoogleMap map;
    GPSTracker gps;

    double distance(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371;
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();


        Bundle extras = getIntent().getExtras();
        final String myID = extras.getString("myID");
        final int number = extras.getInt("number");
        final String[] url = extras.getStringArray("url");
        final String[] orientation = extras.getStringArray("orientation");
        final String[] status = extras.getStringArray("status");
        final String[] name = extras.getStringArray("name");
        final String[] num = extras.getStringArray("num");
        final double[] lat = extras.getDoubleArray("lat");
        final double[] lon = extras.getDoubleArray("lon");

        gps = new GPSTracker(Home.this);
        final double mylat=gps.getLatitude();
        final double mylon=gps.getLongitude();
        final Marker[]m=new Marker[number];

        for (int i = 0; i < number; i++) {
            if (distance(mylat, mylon, lat[i], lon[i]) < 10.0 && status[i].equals("waiting")) {
                m[i]=map.addMarker(new MarkerOptions().position(new LatLng(lat[i], lon[i])).title(name[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                BWPutAccepted bw= new BWPutAccepted(this);
                bw.execute(num[i]);
                status[i]="accepted";

            } else if (status[i].equals("accepted")){
                m[i]=map.addMarker(new MarkerOptions().position(new LatLng(lat[i], lon[i])).title(name[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
            else if (status[i].equals("waiting") && distance(mylat, mylon, lat[i], lon[i]) >= 10.0){
                m[i]=map.addMarker(new MarkerOptions().position(new LatLng(lat[i], lon[i])).title(name[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker arg0) {
                    int I = 0;
                    while (!arg0.equals(m[I]) && I < number) {
                        I++;
                    }
                    if (status[I]=="accepted") {
                        Intent i = new Intent(Home.this, ShowPicture.class);
                        i.putExtra("url", url[I]);
                        i.putExtra("myID",myID);
                        i.putExtra("orientation",orientation[I]);
                        startActivity(i);
                    }
                    return true;
                }
            });

        TakeAPicture = (Button) findViewById(R.id.takeapicture);
        TakeAPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, TakeAPicture.class);
                intent.putExtra("myID", myID);
                startActivity(intent);
            }
        });

        Contacts = (Button) findViewById(R.id.contacts);
        Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWContact backgroundWorker = new BWContact(Home.this);
                backgroundWorker.execute(myID);
            }
        });

        Disconnect = (Button) findViewById(R.id.disconnect);
        Disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Home.this,LoginSignin.class);
                startActivity(i);
            }
        });

        Home = (Button) findViewById(R.id.home);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome backgroundWorker = new BWHome(Home.this);
                backgroundWorker.execute(myID);
            }
        });
    }

}

//CHECK