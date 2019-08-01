package example.digitallife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class Activity_login extends AppCompatActivity {

    private SharedPreferences login_preference;
    private static final String FIRST_TIME = "FIRST_TIME";
    private static final String MAIN_KEY = "MAIN_KEY";
    public static final String RESET_KEY = "RESET_KEY";

    InterstitialAd interstitial_login;

    private EditText et_login;
    private ImageButton ib_login;
    private TextView tv_firstLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialized variables
        login_preference = getPreferences(Context.MODE_PRIVATE);

        et_login = findViewById(R.id.et_login);
        ib_login = findViewById(R.id.ib_login);
        tv_firstLogin = findViewById(R.id.tv_firstLogin);
        Button b_setLogin = findViewById(R.id.b_setLogin);

        // Set visibility of text and buttons for the first time or reset key
        if (login_preference.getBoolean(FIRST_TIME, true) || getIntent().getBooleanExtra(RESET_KEY, false)) {
            ib_login.setVisibility(View.GONE);
            tv_firstLogin.setVisibility(View.VISIBLE);
            b_setLogin.setVisibility(View.VISIBLE);
        } else {
            tv_firstLogin.setVisibility(View.INVISIBLE);
            b_setLogin.setVisibility(View.INVISIBLE);
        }

        // Banner and interstitial ads load
        AdView banner = findViewById(R.id.banner_login);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        interstitial_login = new InterstitialAd(this);
        interstitial_login.setAdUnitId("ca-app-pub-9934738138092081/2614553925");
        interstitial_login.loadAd(new AdRequest.Builder().build());
    }

    public void login(View view) {

        String s_login = et_login.getText().toString();

        if (login_preference.getString(MAIN_KEY, "").equals(s_login)) {
            Intent toMain = new Intent(this, Activity_main.class);
            startActivity(toMain);
            finish();

            if (interstitial_login.isLoaded()) {
                interstitial_login.show();
            }
        } else {
            Toast.makeText(this, R.string.main_key_error, Toast.LENGTH_SHORT).show();
            et_login.setText("");
        }
    }

    public void setLogin(View view) {

        String main_password = et_login.getText().toString();

        if (main_password.length() < 4) {
            Toast.makeText(this, R.string.main_key_must_length, Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences.Editor editor = login_preference.edit();
            editor.putBoolean(FIRST_TIME, false);
            editor.putString(MAIN_KEY, main_password);
            editor.apply();

            Toast.makeText(this, R.string.main_key_set, Toast.LENGTH_SHORT).show();

            ib_login.setVisibility(View.VISIBLE);
            tv_firstLogin.setText(R.string.main_key_after_set);
        }
    }

}
