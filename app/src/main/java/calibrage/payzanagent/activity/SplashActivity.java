package calibrage.payzanagent.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import calibrage.payzanagent.R;
import calibrage.payzanagent.commonUtil.CommonUtil;


public class SplashActivity extends AppCompatActivity {
    public ImageView splashLogo;

    private final int SPLASH_DISPLAY_LENGTH = 5000;

    private ProgressBar progressBar;
    Handler handler = new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashLogo = (ImageView) findViewById(R.id.splash_logo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !CommonUtil.areAllPermissionsAllowed(this, CommonUtil.PERMISSIONS_REQUIRED)) {
            ActivityCompat.requestPermissions(this, CommonUtil.PERMISSIONS_REQUIRED, CommonUtil.PERMISSION_CODE);
        }

     /*  *//* workingfine top_to_middle*//*
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_top_to_middle);
        anim.setInterpolator((new AccelerateDecelerateInterpolator()));
        anim.setFillAfter(true);
        splashLogo.setAnimation(anim);*/


        /*workingfine left_to_middle*/
         Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_left_to_middle);
        anim.setInterpolator((new AccelerateDecelerateInterpolator()));
        anim.setFillAfter(true);
        splashLogo.setAnimation(anim);


        Animation animation1= AnimationUtils.loadAnimation(SplashActivity.this, R.anim.pop_up);
        progressBar.startAnimation(animation1);




        runnable = new Runnable() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(mainIntent);
                finish();

                /*  Intent mainIntent = new Intent(SplashActivity.this,
                    LoginActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
            handler.removeCallbacks(this);*/

            }
        };
        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CommonUtil.PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Log.v(LOG_TAG, "permission granted");

                }
                break;
        }
    }
}

