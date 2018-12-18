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

public class Activity_login extends AppCompatActivity {

    private SharedPreferences login_preference;
    private final String FIRST_TIME = "FIRST_TIME";
    private final String MAIN_KEY = "MAIN_KEY";

    private EditText et_login;
    private ImageButton ib_login;
    private TextView tv_firstLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_preference = getPreferences(Context.MODE_PRIVATE);

        et_login = findViewById(R.id.et_login);
        ib_login = findViewById(R.id.ib_login);
        tv_firstLogin = findViewById(R.id.tv_firstLogin);
        Button b_setLogin = findViewById(R.id.b_setLogin);

        if (login_preference.getBoolean(FIRST_TIME, true)) {
            ib_login.setVisibility(View.INVISIBLE);
            tv_firstLogin.setVisibility(View.VISIBLE);
            b_setLogin.setVisibility(View.VISIBLE);
        }
    }

    public void login(View view) {

        String s_login = et_login.getText().toString();

        if (login_preference.getString(MAIN_KEY, "").equals(s_login)) {
            Intent toMain = new Intent(this, Activity_main.class);
            startActivity(toMain);
            finish();
        } else {
            Toast.makeText(this, R.string.main_key_error, Toast.LENGTH_SHORT).show();
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
