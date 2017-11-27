package calibrage.payzanagent.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import calibrage.payzanagent.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    public static final int MAIN_CONTAINER = R.id.content_frame;
  //  private View view;


    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    protected void popUpFromBackStack(FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStack();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_agent_request, container, false);
//        return view;
//    }

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
    public void replaceFinal(final FragmentActivity activity, final int container, final Fragment
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
                for(int i = 0; i < activity.getSupportFragmentManager().getBackStackEntryCount()-1
                        ; ++i) {
                    activity.getSupportFragmentManager().popBackStack();
                }
       /*         fragmentTransaction
                        .addToBackStack(cuurentFragmentTag)
                        .add(container, fragment, newFragmentTag);*/
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            new Handler().post(mPendingRunnable);
        }
    }

    private ProgressDialog mProgressDialog;

    public void showDialog(final FragmentActivity activity, final String message) {


        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });


    } public void showDialogAsk(final FragmentActivity activity, final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(activity);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage(message);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setCanceledOnTouchOutside(false);
                    }
                    if (mProgressDialog != null && !mProgressDialog.isShowing())
                        mProgressDialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }
    public void hideDialogAsk() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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

   /* // format date time string
    public static String formatDateTimeBlog(String dateTime) {
        try {
            String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
            dateTime = new SimpleDateFormat(dateFormat, Locale.US)
                    .format(new SimpleDateFormat(Constant.DATE_TIME, Locale.US).parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }*/
}
