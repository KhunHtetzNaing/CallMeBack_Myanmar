package com.htetznaing.callmeback;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MPT extends AppCompatActivity {
    EditText editText;
    Button b1;
    Typeface mm;
    String no;
    TeleInfo telephonyInfo;
    boolean isSIM1Ready;
    boolean isSIM2Ready;
    boolean isDualSIM;

    AdView B;
    AdRequest adRequest;
    InterstitialAd IAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpt);

        adRequest = new AdRequest.Builder().build();
        B = (AdView) findViewById(R.id.adView);
        B.loadAd(adRequest);

        if (ContextCompat.checkSelfPermission(MPT.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MPT.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        mm = Typeface.createFromAsset(getAssets(), "icon.png");
        editText = (EditText) findViewById(R.id.edText);
        b1 = (Button) findViewById(R.id.btn1);

        editText.setTypeface(mm);

        telephonyInfo = TeleInfo.getInstance(this);
        isSIM1Ready = telephonyInfo.isSIM1Ready();
        isSIM2Ready = telephonyInfo.isSIM2Ready();
        isDualSIM = telephonyInfo.isDualSIM();

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

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no = editText.getText().toString();
                if (!no.isEmpty()) {
                    if (no.length() >= 9) {
                        if (isDualSIM == true) {
                            chooseSim();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + "*222*"+no + Uri.encode("#")));
                            if (ContextCompat.checkSelfPermission(MPT.this, Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MPT.this,
                                        new String[]{Manifest.permission.CALL_PHONE}, 1);
                            }
                            startActivity(intent);
                        }
                    }else{
                        if (IAd.isLoaded()){
                            IAd.show();
                        }else{
                            IAd.loadAd(adRequest);
                        }
                        Toast.makeText(MPT.this, "Please fill correct Phone Number :)", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (IAd.isLoaded()){
                        IAd.show();
                    }else{
                        IAd.loadAd(adRequest);
                    }
                    Toast.makeText(MPT.this, "Please fill correct Phone Number :)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void chooseSim(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MPT.this);
        builder.setTitle("Please!");
        builder.setMessage("Choose your SIM 1 or 2 ;)");
        builder.setPositiveButton("SIM 1", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                simOne(no);
            }
        });
        builder.setNegativeButton("SIM 2", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                simTwo(no);
            }
        });

        AlertDialog a = builder.create();
        a.show();
    }

    public void simOne(String Number){
        Context context = getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "*222*"+Number + Uri.encode("#")));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("com.android.phone.force.slot", true);
        intent.putExtra("Cdma_Supp", true);
        for (String s : simSlotName)
            intent.putExtra(s, 0);
        if (ContextCompat.checkSelfPermission(MPT.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MPT.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        context.startActivity(intent);
    }

    public void simTwo(String Number){
        Context context = getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "*222*"+Number + Uri.encode("#")));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("com.android.phone.force.slot", true);
        intent.putExtra("Cdma_Supp", true);
        for (String s : simSlotName)
            intent.putExtra(s, 1);
        if (ContextCompat.checkSelfPermission(MPT.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MPT.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        context.startActivity(intent);
    }

    private final static String simSlotName[] = {
            "extra_asus_dial_use_dualsim",
            "com.android.phone.extra.slot",
            "slot",
            "simslot",
            "sim_slot",
            "subscription",
            "Subscription",
            "phone",
            "com.android.phone.DialingMode",
            "simSlot",
            "slot_id",
            "simId",
            "simnum",
            "phone_type",
            "slotId",
            "slotIdx"
    };

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
}
