package example.digitallife;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import example.digitallife.DB.Account;
import example.digitallife.DB.DigitalLife_DB;

public class Activity_account_show extends AppCompatActivity {

    private static final int CODE_EDIT = 1;
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String ID_UPDATE = "ID_UPDATE";

    private DigitalLife_DB db;
    private Account mutant;

    private TextView tv_account;
    private TextView tv_username;
    private TextView tv_password;
    private ImageButton ib_open_link;
    private ImageButton ib_copy_user;
    private ImageButton ib_copy_pass;

    private boolean show_password = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_show);

        AdView banner = findViewById(R.id.banner_account_show);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        db = DigitalLife_DB.getInstance(this);

        tv_account = findViewById(R.id.tv_account);
        tv_username = findViewById(R.id.tv_username);
        tv_password = findViewById(R.id.tv_password);
        ib_open_link = findViewById(R.id.ib_open_link);
        ib_copy_user = findViewById(R.id.ib_copy_user);
        ib_copy_pass = findViewById(R.id.ib_copy_pass);

        loadViews();
        tv_password.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void loadViews() {
        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mutant = db.accountDAO().findById(getIntent().getIntExtra(EXTRA_ID, 0));

        tv_account.setText(mutant.getName());
        tv_username.setText(mutant.getUser());
        tv_password.setText(mutant.getPass());
        ib_open_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(mutant.getLink());
            }
        });
        if (clipboardManager != null) {
            ib_copy_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("user copied", tv_username.getText()));
                    Toast.makeText(Activity_account_show.this, "user copied", Toast.LENGTH_SHORT).show();
                }
            });
            ib_copy_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("password copied", tv_password.getText()));
                    Toast.makeText(Activity_account_show.this, "password copied", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            ((ImageButton) view).setImageResource(R.drawable.ic_hide);
        } else {
            tv_password.setTransformationMethod(new PasswordTransformationMethod());
            ((ImageButton) view).setImageResource(R.drawable.ic_show);

        }
        show_password = !show_password;
    }
}
