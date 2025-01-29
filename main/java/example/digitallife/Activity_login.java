package example.digitallife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Activity_login extends AppCompatActivity {

    private SharedPreferences preferences;
    public static final String EXTRA_RESET_KEY = "RESET_KEY";
    public static final String KEY_MAIN_KEY = "MAIN_KEY";
    private String main_key = null;
    private String first_key = null;
    private boolean reset_key = false;

    private TextView tv_firstLogin;
    private EditText et_login;
    private Button b_login;
    private Button b_biometric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Intent extras and preferences
        preferences = getPreferences(Context.MODE_PRIVATE);
        main_key = preferences.getString(KEY_MAIN_KEY, "");
        reset_key = getIntent().getBooleanExtra(EXTRA_RESET_KEY, false);
        Log.d(KEY_MAIN_KEY, main_key);
        Log.d(EXTRA_RESET_KEY, String.valueOf(reset_key));

        // obfuscate the activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Initialized UI
        et_login = findViewById(R.id.et_login);
        tv_firstLogin = findViewById(R.id.tv_firstLogin);
        b_biometric = findViewById(R.id.ib_biometric);
        b_login = findViewById(R.id.b_login);
        b_login.setOnClickListener(v -> login());

        // by default biometric button is disabled
        b_biometric.setVisibility(View.GONE);

        // control if main key is stabilized
        b_login.setOnClickListener(v -> setMainKey());
        is_mainKey_stabilized();
    }

    private void startActivityMain() {
        startActivity(new Intent(this, Activity_main.class));
        finish();
    }

    private void biometric_layout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // checks biometric
            boolean biometric_enabled = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
            if (biometric_enabled)
                b_biometric.setVisibility(View.VISIBLE);

            final Executor executor = Executors.newSingleThreadExecutor();

            final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                    .setTitle(getString(R.string.biometric_verification))
                    .setNegativeButton(getString(R.string.cancel), executor, (dialog, which) -> {
                    }).build();

            b_biometric.setOnClickListener(v -> biometricPrompt.authenticate(
                            new CancellationSignal(), executor,
                            new BiometricPrompt.AuthenticationCallback() {
                                @Override
                                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                                    runOnUiThread(() -> startActivityMain());
                                }
                            }
                    )
            );
        }
    }

    /**
     * Checks if main key is stabilized.
     * If not, change the layout to give the user the power to do it
     */
    private void is_mainKey_stabilized() {
        if (!main_key.isEmpty() && !reset_key) { // Instructions visible and button save main key
            tv_firstLogin.setVisibility(View.INVISIBLE);
            b_login.setText(getString(R.string.login));
            b_login.setOnClickListener(v -> login());
            biometric_layout();
        }
    }

    private void setMainKey() {
        String et_text = et_login.getText().toString();

        if (et_text.length() < 4) {
            et_login.setError(getString(R.string.main_key_error_length));
            return;
        }
        if (first_key == null) {
            first_key = et_text;
            et_login.setText("");
            tv_firstLogin.setText(R.string.main_key_confirmation);
            return;
        }
        if (!first_key.equals(et_text)) {
            et_login.setText("");
            et_login.setError(getString(R.string.main_key_error_confirm));
            return;
        }

        main_key = et_text;
        // SHAVE NEW MAIN KEY
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_MAIN_KEY, main_key);
        editor.apply();

        tv_firstLogin.setText(R.string.main_key_set);
        b_login.setText(R.string.login);
        b_login.setOnClickListener(v -> login());
        biometric_layout();
    }

    private void login() {
        String et_text = et_login.getText().toString();

        if (!et_text.equals(main_key)) {
            et_login.setError(getResources().getString(R.string.main_key_wrong));
            et_login.setText("");
            return;
        }
        startActivityMain();
    }

}