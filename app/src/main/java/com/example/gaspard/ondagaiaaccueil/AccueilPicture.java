package com.example.gaspard.ondagaiaaccueil;

import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import com.google.android.gms.ads.formats.NativeAd.Image;
import java.io.File;
import android.content.ContentResolver;
import android.provider.MediaStore.Images.Media;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import android.content.Context;
import android.provider.MediaStore.Images;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import android.os.Environment;
import java.util.Date;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract.Constants;
import java.lang.Object;
import android.graphics.Path;


public class AccueilPicture extends AppCompatActivity {
    int MEDIA_TYPE_IMAGE = 1;
    Button b1,b2;
    //private ImageView iv;
    private Uri file;
    private static int RESULT_LOAD_IMAGE = 1;
    private GoogleApiClient client;

    private File getOutputMediaFile(int type) {
        File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File picsDir = new File(dcimDir, "MyPics");

        if (!picsDir.exists()) {
            picsDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(picsDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueilpicture);
        Bundle extras1 = getIntent().getExtras();
        final String idme = extras1.getString("myID");

        b1 = (Button) findViewById(R.id.Camera);
        b2 = (Button) findViewById(R.id.Gallery);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = Uri.fromFile(getOutputMediaFile(1));
                cameraIntent.putExtra("myID", idme);
                cameraIntent.putExtra("return-data", true);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                startActivityForResult(cameraIntent, 100);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(AccueilPicture.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(AccueilPicture.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                else {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.putExtra("myID", idme);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras2 = getIntent().getExtras();
                final String idme = extras2.getString("myID");
                String s=file.getPath();;
                //Bitmap bp = BitmapFactory.decodeFile(s);
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //byte[] byteArray = stream.toByteArray();
                //ImageView imgView = (ImageView) findViewById(R.id.imgView);
                //imgView.setImageBitmap(bp);
                Intent i = new Intent(this, PictureChoosen.class);
                i.putExtra("ChoosenPicture",s);
                System.out.println(idme);
                i.putExtra("myID", idme);
                startActivity(i);
                //BWChooseAContact backgroundWorker = new BWChooseAContact(this);
                //System.out.println(idme);
                //backgroundWorker.execute(idme);
            } else {
                Bundle extras3 = getIntent().getExtras();
                final String idme = extras3.getString("myID");
                Intent i = new Intent(this, AccueilPicture.class);
                i.putExtra("myID", idme);
                startActivity(i);
            }
        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            final String idme = getIntent().getExtras().getString("myID");
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //ImageView imgView = (ImageView) findViewById(R.id.imgView);
            //imgView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Intent i = new Intent(this, PictureChoosen.class);
            i.putExtra("ChoosenPicture",picturePath);
            i.putExtra("myID", idme);
            startActivity(i);
            //BWChooseAContact backgroundWorker = new BWChooseAContact(this);
            //System.out.println(idme);
            //backgroundWorker.execute(idme);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}