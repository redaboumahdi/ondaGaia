package com.example.gaspard.ondagaiaaccueil;


import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

public class ShowPictureGallery extends AppCompatActivity {

    Button Home,Back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpicturegallery);

        Bundle extras = getIntent().getExtras();
        final String myID = extras.getString("myID");
        final String[] url = extras.getStringArray("url");
        final String[] orientation = extras.getStringArray("orientation");
        final String urlaccepted=extras.getString("urlaccepted");
        final String orientationaccepted=extras.getString("orientationaccepted");
        final String[] friends = extras.getStringArray("listofcontact");
        final String[] date = extras.getStringArray("date");
        URL u = null;
        try {
            u = new URL(urlaccepted);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
            if (orientationaccepted.equals("6")) {
                matrix.postRotate(90);
            }
            else if (orientationaccepted.equals("3")) {
                matrix.postRotate(180);
            }
            else if (orientationaccepted.equals("8")) {
                matrix.postRotate(270);
            }
            bp[0] = Bitmap.createBitmap(bp[0], 0, 0, bp[0].getWidth(), bp[0].getHeight(), matrix, true);
        }
        catch (Exception e) {
        }
        ImageView imgView= new ImageView(this);
        imgView.setImageBitmap(bp[0]);
        ImageView img = (ImageView)findViewById(R.id.imgView4);
        img.setImageDrawable(imgView.getDrawable());

        Home = (Button) findViewById(R.id.home8);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome backgroundWorker = new BWHome(ShowPictureGallery.this);
                backgroundWorker.execute(myID);
            }
        });

        Back = (Button) findViewById(R.id.back3);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ShowPictureGallery.this,Gallery.class);
                i.putExtra("myID",myID);
                i.putExtra("url",url);
                i.putExtra("orientation",orientation);
                i.putExtra("date",date);
                i.putExtra("listofcontact",friends);
                startActivity(i);
            }
        });
    }
}