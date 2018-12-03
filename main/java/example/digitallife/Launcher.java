package example.digitallife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class Launcher extends AppCompatActivity {

    static final int INSERT_ACCOUNT = 1;
    static final int UPDATE_ACCOUNT = 2;

    ArrayList<String> hardcode = new ArrayList<>();
    LinearLayout ll_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ll_list = findViewById(R.id.ll_accounts);

        /**
         * CHANGE ALERT OF FIRST ACCOUNT!!!!
         */

        if (hardcode.isEmpty()) {
            Toast.makeText(this, "Añade tu primera cuenta! :)", Toast.LENGTH_LONG).show();

            /*TextView tv = new TextView(this);
            tv.setText("Empieza a añadir tus cuentas! :)");
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            ll_list.addView(tv);*/

        }

       /* //hardcode of examples accounts
        String twitter = "Twitter";
        String instagram = "Instagram";
        String telegram = "Telegram";
        String skype = "Skype";
        String facebook = "Facebook";
        String fotolog = "Fotolog";

        hardcode.add(twitter);
        hardcode.add(instagram);
        hardcode.add(telegram);
        hardcode.add(skype);
        hardcode.add(facebook);
        hardcode.add(fotolog);
        hardcode.add(twitter);
        hardcode.add(instagram);
        hardcode.add(telegram);
        hardcode.add(skype);
        hardcode.add(facebook);
        hardcode.add(fotolog);
        hardcode.add(twitter);
        hardcode.add(instagram);
        hardcode.add(telegram);
        hardcode.add(skype);
        hardcode.add(facebook);
        hardcode.add(fotolog);
        hardcode.add(twitter);
        hardcode.add(instagram);
        hardcode.add(telegram);
        hardcode.add(skype);
        hardcode.add(facebook);
        hardcode.add(fotolog);

        for (String acount : hardcode){
            Button b = new Button(this);
            b.setText(acount);
            ll_list.addView(b);
        }
    */
    }

    // public void addAccountToList(String account, String url, String username, String password) {

    public void addAccountToList(String account) {
        Button b = new Button(this);
        b.setText(account);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_update_account(v);
            }
        });
        ll_list.addView(b);
    }

   public void start_Add_account(View view) {
        Intent start_account = new Intent(this, Account.class);
        startActivityForResult(start_account, INSERT_ACCOUNT);
    }

    public void start_update_account(View view) {
        Button b = (Button) view;
        Intent start_account = new Intent(this, Account.class);
        start_account.putExtra("ACCOUNT", b.getText());
        startActivityForResult(start_account, UPDATE_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == INSERT_ACCOUNT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String account = data.getStringExtra("ACCOUNT");
                String link = data.getStringExtra("LINK");
                String username = data.getStringExtra("USERNAME");
                String password = data.getStringExtra("PASSWORD");
                hardcode.add(account);
                addAccountToList(account + " - " + username);
            }
        }
        if (requestCode == UPDATE_ACCOUNT) {

        }

    }
}
