/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package calibrage.payzanagent.Calib.ui.activity;

import android.app.Application;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.compat.BuildConfig;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import calibrage.payzanagent.Calib.ui.IScreen;


/**
 * This class is used as base-class for application-base-activity.
 */
public abstract class BaseActivity extends AppCompatActivity implements IScreen {

//    private String LOG_TAG = getClass().getSimpleName();
//    public ProgressHUD mProgressDialog;
//    public String ACTION_DEATH_REGISTER_COMPLETE = "action_death_register_complete";
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                mMessageReceiver, new IntentFilter(ACTION_DEATH_REGISTER_COMPLETE));
//    }
//    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//                finish();
//
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (BuildConfig.DEBUG) {
//            Log.i(LOG_TAG, "onResume()");
//        }
//
//        Application application = this.getApplication();
//        if (application instanceof BaseApplication) {
//            BaseApplication baseApplication = (BaseApplication) application;
//            if (baseApplication.isAppInBackground()) {
//                onAppResumeFromBackground();
//
//            }
//            baseApplication.onActivityResumed();
//        }
//    }
//
//    /**
//     * This callback will be called after onResume if application is being
//     * resumed from background. <br/>
//     * <p/>
//     * Subclasses can override this method to get this callback.
//     */
//    protected void onAppResumeFromBackground() {
//        if (BuildConfig.DEBUG) {
//            Log.i(LOG_TAG, "onAppResumeFromBackground()");
//        }
//
//    }
//
//    /**
//     * This method should be called to force app assume itself not in
//     * background.
//     */
//    public final void setAppNotInBackground() {
//        Application application = this.getApplication();
//        if (application instanceof BaseApplication) {
//            BaseApplication baseApplication = (BaseApplication) application;
//            baseApplication.setAppInBackground(false);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (BuildConfig.DEBUG) {
//            Log.i(LOG_TAG, "onPause()");
//        }
//
//        Application application = this.getApplication();
//        if (application instanceof BaseApplication) {
//            BaseApplication baseApplication = (BaseApplication) application;
//            baseApplication.onActivityPaused();
//        }
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (BuildConfig.DEBUG) {
//            Log.i(LOG_TAG, "onNewIntent()");
//        }
//    }
//
//    // ////////////////////////////// show and hide ProgressDialog
//
//    /**
//     * Subclass should over-ride this method to update the UI with response,
//     * this base class promises to call this method from UI thread.
//     *
//     * @param serviceResponse
//     */
//    public abstract void updateUi(final boolean status, final int action, final Object serviceResponse);
//
//    /**
//     * Shows a simple native progress dialog<br/>
//     * Subclass can override below two methods for custom dialogs- <br/>
//     * 1. showProgressDialog <br/>
//     * 2. removeProgressDialog
//     *
//     * @param bodyText
//     */
//
//    public void showProgressDialog(String bodyText, final boolean isRequestCancelable) {
//        try {
//            if (isFinishing()) {
//                return;
//            }
//            if (mProgressDialog == null) {
//                mProgressDialog = ProgressHUD.show(this, "", isRequestCancelable, false, null);
//                mProgressDialog.setOnKeyListener(new Dialog.OnKeyListener() {
//                    @Override
//                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        if (keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_SEARCH) {
//                            return true; //
//                        } else if (keyCode == KeyEvent.KEYCODE_BACK && isRequestCancelable) {
//                            Log.e("Ondailogback", "cancel dailog");
//                            RequestManager.cancelRequest();
//                            dialog.dismiss();
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//            }
//
//           // mProgressDialog.setMessage(bodyText);
//
//            if (!mProgressDialog.isShowing()) {
//                mProgressDialog.show();
//            }
//
//        } catch (Exception e) {
//
//        }
//    }
//
//
//    /**
//     * Removes the simple native progress dialog shown via showProgressDialog <br/>
//     * Subclass can override below two methods for custom dialogs- <br/>
//     * 1. showProgressDialog <br/>
//     * 2. removeProgressDialog
//     */
//    public void removeProgressDialog() {
//        try {
//            if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    public void setProgressDialog(ProgressHUD dialog) {
//        this.mProgressDialog = dialog;
//    }
//    // ////////////////////////////// show and hide key-board
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        View v = getCurrentFocus();
//        boolean ret = super.dispatchTouchEvent(event);
//
//        if (v instanceof EditText) {
//            View w = getCurrentFocus();
//            int scrcoords[] = new int[2];
//            w.getLocationOnScreen(scrcoords);
//            float x = event.getRawX() + w.getLeft() - scrcoords[0];
//            float y = event.getRawY() + w.getTop() - scrcoords[1];
//
//            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
//                KeypadUtils.hideSoftKeypad(this);
//            }
//        }
//        return ret;
//    }



}