package calibrage.payzanagent.activity;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import calibrage.payzanagent.R;
import calibrage.payzanagent.fragments.AgentRequestsFragment;
import calibrage.payzanagent.fragments.BankDetailFragment;
import calibrage.payzanagent.fragments.BaseFragment;
import calibrage.payzanagent.fragments.LoginFragment;
import calibrage.payzanagent.utils.SharedPrefsData;

import static calibrage.payzanagent.fragments.BaseFragment.MAIN_CONTAINER;

public class HomeActivity extends BaseActivity {
    Fragment fragment;
    FragmentManager fragmentManager;
    private int isLogin = 1;
    public static   Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
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
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
       // toolbar.setTitleTextColor(ContextCompat.getColor(HomeActivity.this,R.color.new_accent));
      //  toolbar.setTitle("f");
     //   toolbar.setNavigationIcon(R.drawable.right_arrow);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        //getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            toolbar.setTitle("Login");
            toolbar.setSubtitle(" ");
         //   Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            SharedPrefsData.getInstance(this).ClearData(this);
            for(int i = 0; i <getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                getSupportFragmentManager().popBackStack();
            }
            getSupportFragmentManager().beginTransaction()
                    .add(BaseFragment.MAIN_CONTAINER, new LoginFragment ()).commit();

        }else if(item.getItemId() == android.R.id.home){
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }



    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();
        //fragmentManager.beginTransaction().replace(R.id.home_container, frag).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
          //  HomeActivity.toolbar.setTitle(getResources().getString(R.string.main_sname));
//            toolbar.setTitle(" ");
//           toolbar.setSubtitle(" ");
          // toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));

           // ReplcaFragment(new BankDetailFragment());
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
