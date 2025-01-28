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
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // BottomAppBar config
        BottomAppBar bottomAppBar = findViewById(R.id.bar);
        setSupportActionBar(bottomAppBar);

        fab = findViewById(R.id.fab_account_show);
        fab.setOnClickListener(v -> passwordShowHide());
        ImageButton ib_copy_user = findViewById(R.id.ib_copy_username);
        ImageButton ib_copy_pass = findViewById(R.id.ib_copy_password);
        TextView tv_name = findViewById(R.id.tv_account);
        TextView tv_link = findViewById(R.id.tv_link);
        tv_user = findViewById(R.id.tv_username);
        tv_pass = findViewById(R.id.tv_password);
        tv_pass.setTransformationMethod(new PasswordTransformationMethod());

        Intent i = getIntent();
        tv_name.setText(i.getStringExtra(NAME));
        tv_link.setText(i.getStringExtra(LINK));
        tv_user.setText(i.getStringExtra(USER));
        tv_pass.setText(i.getStringExtra(PASS));

        // Setting clipboard
        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboardManager != null) {
            ib_copy_user.setOnClickListener(view -> {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("USERNAME", tv_user.getText()));
                Snackbar.make(fab, R.string.copy_user, Snackbar.LENGTH_SHORT)
                        .setAnchorView(fab)
                        .show();
            });
            ib_copy_pass.setOnClickListener(view -> {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("PASSWORD", tv_pass.getText()));
                Snackbar.make(fab, R.string.copy_password, Snackbar.LENGTH_SHORT)
                        .setAnchorView(fab)
                        .show();
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

        final Intent intent = getIntent();

        switch (item.getItemId()) {
            case R.id.action_edit:
                intent.putExtra("ACTION", "UPDATE");
                setResult(RESULT_OK, intent);
                finish();
                return true;

            case R.id.action_delete:

                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.ask_del_account)
                        .setMessage(R.string.permanent_action)
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {

                        })
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            intent.putExtra("ACTION", "DELETE");
                            setResult(RESULT_OK, intent);
                            finish();
                        })
                        .show();


                return true;

            case R.id.action_go_web:
                openWebPage(Objects.requireNonNull(getIntent().getStringExtra(LINK)));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openWebPage(String url) {
        if (!(url.startsWith("http://") && url.startsWith("https://"))) {
            url = "http://" + url;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void passwordShowHide() {
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
