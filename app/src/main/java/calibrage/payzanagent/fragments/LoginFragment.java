package calibrage.payzanagent.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.model.LoginModel;
import calibrage.payzanagent.model.LoginResponseModel;
import calibrage.payzanagent.model.UpdateAgentRequestModel;
import calibrage.payzanagent.model.UpdateAgentRequestResponceModel;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import calibrage.payzanagent.utils.CommonConstants;
import calibrage.payzanagent.utils.SharedPrefsData;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginFragment extends BaseFragment {

    public static final String TAG = LoginFragment.class.getSimpleName();
    private Button btnLogin;
    private EditText txt_Email, txt_password;
    private Subscription mRegisterSubscription;
    FragmentManager fragmentManager;
    View view;
    public static Toolbar toolbar;
    private Context context;
    private String mobileOrEmail, passCode,dateandtime;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginFragment.this);
                    return false;
                }
            });
        }
        context = this.getActivity();
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.login_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
       /* HomeActivity.toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
       HomeActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeTab();
            }
        });*/
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        txt_password = (EditText) view.findViewById(R.id.txt_password);
        txt_Email = (EditText) view.findViewById(R.id.txt_Email);
        fragmentManager = getActivity().getSupportFragmentManager();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               replaceFragment(getActivity(), MAIN_CONTAINER, new AggrementDocumentsFragment(), TAG, AggrementDocumentsFragment.TAG);
              //  AgentUpdateRequest();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
           //    startActivity(new Intent(LoginActivity.this,AgentRequestsFragment.class));
             //   login();
              // ReplcaFragment(new AgentRequestsFragment());
                /*if (isValidateUi()) {
                    login();
                    dateAndtime();
                   // replaceFragment(getActivity(), MAIN_CONTAINER, new MainFragment(), TAG, MainFragment.TAG);

                    // ReplcaFragment(new AgentRequestsFragment());
                }*/





            }
        });
        return view;
    }

    private void dateAndtime() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = df.format(c.getTime());
        dateandtime = formattedDate;
        SharedPrefsData.getInstance(context).updateStringValue(context,"datetime",dateandtime);
        /*// formattedDate have current date/time
        Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "dateAndtime: "+formattedDate);
       // Now we display formattedDate value in TextView*/


    }

    private boolean isValidateUi() {

        boolean status = true;
        mobileOrEmail = txt_Email.getText().toString().trim();
        passCode = txt_password.getText().toString();
        if (mobileOrEmail.isEmpty()) {
            status = false;
            Toast.makeText(context, "Mobile/Email is required", Toast.LENGTH_SHORT).show();
        } else if (passCode.isEmpty()) {
            status = false;
            Toast.makeText(context, "password is required", Toast.LENGTH_SHORT).show();
        }
        return status;
    }


    private void login() {
        showDialog(getActivity(), "Authenticating...");
        JsonObject object = getLoginObject();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        mRegisterSubscription = service.UserLogin(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponseModel>() {
                    @Override
                    public void onCompleted() {
                        // Toast.makeText(getActivity(), "check", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginResponseModel loginResponseModel) {
                        hideDialog();
                        if(loginResponseModel.getIsSuccess())
                        {
                            Toast.makeText(getActivity(), "sucess", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "onNext: Result :"+loginResponseModel.toString());
                            CommonConstants.USERID = loginResponseModel.getResult().getUser().getId();
                            SharedPrefsData.getInstance(context).updateStringValue(context,"userid",loginResponseModel.getResult().getUser().getId());
                            //    CommonConstants.WALLETID = String.valueOf(loginResponseModel.getData().getUserWallet().getWalletId());
                            // ReplcaFragment(new AgentRequestsFragment());
                            replaceFragment(getActivity(), MAIN_CONTAINER, new MainFragment(), TAG, MainFragment.TAG);
                       /* toolbar.setNavigationIcon(null);
                        toolbar.setTitle("");*/
                            //finish();
                        }else {
                            showToast(context,loginResponseModel.getEndUserMessage());
                        }

                    }
                });

    }




    private JsonObject getLoginObject() {
        LoginModel loginModel = new LoginModel();
        loginModel.setPassword(txt_password.getText().toString());
        loginModel.setUserName(txt_Email.getText().toString());
        return new Gson().toJsonTree(loginModel)
                .getAsJsonObject();
    }

    private void hideSoftKeyboard(LoginFragment loginFragment) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }


}
