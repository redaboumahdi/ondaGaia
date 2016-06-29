package com.example.gaspard.ondagaiaaccueil;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

public class Gallery extends AppCompatActivity {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    Button Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        Bundle extras = getIntent().getExtras();
        final String myID = extras.getString("myID");
        final String[] url = extras.getStringArray("url");
        final String[] orientation = extras.getStringArray("orientation");
        final String[] status = extras.getStringArray("status");
        final String[] name = extras.getStringArray("name");

        ArrayList<ImageItem>data=new ArrayList<>();
        final ArrayList<String>urlaccepted=new ArrayList<>();
        final ArrayList<String>orientationaccepted=new ArrayList<>();

        for (int i=0;i<url.length;i++){
            if (status[i].equals("accepted")){
                URL u = null;
                try {
                    u = new URL(url[i]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                urlaccepted.add(url[i]);
                orientationaccepted.add(orientation[i]);
                final URL uu=u;
                final Bitmap [] bp = {null};
                Thread thread=new Thread(){
                    public void run() {
                        try {
                            HttpURLConnection connection = (HttpURLConnection) uu.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            bp[0] = BitmapFactory.decodeStream(input);
                        } catch (IOException e) {
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

                try {
                    Matrix matrix = new Matrix();
                    if (orientation[i].equals("6")) {
                        matrix.postRotate(90);
                    }
                    else if (orientation[i].equals("3")) {
                        matrix.postRotate(180);
                    }
                    else if (orientation[i].equals("8")) {
                        matrix.postRotate(270);
                    }
                    bp[0] = Bitmap.createBitmap(bp[0], 0, 0, bp[0].getWidth(), bp[0].getHeight(), matrix, true);
                }
                catch (Exception e) {
                }
                data.add(new ImageItem(bp[0],name[i]));
            }
        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, data);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(Gallery.this, ShowPictureGallery.class);
                i.putExtra("urlaccepted", urlaccepted.get(position));
                i.putExtra("orientationaccepted", orientationaccepted.get(position));
                i.putExtra("myID", myID);
                i.putExtra("url", url);
                i.putExtra("orientation", orientation);
                i.putExtra("status", status);
                i.putExtra("name", name);
                startActivity(i);
            }
        });

        Home = (Button) findViewById(R.id.home9);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome backgroundWorker = new BWHome(Gallery.this);
                backgroundWorker.execute(myID);
            }
        });
    }

}