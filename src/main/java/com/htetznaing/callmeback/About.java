package com.htetznaing.callmeback;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class About extends AppCompatActivity {
    AdView B;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        adRequest = new AdRequest.Builder().build();
        B = (AdView) findViewById(R.id.adView);
        B.loadAd(adRequest);
    }

    public void sendEmail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","khunhtetznaing@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Call Me Back[MM]");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Enter your feedback here!.");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
