package example.digitallife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import example.digitallife.DB.Account;
import example.digitallife.DB.DigitalLife_DB;

public class Activity_launcher extends AppCompatActivity {

    private DigitalLife_DB db;
    private LinearLayout ll_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        db = DigitalLife_DB.getInstance(this);

        ll_list = findViewById(R.id.ll_accounts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadScreen();
    }

    public void insert_account(View view) {
        Intent start_account = new Intent(this, Activity_account.class);
        startActivity(start_account);
    }

    private void update_account(int id) {
        Intent start_account = new Intent(this, Activity_account.class);
        start_account.putExtra("ID_UPDATE", id);
        startActivity(start_account);
    }

    private void reloadScreen() {
        List<Account> list_accounts = db.accountDAO().getAllAccounts();

        if (list_accounts.isEmpty()) {
            ll_list.removeAllViews();
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            tv.setText(R.string.start_add);
            ll_list.addView(tv);
        } else {
            ll_list.removeAllViews();
            for (Account a : list_accounts) {

                Button b = new Button(this);
                b.setId(a.getId());
                b.setText(a.getName());
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update_account(v.getId());
                    }
                });

                ll_list.addView(b);
            }
        }
    }
}
