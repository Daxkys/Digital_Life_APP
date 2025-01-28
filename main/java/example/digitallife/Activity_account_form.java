package example.digitallife;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Activity_account_form extends AppCompatActivity {

    private static final String NAME = "NAME";
    private static final String USER = "USER";
    private static final String PASS = "PASS";
    private static final String LINK = "LINK";

    private EditText et_name;
    private EditText et_link;
    private EditText et_user;
    private EditText et_pass;
    private CheckBox cb_lower;
    private CheckBox cb_upper;
    private CheckBox cb_digit;
    private CheckBox cb_symbol;
    private CheckBox cb_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        et_name = findViewById(R.id.et_name);
        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        et_link = findViewById(R.id.et_link);
        cb_lower = findViewById(R.id.cb_lower);
        cb_upper = findViewById(R.id.cb_upper);
        cb_digit = findViewById(R.id.cb_digit);
        cb_symbol = findViewById(R.id.cb_symbol);
        cb_long = findViewById(R.id.cb_long);
        FloatingActionButton fab = findViewById(R.id.fab_account_show);
        fab.setOnClickListener(v -> save());

        Intent i = getIntent();
        et_name.setText(i.getStringExtra(NAME));
        et_user.setText(i.getStringExtra(USER));
        et_pass.setText(i.getStringExtra(PASS));
        et_link.setText(i.getStringExtra(LINK));

        checkPasswordSecurity(et_pass.getText().toString());

        et_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPasswordSecurity(s.toString());
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

    public void checkPasswordSecurity(String password) {
        boolean checkLower = password.matches("^.*[a-z].*$");
        boolean checkUpper = password.matches("^.*[A-Z].*$");
        boolean checkDigit = password.matches("^.*\\d.*$");
        boolean checkSymbol = password.matches("^.*\\W.*$");
        boolean checkLong = password.length() >= 12;

        cb_lower.setChecked(checkLower);
        cb_upper.setChecked(checkUpper);
        cb_digit.setChecked(checkDigit);
        cb_symbol.setChecked(checkSymbol);
        cb_long.setChecked(checkLong);
    }

    public void save() {
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
