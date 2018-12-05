package example.digitallife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import example.digitallife.DB.Account;
import example.digitallife.DB.DIgitalLife_DB;

public class Activity_account extends AppCompatActivity {

    DIgitalLife_DB db;

    EditText et_name;
    EditText et_link;
    EditText et_user;
    EditText et_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        db = DIgitalLife_DB.getInstance(this);

        et_name = findViewById(R.id.et_name);
        et_link = findViewById(R.id.et_link);
        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);

        Intent i = getIntent();
        String query = i.getStringExtra("ACCOUNT");

        et_name.setText(query);
    }

    @Override
    public void onSaveInstanceState (Bundle state) {
        state.putString("NAME",et_name.getText().toString());
        state.putString("LINK",et_link.getText().toString());
        state.putString("USER",et_user.getText().toString());
        state.putString("PASS",et_pass.getText().toString());

        super.onSaveInstanceState(state);
    }

    @Override
    public void onRestoreInstanceState (Bundle state) {
        super.onRestoreInstanceState(state);

        et_name.setText(state.getString("NAME"));
        et_link.setText(state.getString("LINK"));
        et_user.setText(state.getString("USER"));
        et_pass.setText(state.getString("PASS"));
    }

    public void result_launcher(View view) {
        String name = et_name.getText().toString();
        String link = et_link.getText().toString();
        String user = et_user.getText().toString();
        String pass = et_pass.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, R.string.fail_input_account, Toast.LENGTH_LONG).show();
        } else {
            Account account = new Account(name,link,user,pass);

            db.accountDAO().insertAccount(account);

            finish();
        }
    }
}
