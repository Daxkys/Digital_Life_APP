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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        b_login = findViewById(R.id.b_login);
        b_biometric = findViewById(R.id.ib_biometric);

        // by default: first time or reset key action
        b_biometric.setVisibility(View.GONE);
        b_login.setOnClickListener(v -> setMainKey());

        // control if main key is stabilized or not reset key action
        if (!main_key.isEmpty() && !reset_key) {
            tv_firstLogin.setVisibility(View.INVISIBLE);
            b_login.setText(getString(R.string.login));
            b_login.setOnClickListener(v -> login());
            biometric_layout();
        }
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