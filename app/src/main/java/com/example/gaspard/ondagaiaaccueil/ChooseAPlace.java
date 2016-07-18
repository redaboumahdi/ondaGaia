package com.example.gaspard.ondagaiaaccueil;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import android.widget.Spinner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseAPlace extends FragmentActivity {
    private GoogleMap map;
    Button send, Home;
    private double longitude;
    private double latitude;
    GPSTracker gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseaplace);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        final String myID=((GlobalVar)this.getApplication()).getmyID();
        final String picturepath=getIntent().getExtras().getString("picture");
        final String orientation=getIntent().getExtras().getString("orientation");
        final String[]selectedID=getIntent().getExtras().getStringArray("IDselected");

        final Spinner spinner = (Spinner) findViewById(R.id.RADIUS);
        int I=100;
        ArrayList<Integer>data=new ArrayList<Integer>();
        for (int i=0;i<100;i++){
            data.add(I);
            I+=100;
        }
        ArrayAdapter<Integer> dataAdapter=new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,data);
        spinner.setAdapter(dataAdapter);

        gps = new GPSTracker(ChooseAPlace.this);
        final double mylat=gps.getLatitude();
        final double mylon=gps.getLongitude();

        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnCameraChangeListener(new OnCameraChangeListener() {
            public void onCameraChange(CameraPosition arg0) {
                map.clear();
                Marker m=map.addMarker(new MarkerOptions().position(arg0.target));
                map.addMarker(new MarkerOptions().position(new LatLng(mylat,mylon)).title("my position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                latitude = m.getPosition().latitude;
                longitude = m.getPosition().longitude;
            }
        });
        send= (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final File file = new File (picturepath);
                Map config = new HashMap();
                config.put("api_secret", "CDUO-h5Y89RWXDHqjnIwYE-j_58");
                config.put("api_key", "212168143373637");
                config.put("cloud_name", "djzvvfdmn");
                final Cloudinary cloudinary = new Cloudinary(config);
                final Map[] uploadResult = {null};
                Thread thread=new Thread(){
                    public void run(){
                        try {
                            uploadResult[0] = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final String urlpicture = (String) uploadResult[0].get("url");
                for (int i=0;i<selectedID.length;i++){
                    final int finalI = i;
                    Thread thread1=new Thread(){
                        public void run(){
                            try {
                                String IDfriend=selectedID[finalI];
                                String radius=spinner.getSelectedItem().toString();
                                String lat=Float.toString((float)latitude);
                                String lon=Float.toString((float)longitude);
                                String send_url = "http://192.168.8.102:8888/send.php";
                                URL url = new URL(send_url);
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setRequestMethod("POST");
                                httpURLConnection.setDoOutput(true);
                                httpURLConnection.setDoInput(true);
                                OutputStream outputStream = httpURLConnection.getOutputStream();
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                                String send_data = URLEncoder.encode("myID","UTF-8")+"="+URLEncoder.encode(myID,"UTF-8") + "&"
                                        +URLEncoder.encode("IDfriend","UTF-8")+"="+URLEncoder.encode(IDfriend,"UTF-8") + "&"
                                        +URLEncoder.encode("urlpicture","UTF-8")+"="+URLEncoder.encode(urlpicture,"UTF-8")+"&"
                                        +URLEncoder.encode("orientation","UTF-8")+"="+URLEncoder.encode(orientation,"UTF-8")+"&"
                                        +URLEncoder.encode("radius","UTF-8")+"="+URLEncoder.encode(radius,"UTF-8")+"&"
                                        +URLEncoder.encode("lat","UTF-8")+"="+URLEncoder.encode(lat,"UTF-8") + "&"
                                        +URLEncoder.encode("lon","UTF-8")+"="+URLEncoder.encode(lon,"UTF-8");
                                bufferedWriter.write(send_data);
                                bufferedWriter.flush();
                                bufferedWriter.close();
                                InputStream inputStream = httpURLConnection.getInputStream();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                                String result="";
                                String line="";
                                while ((line = bufferedReader.readLine())!= null){
                                    result += line;
                                }
                                bufferedReader.close();
                                inputStream.close();
                                httpURLConnection.disconnect();
                            } catch( MalformedURLException e){
                                e.printStackTrace();
                            } catch ( IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    thread1.start();
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                BWHome bw=new BWHome(ChooseAPlace.this);
                bw.execute(myID);
            }
        });

        Home = (Button) findViewById(R.id.home4);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome bw=new BWHome(ChooseAPlace.this);
                bw.execute(myID);
            }
        });


    }
}

//CHECK