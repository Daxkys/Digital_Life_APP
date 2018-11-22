package example.digitallife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    public void start_Add_account (View view) {
        Intent openActivity = new Intent(this, Add_account.class);
        startActivity(openActivity);
    }

}
