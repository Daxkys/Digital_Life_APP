package example.digitallife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_launcher extends AppCompatActivity {

    static final int INSERT_ACCOUNT = 1;
    static final int UPDATE_ACCOUNT = 2;

    ArrayList<Account> hardcode = new ArrayList<>();
    LinearLayout ll_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ll_list = findViewById(R.id.ll_accounts);

        if (hardcode.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            tv.setText(R.string.start_add);
            ll_list.addView(tv);

        } else {
            reloadLauncher();
        }
    }

    public void insert_account(View view) {
        Intent start_account = new Intent(this, Activity_account.class);
        startActivityForResult(start_account, INSERT_ACCOUNT);
    }

    public void update_account(View view) {
        Button b = (Button) view;
        Intent start_account = new Intent(this, Activity_account.class);
        start_account.putExtra("ACCOUNT", b.getText());
        startActivityForResult(start_account, UPDATE_ACCOUNT);
    }


    public void addButtonToList(String account) {
        Button b = new Button(this);
        b.setText(account);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_account(v);
            }
        });
        ll_list.addView(b);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra("NAME");
            String link = data.getStringExtra("LINK");
            String user = data.getStringExtra("USER");
            String pass = data.getStringExtra("PASS");

            Account account = new Account(name, link, user, pass);

            if (requestCode == INSERT_ACCOUNT) {
                if (hardcode.isEmpty()) {
                    reloadLauncher();
                }
                addButtonToList(name);

                hardcode.add(account); // delete when DB
            }
            if (requestCode == UPDATE_ACCOUNT) {
                for (Account a : hardcode) {
                    if (a.getName().equalsIgnoreCase(data.getStringExtra("ACCOUNT"))) {
                        hardcode.set(hardcode.indexOf(a), account);
                        break;
                    }
                }
                reloadLauncher();
            }
        }
    }

    /**
     * // UPDATE ONLY THE CHANGED BUTTON AND NO ALL THE LINEAR LAYOUT (?)
     */
    private void reloadLauncher() {
        ll_list.removeAllViews();
        for (Account a : hardcode) {
            addButtonToList(a.getName());
        }
    }
}
