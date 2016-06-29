package com.example.gaspard.ondagaiaaccueil;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import android.widget.Spinner;

import java.util.ArrayList;

public class ChooseAPlace extends FragmentActivity {
    private GoogleMap map;
    Button send, Home;
    private double longitude;
    private double latitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseaplace);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        final String myID=getIntent().getExtras().getString("myID");
        final String IDfriend=getIntent().getExtras().getString("IDfriend");
        final String picturepath=getIntent().getExtras().getString("picture");
        final String orientation=getIntent().getExtras().getString("orientation");

        final Spinner spinner = (Spinner) findViewById(R.id.RADIUS);
        int I=100;
        ArrayList<Integer>data=new ArrayList<Integer>();
        for (int i=0;i<100;i++){
            data.add(I);
            I+=100;
        }
        ArrayAdapter<Integer> dataAdapter=new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,data);
        spinner.setAdapter(dataAdapter);


        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnCameraChangeListener(new OnCameraChangeListener() {
            public void onCameraChange(CameraPosition arg0) {
                map.clear();
                Marker m=map.addMarker(new MarkerOptions().position(arg0.target));
                latitude = m.getPosition().latitude;
                longitude = m.getPosition().longitude;
            }
        });

        send= (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWSend backgroundWorker = new BWSend(ChooseAPlace.this);
                System.out.println(spinner.getSelectedItem().toString());
                backgroundWorker.execute(myID,IDfriend,picturepath,orientation,spinner.getSelectedItem().toString(),Float.toString((float)latitude),Float.toString((float)longitude));
            }
        });

        Home = (Button) findViewById(R.id.home4);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome backgroundWorker = new BWHome(ChooseAPlace.this);
                backgroundWorker.execute(myID);
            }
        });


    }
}

//CHECK