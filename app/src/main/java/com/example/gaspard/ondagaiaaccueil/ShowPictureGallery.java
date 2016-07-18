package com.example.gaspard.ondagaiaaccueil;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowPictureGallery extends AppCompatActivity {

    Button Home,Back,Suppress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpicturegallery);

        Bundle extras = getIntent().getExtras();
        final String stringaccepted=extras.getString("stringaccepted");
        final String orientationaccepted=extras.getString("orientationaccepted");
        final String num=extras.getString("numaccepted");
        final String myID = ((GlobalVar)this.getApplication()).getmyID();

        URL u = null;
        try {
            u = new URL(stringaccepted);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final URL uu = u;
        final Bitmap[] bp = {null};
        Thread thread = new Thread() {
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
                BWHome bw=new BWHome(ShowPictureGallery.this);
                bw.execute(myID);
            }
        });

        Back = (Button) findViewById(R.id.back3);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWGallery bw=new BWGallery(ShowPictureGallery.this);
                bw.execute(myID);
            }
        });

        Suppress = (Button) findViewById(R.id.suppress3);
        Suppress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShowPictureGallery.this);
                alertDialogBuilder.setMessage("Are you sure you wanted to suppress the picture?");

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        BWSuppressPicture bw =new BWSuppressPicture(ShowPictureGallery.this);
                        bw.execute(num);
                        BWGallery bww= new BWGallery(ShowPictureGallery.this);
                        bww.execute(myID);
                    }
                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();;
            }
        });
    }
}