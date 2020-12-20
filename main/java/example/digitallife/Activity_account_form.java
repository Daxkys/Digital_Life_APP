package example.digitallife;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form);

        AdView banner = findViewById(R.id.banner_account_form);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        et_name = findViewById(R.id.et_name);
        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        et_link = findViewById(R.id.et_link);

        Intent i = getIntent();
        et_name.setText(i.getStringExtra(NAME));
        et_user.setText(i.getStringExtra(USER));
        et_pass.setText(i.getStringExtra(PASS));
        et_link.setText(i.getStringExtra(LINK));
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
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        et_name.setText(state.getString(NAME));
        et_user.setText(state.getString(USER));
        et_pass.setText(state.getString(PASS));
        et_link.setText(state.getString(LINK));
    }

    public void buttonOK(View view) {
        String name = et_name.getText().toString();
        String user = et_user.getText().toString();
        String pass = et_pass.getText().toString();
        String link = et_link.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, R.string.fail_input_account, Toast.LENGTH_LONG).show();
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

    public void showHide_password(View view) {
        if (et_pass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ((ImageButton) view).setImageResource(R.drawable.ic_show);
        } else {
            et_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ((ImageButton) view).setImageResource(R.drawable.ic_hide);
        }

        et_pass.setSelection(et_pass.length());
    }
}
