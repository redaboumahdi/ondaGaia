package com.example.gaspard.ondagaiaaccueil;

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

public class ShowPicture extends AppCompatActivity {

    Button Home;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpicture);
        final String url = getIntent().getExtras().getString("url");
        final String myID = getIntent().getExtras().getString("myID");
        final String orientation = getIntent().getExtras().getString("orientation");
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
                BWHome backgroundWorker = new BWHome(ShowPicture.this);
                backgroundWorker.execute(myID);
            }
        });
    }
}

//CHECK