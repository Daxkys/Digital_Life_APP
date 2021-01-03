package example.digitallife;

import android.annotation.SuppressLint;
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
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Activity_account_show extends AppCompatActivity {

    private static final String NAME = "NAME";
    private static final String USER = "USER";
    private static final String PASS = "PASS";
    private static final String LINK = "LINK";

    private FloatingActionButton fab;
    private TextView tv_user;
    private TextView tv_pass;

    private boolean show_password = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_show);

        // UI block code
        AdView banner = findViewById(R.id.banner_account_show);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        // BottomAppBar config
        BottomAppBar bottomAppBar = findViewById(R.id.bar);
        setSupportActionBar(bottomAppBar);

        fab = findViewById(R.id.fab_account_show);
        Button ib_copy_user = findViewById(R.id.copy_user);
        Button ib_copy_pass = findViewById(R.id.copy_pass);
        TextView tv_name = findViewById(R.id.tv_account);
        tv_user = findViewById(R.id.tv_username);
        tv_pass = findViewById(R.id.tv_password);
        tv_pass.setTransformationMethod(new PasswordTransformationMethod());

        Intent i = getIntent();
        tv_name.setText(i.getStringExtra(NAME));
        tv_user.setText(i.getStringExtra(USER));
        tv_pass.setText(i.getStringExtra(PASS));

        // Setting clipboard
        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboardManager != null) {
            ib_copy_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("USERNAME", tv_user.getText()));
                    Toast.makeText(Activity_account_show.this, R.string.copy_user, Toast.LENGTH_SHORT).show();
                    //Snackbar.make(fab, R.string.copy_user, Snackbar.LENGTH_SHORT).show();
                }
            });
            ib_copy_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("PASSWORD", tv_pass.getText()));
                    Toast.makeText(Activity_account_show.this, R.string.copy_password, Toast.LENGTH_SHORT).show();
                    //Snackbar.make(fab, R.string.copy_password, Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_show, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = getIntent();

        switch (item.getItemId()) {
            case R.id.action_edit:
                intent.putExtra("ACTION", "UPDATE");
                setResult(RESULT_OK, intent);
                finish();
                return true;

            case R.id.action_delete:
                intent.putExtra("ACTION", "DELETE");
                setResult(RESULT_OK, intent);
                finish();
                return true;

            case R.id.action_go_web:
                openWebPage(getIntent().getStringExtra(LINK));
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
            tv_pass.setTransformationMethod(null);
            fab.setImageResource(R.drawable.ic_hide);
        } else {
            tv_pass.setTransformationMethod(new PasswordTransformationMethod());
            fab.setImageResource(R.drawable.ic_show);

        }
        show_password = !show_password;
    }
}
