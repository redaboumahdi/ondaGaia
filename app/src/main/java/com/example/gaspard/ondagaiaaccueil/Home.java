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
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.os.AsyncTask;

public class Home extends FragmentActivity {

    Button TakeAPicture;
    Button Contacts;
    Button Disconnect;
    Button Gallery;
    Button Home;
    Button Chat;
    GoogleMap map;
    GPSTracker gps;

    double distance(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371008;
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
        final String[] url = extras.getStringArray("url");
        final String[] orientation = extras.getStringArray("orientation");
        final String[] radius = extras.getStringArray("radius");
        final String[] status = extras.getStringArray("status");
        final String[] name = extras.getStringArray("name");
        final String[] num = extras.getStringArray("num");
        final double[] lat = extras.getDoubleArray("lat");
        final double[] lon = extras.getDoubleArray("lon");

        final String myID=((GlobalVar)this.getApplication()).getmyID();

        gps = new GPSTracker(Home.this);
        final double mylat=gps.getLatitude();
        final double mylon=gps.getLongitude();

        //map.addMarker(new MarkerOptions().position(new LatLng(mylat,mylon)).title("my position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


        final Marker[] m = new Marker[url.length];
        for (int i = 0; i < url.length; i++) {
            if (distance(mylat, mylon, lat[i], lon[i]) < Integer.parseInt(radius[i]) && status[i].equals("waiting")) {
                m[i] = map.addMarker(new MarkerOptions().position(new LatLng(lat[i], lon[i])).title(name[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                status[i] = "accepted";
                Calendar c = new GregorianCalendar();
                final String date = Long.toString(c.getTimeInMillis());
                final String nn = num[i];
                status[i] = "accepted";
                BWPutAccepted bw = new BWPutAccepted(this);
                bw.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, nn, date);
            } else if (status[i].equals("accepted")) {
                m[i] = map.addMarker(new MarkerOptions().position(new LatLng(lat[i], lon[i])).title(name[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            } else if (status[i].equals("waiting") && distance(mylat, mylon, lat[i], lon[i]) >= Integer.parseInt(radius[i])) {
                m[i] = map.addMarker(new MarkerOptions().position(new LatLng(lat[i], lon[i])).title(name[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                int I = 0;
                while (!arg0.equals(m[I]) && I < url.length) {
                    I++;
                }
                if (status[I].equals("accepted")) {
                    Intent i = new Intent(Home.this, ShowPicture.class);
                    i.putExtra("url", url[I]);
                    i.putExtra("orientation", orientation[I]);
                    i.putExtra("num",num[I]);
                    startActivity(i);
                }
                else{
                    Intent i= new Intent(Home.this,ShowPictureBlur.class);
                    i.putExtra("url", url[I]);
                    i.putExtra("orientation", orientation[I]);
                    i.putExtra("num",num[I]);
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
                startActivity(intent);
            }
        });

        Contacts = (Button) findViewById(R.id.contacts);
        Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWContact bw =new BWContact(Home.this);
                bw.execute(myID);
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
                BWHome bw =new BWHome(Home.this);
                bw.execute(myID);
            }
        });

        Gallery = (Button) findViewById(R.id.gallery);
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWGallery backgroundWorker = new BWGallery(Home.this);
                backgroundWorker.execute(myID);
            }
        });

        Chat = (Button) findViewById(R.id.chat);
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,Chat.class);
                startActivity(i);
            }
        });
    }

}

//CHECK