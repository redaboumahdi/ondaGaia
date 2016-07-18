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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class Gallery extends AppCompatActivity {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    Button Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        final String myID= ((GlobalVar)this.getApplication()).getmyID();

        Bundle extras = getIntent().getExtras();
        final String[] friendsgallery = extras.getStringArray("listofcontact");
        final String[] urlgallery = extras.getStringArray("url");
        final String[] orientationgallery = extras.getStringArray("orientation");
        final String[] dategallery = extras.getStringArray("date");
        final String[] numgallery = extras.getStringArray("num");



        ArrayList<ArrayList<String>> friends2=new ArrayList<ArrayList<String>>();
        for (int i=0;i<friendsgallery.length;i++){
            friends2.add(new ArrayList<String>(Arrays.asList(dategallery[i], friendsgallery[i],orientationgallery[i],numgallery[i],urlgallery[i])));
        }

        Collections.sort(friends2, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(0).compareTo(o2.get(0));
            }
        });


        ArrayList<ImageItem>data=new ArrayList<>();
        for (int i=0;i<urlgallery.length;i++){
            URL u = null;
            try {
                u = new URL(urlgallery[i]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            final URL uu=u;
            final Bitmap [] bp = {null};
            Thread thread=new Thread(){
                public void run() {try {
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
                if (orientationgallery[i].equals("6")) {
                    matrix.postRotate(90);
                }
                else if (orientationgallery[i].equals("3")) {
                    matrix.postRotate(180);
                }
                else if (orientationgallery[i].equals("8")) {
                    matrix.postRotate(270);
                }
                bp[0] = Bitmap.createBitmap(bp[0], 0, 0, bp[0].getWidth(), bp[0].getHeight(), matrix, true);
            }
            catch (Exception e) {
            }
            data.add(new ImageItem(bp[0],friendsgallery[i]));
        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, data);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(Gallery.this, ShowPictureGallery.class);
                i.putExtra("stringaccepted", urlgallery[position]);
                i.putExtra("orientationaccepted", orientationgallery[position]);
                i.putExtra("numaccepted",numgallery[position]);
                startActivity(i);
            }
        });

        Home = (Button) findViewById(R.id.home9);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome bw=new BWHome(Gallery.this);
                bw.execute(myID);
            }
        });
    }

}