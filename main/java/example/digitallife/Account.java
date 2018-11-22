package example.digitallife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }

    public void close_activity(View view) {
        EditText et_account = findViewById(R.id.et_account);
        EditText et_link = findViewById(R.id.et_link);
        EditText et_username = findViewById(R.id.et_username);
        EditText et_password = findViewById(R.id.et_password);

        String account = et_account.getText().toString();
        String link = et_link.getText().toString();
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (account.length() != 0 && username.length() != 0) {
            Intent back_launcher = getIntent();
            back_launcher.putExtra("ACCOUNT", account);
            back_launcher.putExtra("LINK", link);
            back_launcher.putExtra("USERNAME", username);
            back_launcher.putExtra("PASSWORD", password);
            setResult(RESULT_OK, back_launcher);
            finish();
        } else {
            Toast.makeText(this, R.string.fail_input_account, Toast.LENGTH_SHORT).show();
        }
    }
}
