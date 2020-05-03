package example.digitallife;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import example.digitallife.DB.Account;
import example.digitallife.DB.DigitalLife_DB;

public class Activity_account_show extends AppCompatActivity {

    private static final int CODE_EDIT = 1;
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String ID_UPDATE = "ID_UPDATE";

    private DigitalLife_DB db;
    private Account mutant;

    private FloatingActionButton fab;
    private TextView tv_account;
    private TextView tv_username;
    private TextView tv_password;
    private Button ib_copy_user;
    private Button ib_copy_pass;
    private Button go_web;

    private boolean show_password = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_show);

        AdView banner = findViewById(R.id.banner_account_show);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        db = DigitalLife_DB.getInstance(this);

        fab = findViewById(R.id.fab_account_show);
        tv_account = findViewById(R.id.tv_account);
        tv_username = findViewById(R.id.tv_username);
        tv_password = findViewById(R.id.tv_password);
        ib_copy_user = findViewById(R.id.copy_user);
        ib_copy_pass = findViewById(R.id.copy_pass);

        loadViews();
        tv_password.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void loadViews() {
        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mutant = db.accountDAO().findById(getIntent().getIntExtra(EXTRA_ID, 0));

        tv_account.setText(mutant.getName());
        tv_username.setText(mutant.getUser());
        tv_password.setText(mutant.getPass());

        if (clipboardManager != null) {
            ib_copy_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("USERNAME", tv_username.getText()));
                    Toast.makeText(Activity_account_show.this, R.string.copy_user, Toast.LENGTH_SHORT).show();
                    //Snackbar.make(fab, R.string.copy_user, Snackbar.LENGTH_SHORT).show();
                }
            });
            ib_copy_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("PASSWORD", tv_password.getText()));
                    Toast.makeText(Activity_account_show.this, R.string.copy_password, Toast.LENGTH_SHORT).show();
                    //Snackbar.make(fab, R.string.copy_password, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                loadViews();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_show, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent edit = new Intent(this, Activity_account_form.class);
                edit.putExtra(ID_UPDATE, mutant.getId());
                startActivityForResult(edit, CODE_EDIT);
                return true;

            case R.id.action_delete:
                db.accountDAO().deleteAccount(mutant);
                DigitalLife_DB.destroyInstance();
                finish();
                return true;

            case R.id.action_go_web:
                openWebPage(mutant.getLink());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openWebPage(String url) {
        if (!(url.startsWith("http://") && url.startsWith("https://"))) {
            url = "http://" + url;
        }
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (browser.resolveActivity(getPackageManager()) != null) {
            startActivity(browser);
        }
    }

    public void showHide_password(View view) {
        if (show_password) {
            tv_password.setTransformationMethod(null);
            fab.setImageResource(R.drawable.ic_hide);
        } else {
            tv_password.setTransformationMethod(new PasswordTransformationMethod());
            fab.setImageResource(R.drawable.ic_show);

        }
        show_password = !show_password;
    }
}
