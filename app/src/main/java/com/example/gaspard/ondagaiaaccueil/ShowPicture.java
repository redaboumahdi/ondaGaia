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
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

public class ShowPicture extends AppCompatActivity {

    Button Home,Suppress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpicture);
        final String url = getIntent().getExtras().getString("url");
        final String orientation = getIntent().getExtras().getString("orientation");
        final String num = getIntent().getExtras().getString("num");
        final String myID = ((GlobalVar)this.getApplication()).getmyID();

        URL u = null;
        try {
            u = new URL(url);
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
            if (orientation.equals("6")) {
                matrix.postRotate(90);
            }
            else if (orientation.equals("3")) {
                matrix.postRotate(180);
            }
            else if (orientation.equals("8")) {
                matrix.postRotate(270);
            }
            bp[0] = Bitmap.createBitmap(bp[0], 0, 0, bp[0].getWidth(), bp[0].getHeight(), matrix, true);
        }
        catch (Exception e) {
        }
        ImageView imgView= new ImageView(this);
        imgView.setImageBitmap(bp[0]);
        ImageView img = (ImageView)findViewById(R.id.imgView3);
        img.setImageDrawable(imgView.getDrawable());

        Home = (Button) findViewById(R.id.home7);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome bw=new BWHome(ShowPicture.this);
                bw.execute(myID);
            }
        });

        Suppress = (Button) findViewById(R.id.suppress1);
        Suppress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShowPicture.this);
                alertDialogBuilder.setMessage("Are you sure you wanted to suppress the picture?");

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        BWSuppressPicture bw =new BWSuppressPicture(ShowPicture.this);
                        bw.execute(num);
                        BWHome bww= new BWHome(ShowPicture.this);
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
                alertDialog.show();
            }
        });
    }
}

//CHECK