package example.digitallife;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

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
    private SharedPreferences preferences;
    private TextView tv_sumAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Initialized variables
        db = DigitalLife_DB.getInstance(this);
        accounts = db.accountDAO().getAllAccounts();

        // load preferences
        preferences = getPreferences(Context.MODE_PRIVATE);
        int n_columns = Integer.parseInt(preferences.getString("N_COLUMNS", "1"));


        // UI and Ad block code
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, n_columns));
        dl_adapter = new DL_Adapter(accounts);
        dl_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity_Show(recyclerView.getChildAdapterPosition(view));
            }
        });
        recyclerView.setAdapter(dl_adapter);

        // BottomAppBar config
        BottomAppBar bottomAppBar = findViewById(R.id.bar);
        setSupportActionBar(bottomAppBar);
        tv_sumAccounts = findViewById(R.id.tv_sumAccounts);

        // Ad
        AdView banner = findViewById(R.id.banner_main);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);


        DisplayTotal();

        /* FOR A FUTURE VERSION
        TODO: implement ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
         */

    }

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

                    if (action.equals("UPDATE")) {

                        startActivity_Edit(position);

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
            upload_login.putExtra(Activity_login.RESET_KEY, true);
            startActivity(upload_login);
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_columns) {

            final String[] columns = {"1", "2"};
            final int selected = Integer.parseInt(preferences.getString("N_COLUMNS", "1")) - 1;

            DialogInterface.OnClickListener cancel_dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };

            DialogInterface.OnClickListener save_nColumns = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("N_COLUMNS", columns[position]);
                    editor.apply();

                    int n_columns = Integer.parseInt(columns[position]);

                    recyclerView.setLayoutManager(new GridLayoutManager(Activity_main.this, n_columns));
                }
            };

            new MaterialAlertDialogBuilder(Activity_main.this)
                    .setTitle(R.string.action_n_columns)
                    .setNeutralButton(R.string.gen_cancel, cancel_dialog)
                    .setPositiveButton(R.string.save, save_nColumns)
                    .setSingleChoiceItems(columns, selected, null)
                    .create()
                    .show();

        } else if (item.getItemId() == R.id.rate_app) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "example.digitallife")));
        }
        return super.onOptionsItemSelected(item);
    }

    public void startActivity_Form(View view) {

        @SuppressWarnings({"unused", "RedundantSuppression"}) FloatingActionButton view_fab = (FloatingActionButton) view;

        Intent intent = new Intent(view.getContext(), Activity_account_form.class);
        startActivityForResult(intent, CODE_FORM);

/*        Fragment fragment_form = new Fragment_form();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment_form);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();

        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        bottomAppBar.replaceMenu(R.menu.menu_empty);

        view_fab.setImageResource(R.drawable.ic_save);*/
    }

    public void startActivity_Show(int position) {
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

    public void startActivity_Edit(int position) {
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

    private void query_search(String name) {

        accounts = db.accountDAO().selectLike_byName("%".concat(name).concat("%"));
        dl_adapter.filter(accounts);
    }

    private void DisplayTotal() {
//        Fragment_main fragment_main = (Fragment_main) getSupportFragmentManager().findFragmentById(R.id.fragment);
//        fragment_main.displayTotal(accounts.size());

        tv_sumAccounts.setText(getResources().getString(R.string.total_accounts).concat(": ").concat(String.valueOf(accounts.size())));
    }

    // TODO: ItemTouchHelper - swipe
    /*
    private ItemTouchHelper.Callback createHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    update_item(viewHolder.getAdapterPosition());
                } else {
                    delete_item(viewHolder.getAdapterPosition());
                }
            }
        };
    }

     */
}
