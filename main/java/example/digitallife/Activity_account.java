package example.digitallife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import example.digitallife.DB.Account;
import example.digitallife.DB.DIgitalLife_DB;

public class Activity_account extends AppCompatActivity {

    DIgitalLife_DB db;
    int id_update;
    Account mutant;

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
        id_update = i.getIntExtra("ID_UPDATE", 0);

        if (id_update == 0) {
            ImageButton b_delete = findViewById(R.id.b_delete);
            b_delete.setVisibility(View.GONE);
        } else {
            mutant = db.accountDAO().findById(id_update);
            et_name.setText(mutant.getName());
            et_link.setText(mutant.getLink());
            et_user.setText(mutant.getUser());
            et_pass.setText(mutant.getPass());
        }
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

    public void buttonOK(View view) {
        String name = et_name.getText().toString();
        String link = et_link.getText().toString();
        String user = et_user.getText().toString();
        String pass = et_pass.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, R.string.fail_input_account, Toast.LENGTH_LONG).show();
        } else {
            if (id_update == 0) {
                mutant = new Account(name, link, user, pass);
                db.accountDAO().insertAccount(mutant);
            } else {
                mutant.setName(name);
                mutant.setLink(link);
                mutant.setUser(user);
                mutant.setPass(pass);
                db.accountDAO().updateAccount(mutant);
            }
            finish();
        }
    }

    public void buttonDelete(View view) {
        db.accountDAO().deleteAccount(mutant);
        finish();
    }

    public void showHide_password(View view) {
        if (et_pass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ((ImageButton) view).setImageResource(R.drawable.ic_eye_open);
        } else {
            et_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ((ImageButton) view).setImageResource(R.drawable.ic_eye_closed);
        }

        et_pass.setSelection(et_pass.length());
    }
}
