package calibrage.payzanagent.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import calibrage.payzanagent.R;
import calibrage.payzanagent.fragments.AgentRequestsFragment;
import calibrage.payzanagent.fragments.LoginFragment;

public class HomeActivity extends AppCompatActivity {
    Fragment fragment;
    FragmentManager fragmentManager;
    private int isLogin = 1;
    public  static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar();
        fragmentManager = getSupportFragmentManager();
        if (isLogin == 1) {
            ReplcaFragment(new LoginFragment());
        } else {
            ReplcaFragment(new AgentRequestsFragment());
        }

    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(HomeActivity.this,R.color.new_accent));
        toolbar.setTitle("f");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {


        fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();
        //fragmentManager.beginTransaction().replace(R.id.home_container, frag).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            final Dialog dialog = new Dialog(HomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            dialog.setContentView(R.layout.alert_dialouge_home);

            Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
            Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);


            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    finish();
                }
            });
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.show();

        }
    }
}
