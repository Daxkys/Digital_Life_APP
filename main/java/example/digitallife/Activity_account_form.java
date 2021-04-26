package example.digitallife;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Activity_account_form extends AppCompatActivity {

    private static final String NAME = "NAME";
    private static final String USER = "USER";
    private static final String PASS = "PASS";
    private static final String LINK = "LINK";

    private EditText et_name;
    private EditText et_link;
    private EditText et_user;
    private EditText et_pass;
    private TextView tv_percent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        AdView banner = findViewById(R.id.banner_account_form);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        et_name = findViewById(R.id.et_name);
        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        et_link = findViewById(R.id.et_link);
        tv_percent = findViewById(R.id.tv_percent);
        progressBar = findViewById(R.id.progressBar);

        Intent i = getIntent();
        et_name.setText(i.getStringExtra(NAME));
        et_user.setText(i.getStringExtra(USER));
        et_pass.setText(i.getStringExtra(PASS));
        et_link.setText(i.getStringExtra(LINK));

        setUI_progressBar(calculatePasswordStrongPercent(et_pass.getText().toString()));

        et_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setUI_progressBar(calculatePasswordStrongPercent(s.toString()));
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(NAME, et_name.getText().toString());
        state.putString(USER, et_user.getText().toString());
        state.putString(PASS, et_pass.getText().toString());
        state.putString(LINK, et_link.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle state) {
        super.onRestoreInstanceState(state);
        et_name.setText(state.getString(NAME));
        et_user.setText(state.getString(USER));
        et_pass.setText(state.getString(PASS));
        et_link.setText(state.getString(LINK));
    }

    private void setUI_progressBar(int percent) {
        progressBar.setProgress(percent);
        tv_percent.setText(getResources().getString(R.string.password_strong).concat(" ").concat(String.valueOf(percent)).concat("%"));
    }

    public int calculatePasswordStrongPercent(String password) {

        // variables
        int percent = 0;
        int length = password.length();

        // Checks | Percent (total = 100?)
        if (password.matches("^.*[a-z].*$")) percent += 20;
        if (password.matches("^.*[A-Z].*$")) percent += 20;
        if (password.matches("^.*\\d.*$")) percent += 20;
        if (password.matches("^.*\\W.*$")) percent += 20;
        if (length > 10) length = 10;
        percent += 2 * length;

        return percent;
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public void buttonOK(View view) {
        String name = et_name.getText().toString();
        String user = et_user.getText().toString();
        String pass = et_pass.getText().toString();
        String link = et_link.getText().toString();

        if (name.isEmpty()) {
            et_name.setError(getResources().getString(R.string.fail_input_account));
        } else {
            Intent intent = getIntent();
            intent.putExtra(NAME, name);
            intent.putExtra(USER, user);
            intent.putExtra(PASS, pass);
            intent.putExtra(LINK, link);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
