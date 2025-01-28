package example.digitallife;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import example.digitallife.database.Account;
import example.digitallife.database.DigitalLife_DB;

public class Activity_main extends AppCompatActivity {

    private static final int CODE_FORM = 1;
    private static final int CODE_SHOW = 2;
    private static final int CODE_EDIT = 3;

    private static final String POSITION = "POSITION";
    private static final String ID_ACCOUNT = "ID_ACCOUNT";
    private static final String NAME = "NAME";
    private static final String USER = "USER";
    private static final String PASS = "PASS";
    private static final String LINK = "LINK";

    private DigitalLife_DB db;
    private List<Account> accounts;
    private RecyclerView recyclerView;
    private DL_Adapter dl_adapter;
    private TextView tv_sumAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // DATABASE
        db = DigitalLife_DB.getInstance(this);
        accounts = db.accountDAO().getAllAccounts();

        // UI
        tv_sumAccounts = findViewById(R.id.tv_sumAccounts);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivityForm());
        BottomAppBar bottomAppBar = findViewById(R.id.bar);
        setSupportActionBar(bottomAppBar);

        // RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dl_adapter = new DL_Adapter(accounts);
        dl_adapter.setOnClickListener(view -> startActivityShow(recyclerView.getChildAdapterPosition(view)));
        recyclerView.setAdapter(dl_adapter);

        DisplayTotal();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Account account;

            int position = data.getIntExtra(POSITION, 0);
            int id = data.getIntExtra(ID_ACCOUNT, 0);
            String name = data.getStringExtra(NAME);
            String user = data.getStringExtra(USER);
            String pass = data.getStringExtra(PASS);
            String link = data.getStringExtra(LINK);
            // long last_edit = Calendar.getInstance().getTimeInMillis();

            String action = data.getStringExtra("ACTION");

            switch (requestCode) {
                case CODE_FORM:
                    // insert in DB of account
                    account = new Account(name, link, user, pass);
                    db.accountDAO().insertAccount(account);

                    // cleared the list, getting all accounts again but with last append shorted
                    accounts.clear();
                    accounts.addAll(db.accountDAO().getAllAccounts());
                    dl_adapter.notifyDataSetChanged();
                    DisplayTotal();

                    break;

                case CODE_SHOW:

                    if (Objects.requireNonNull(action).equals("UPDATE")) {

                        startActivityEdit(position);

                    } else if (action.equals("DELETE")) {

                        db.accountDAO().deleteAccount(accounts.get(position));
                        accounts.remove(position);
                        dl_adapter.notifyItemRemoved(position);
                        DisplayTotal();

                    }

                    break;

                case CODE_EDIT:
                    // load the old account and set new values
                    account = db.accountDAO().findById(id);

                    account.setName(name);
                    account.setLink(link);
                    account.setUser(user);
                    account.setPass(pass);

                    // update DB, updated in memory and notify the recyclerView
                    db.accountDAO().updateAccount(account);
                    accounts.set(position, account);
                    dl_adapter.notifyItemChanged(position);

                    break;
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
        if (item.getItemId() == R.id.action_change_key) {
            Intent upload_login = new Intent(this, Activity_login.class);
            upload_login.putExtra(Activity_login.EXTRA_RESET_KEY, true);
            startActivity(upload_login);
            finish();
            return true;
        } else if (item.getItemId() == R.id.rate_app) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "example.digitallife")));
        }
        return super.onOptionsItemSelected(item);
    }

    public void startActivityForm() {
        startActivityForResult(new Intent(this, Activity_account_form.class), CODE_FORM);
    }

    public void startActivityEdit(int position) {
        Account account = accounts.get(position);

        Intent intent = new Intent(this, Activity_account_form.class);
        intent.putExtra(POSITION, position);
        intent.putExtra(ID_ACCOUNT, account.getId());
        intent.putExtra(NAME, account.getName());
        intent.putExtra(USER, account.getUser());
        intent.putExtra(PASS, account.getPass());
        intent.putExtra(LINK, account.getLink());

        startActivityForResult(intent, CODE_EDIT);
    }

    public void startActivityShow(int position) {
        Account account = accounts.get(position);

        Intent intent = new Intent(this, Activity_account_show.class);
        intent.putExtra(POSITION, position);
        intent.putExtra(ID_ACCOUNT, account.getId());
        intent.putExtra(NAME, account.getName());
        intent.putExtra(USER, account.getUser());
        intent.putExtra(PASS, account.getPass());
        intent.putExtra(LINK, account.getLink());

        startActivityForResult(intent, CODE_SHOW);
    }

    private void query_search(String name) {

        accounts = db.accountDAO().selectLike_byName("%".concat(name).concat("%"));
        dl_adapter.filter(accounts);
    }

    private void DisplayTotal() {
        tv_sumAccounts.setText(getResources().getString(R.string.total_accounts).concat(": ").concat(String.valueOf(accounts.size())));
    }
}
