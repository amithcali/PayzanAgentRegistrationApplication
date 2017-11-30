package calibrage.payzanagent.networkservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import calibrage.payzanagent.fragments.MainFragment;
import calibrage.payzanagent.model.LoginModel;
import calibrage.payzanagent.model.LoginResponseModel;
import calibrage.payzanagent.utils.CommonConstants;
import calibrage.payzanagent.utils.SharedPrefsData;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Calibrage11 on 11/30/2017.
 */

public class RefreshToken extends BroadcastReceiver {
    Context mContext;
    private Subscription mRegisterSubscription;
    @Override
    public void onReceive(Context mContext, Intent intent) {
        this.mContext = mContext;
        
    }

    private void login() {
       // showDialog(getActivity(), "Authenticating...");
        JsonObject object = getLoginObject();
        MyServices service = ServiceFactory.createRetrofitService(mContext, MyServices.class);
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
                    //    hideDialog();
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
                        Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginResponseModel loginResponseModel) {
                      //  hideDialog();
                        if(loginResponseModel.getIsSuccess())
                        {
                            // Toast.makeText(getActivity(), "sucess", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "onNext: Result :"+loginResponseModel.toString());
                            CommonConstants.USERID = loginResponseModel.getResult().getUser().getId();
                            CommonConstants.USER_NAME = loginResponseModel.getResult().getUser().getUserName();
                            SharedPrefsData.getInstance(mContext).updateStringValue(mContext,"userid",loginResponseModel.getResult().getUser().getId());
                            SharedPrefsData.getInstance(mContext).updateStringValue(mContext,"username",loginResponseModel.getResult().getUser().getUserName());
                            SharedPrefsData.getInstance(mContext).updateStringValue(mContext,"Token",loginResponseModel.getResult().getTokenType()+" "+loginResponseModel.getResult().getAccessToken());

                        }else {

                        }

                    }
                });

    }




    private JsonObject getLoginObject() {
        LoginModel loginModel = new LoginModel();
//        loginModel.setPassword(txt_password.getText().toString());
//        loginModel.setUserName(txt_Email.getText().toString());
        return new Gson().toJsonTree(loginModel)
                .getAsJsonObject();
    }
}
