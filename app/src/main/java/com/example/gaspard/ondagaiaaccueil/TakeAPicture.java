package com.example.gaspard.ondagaiaaccueil;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.File;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import android.os.Environment;
import java.util.Date;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.support.v4.app.ActivityCompat;

public class TakeAPicture extends AppCompatActivity {

    Button b1,b2,Home;
    private Uri file;
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
        setContentView(R.layout.takeapicture);

        final String myID = ((GlobalVar)this.getApplication()).getmyID();


        Home = (Button) findViewById(R.id.home3);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome bw=new BWHome(TakeAPicture.this);
                bw.execute(myID);
            }
        });

        b1 = (Button) findViewById(R.id.Camera);
        b2 = (Button) findViewById(R.id.Gallery);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = Uri.fromFile(getOutputMediaFile(1));
                cameraIntent.putExtra("return-data", true);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                startActivityForResult(cameraIntent, 1);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(TakeAPicture.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(TakeAPicture.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                else {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,2);
                }
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String picturepath=file.getPath();;
                Intent i = new Intent(this, PictureChoosen.class);
                i.putExtra("ChoosenPicture",picturepath);
                startActivity(i);
            } else {
                Intent i = new Intent(this, TakeAPicture.class);
                startActivity(i);
            }
        }
        else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Intent i = new Intent(this, PictureChoosen.class);
            i.putExtra("ChoosenPicture",picturePath);
            startActivity(i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

//CHECK