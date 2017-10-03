package com.htetznaing.callmeback;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2,b3;
    AdView B;
    AdRequest adRequest;
    InterstitialAd IAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adRequest = new AdRequest.Builder().build();
        B = (AdView) findViewById(R.id.adView);
        B.loadAd(adRequest);

        b1 = (Button) findViewById(R.id.btn1);
        b2 = (Button) findViewById(R.id.btn2);
        b3 = (Button) findViewById(R.id.btn3);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"သင္ဖုန္း Bill မရွိတဲ့အခ်ိန္ ဒါမွမဟုတ္ အေၾကာင္းတခုခုေၾကာင့္\n" +
                        "တစ္ဖက္ကလူကိုဖုန္းျပန္ေခၚခိုင္းဖို႔ ပိုက္ဆံမကုန္ပဲ ဒီ Call Me Back App နဲ႔\n" +
                        "\"Call me back\" SMS ေလးေပးပို႔ႏိုင္ပါတယ္။\n" +
                        "Download Free at Google Play Store : https://play.google.com/store/apps/details?id=com.htetznaing.callmeback");
                startActivity(new Intent(Intent.createChooser(intent,"Call Me Back App")));
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        IAd = new InterstitialAd(this);
        IAd.setAdUnitId("ca-app-pub-4173348573252986/9907889932");
        IAd.loadAd(adRequest);
        IAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (!IAd.isLoaded()){
                    IAd.loadAd(adRequest);
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (!IAd.isLoaded()){
                    IAd.loadAd(adRequest);
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                if (!IAd.isLoaded()){
                    IAd.loadAd(adRequest);
                }
            }
        });
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
            startActivity(new Intent(MainActivity.this,About.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (IAd.isLoaded()){
            IAd.show();
        }else{
            IAd.loadAd(adRequest);
        }
        switch (v.getId()){
            case R.id.btn1:startActivity(new Intent(MainActivity.this,MPT.class));break;
            case R.id.btn2:startActivity(new Intent(MainActivity.this,Telenor.class));break;
            case R.id.btn3:startActivity(new Intent(MainActivity.this,Ooredoo.class));break;
        }
    }

    public void dev(View view){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("fb://profile/100011339710114"));
            startActivity(intent);
        }catch (Exception e){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://m.facebook.com/KHtetzNaing"));
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to Exit ?");
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image,null);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (IAd.isLoaded()){
                    IAd.show();
                }else{
                    IAd.loadAd(adRequest);
                }
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (IAd.isLoaded()){
                    IAd.show();
                }else{
                    IAd.loadAd(adRequest);
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
