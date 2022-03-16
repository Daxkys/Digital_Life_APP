package example.digitallife;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;

public class Activity_splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Call the function to initialize AdMob SDK
        MobileAds.initialize(this, initializationStatus -> {
            startActivity(new Intent(Activity_splash.this, Activity_login.class));
            finish();
        });
    }
}
