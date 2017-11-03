package calibrage.payzanagent.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import calibrage.payzanagent.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    public static final int MAIN_CONTAINER = R.id.content_frame;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    protected void popUpFromBackStack(FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStack();
    }

    public static void addFragment(FragmentActivity activity, int container, Fragment fragment,
                                   String cuurentFragmentTag, String newFragmentTag) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(cuurentFragmentTag)
                .add(container, fragment, newFragmentTag)
                .commit();
    }

    public void replaceFragment(final FragmentActivity activity, final int container, final Fragment
            fragment, final String cuurentFragmentTag, final String newFragmentTag) {
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                FragmentTransaction fragmentTransaction = activity
                        .getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction
                        .addToBackStack(cuurentFragmentTag)
                        .add(container, fragment, newFragmentTag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            new Handler().post(mPendingRunnable);
        }
    }

    private ProgressDialog mProgressDialog;

    public void showDialog(FragmentActivity activity, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);

        }
        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();

    }

    public void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

}
