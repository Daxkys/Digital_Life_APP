package example.digitallife;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import example.digitallife.DB.Account;
import example.digitallife.DB.DigitalLife_DB;

public class Activity_main extends AppCompatActivity {

    private DigitalLife_DB db;
    private LinearLayout ll_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialized variables
        db = DigitalLife_DB.getInstance(this);
        ll_list = findViewById(R.id.ll_accounts);

        // Ad block code
        MobileAds.initialize(this, "ca-app-pub-9934738138092081~2013437011");
        AdView banner = findViewById(R.id.banner_main);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query_search(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_key:
                Intent upload_login = new Intent(this, Activity_login.class);
                upload_login.putExtra(Activity_login.RESET_KEY, true);
                startActivity(upload_login);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void insert_account(View view) {
        Intent start_account = new Intent(this, Activity_account_form.class);
        startActivity(start_account);
    }

    private void update_account(int id) {
        Intent data = new Intent(this, Activity_account_show.class);
        Account account = db.accountDAO().findById(id);
        data.putExtra(Activity_account_show.EXTRA_ID, account.getId());
        startActivity(data);
    }

    private void query_search(String name) {
        ll_list.removeAllViews();
        List<Account> list_accounts = db.accountDAO().selectLike_byName("%".concat(name).concat("%"));

        if (list_accounts.size() >= 1) {

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
        } else {
            TextView tv_fail_search = new TextView(this);
            tv_fail_search.setGravity(Gravity.CENTER);
            tv_fail_search.setText(R.string.search_fail);
            tv_fail_search.setTextColor(Color.RED);
            ll_list.addView(tv_fail_search);
        }
    }

}
