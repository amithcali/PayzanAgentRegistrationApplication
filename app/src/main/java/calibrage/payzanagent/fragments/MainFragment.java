package calibrage.payzanagent.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.model.BusinessCategoryModel;
import calibrage.payzanagent.model.HomeModel;
import calibrage.payzanagent.networkservice.ApiConstants;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import calibrage.payzanagent.utils.SharedPrefsData;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = MainFragment.class.getSimpleName();
    View view;
    private Context context;
    private String stUsername;
    private Subscription operatorSubscription;
    String userid;
    TextView txtCount1,txtCount2,txtCount3,txtCount4,txtStatusType1,txtStatusType2,txtStatusType3,txtStatusType4;
    Button btnNewAgent,btnAgentRequest,btnToverify,btnApprovedAgents;
    ArrayList<String> homeArrayList = new ArrayList<String>();
    private ArrayList<HomeModel.Result> homeListResults = new ArrayList<>();
    //,btnSettings
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container,false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        context = this.getActivity();
        userid = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("userid");
        getRequestCount(userid);
        txtCount1 = (TextView) view.findViewById(R.id.txt_count1);
        txtCount2 = (TextView)view.findViewById(R.id.txt_count2);
        txtCount3 = (TextView)view.findViewById(R.id.txt_count3);
        txtCount4 = (TextView)view.findViewById(R.id.txt_count4);
        txtStatusType1 = (TextView)view.findViewById(R.id.txt_status_type1);
        txtStatusType2 = (TextView)view.findViewById(R.id.txt_status_type2);
        txtStatusType3 = (TextView)view.findViewById(R.id.txt_status_type3);
        txtStatusType4 = (TextView)view.findViewById(R.id.txt_status_type4);


        stUsername = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("username");
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.main_sname));
        HomeActivity.toolbar.setSubtitle(stUsername);
        HomeActivity.toolbar.setSubtitleTextColor(ContextCompat.getColor(context,R.color.white_new));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        btnNewAgent = (Button)view.findViewById(R.id.btn_newAgent);
        btnAgentRequest=  (Button)view.findViewById(R.id.btn_agentRequests);
      //  btnSettings  = (Button)view.findViewById(R.id.btn_settings);
        btnToverify = (Button)view.findViewById(R.id.btn_yetToVerify);
        btnApprovedAgents = (Button)view.findViewById(R.id.btn_approvedAgents);
        btnNewAgent.setOnClickListener(this);
        btnAgentRequest.setOnClickListener(this);
        btnToverify.setOnClickListener(this);
        btnApprovedAgents.setOnClickListener(this);
       // btnSettings.setOnClickListener(this);
        return view;
    }

    private void getRequestCount(String providerType) {
        showDialog(getActivity(), "Authenticating...");
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getHomeRequest(ApiConstants.HOME_REQUEST + providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeModel>() {
                    @Override
                    public void onCompleted() {
                        //  Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
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
                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(HomeModel homeModel) {
                        hideDialog();
                        Log.d("response", homeModel.getIsSuccess().toString());
                        homeListResults = (ArrayList<HomeModel.Result>) homeModel.getResult();
                        for (int i = 0; i < homeModel.getResult().size(); i++) {
                            homeArrayList.add(homeModel.getResult().get(i).getStatusType());
                            txtCount1.setText(homeModel.getResult().get(i).getStatusType());
                        }
                     // txtCount1.setText(homeModel.getResult().get);
                    }

                });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_newAgent:
                replaceFragment(getActivity(), MAIN_CONTAINER, new RegistrationViewFragment(), TAG, RegistrationViewFragment.TAG);

                break;
            case R.id.btn_agentRequests:
                replaceFragment(getActivity(),MAIN_CONTAINER,new AgentRequestsFragment(),TAG,AgentRequestsFragment.TAG);
                break;
            case R.id.btn_yetToVerify:
               //showToast(getActivity(),"Yet to implement feature");
                replaceFragment(getActivity(),MAIN_CONTAINER,new InProgressFragment(),TAG,InProgressFragment.TAG);
                break;
            case R.id.btn_approvedAgents:
              //  showToast(getActivity(),"Yet to implement feature");
                replaceFragment(getActivity(),MAIN_CONTAINER,new ApprovedAgentsFragment(),TAG,ApprovedAgentsFragment.TAG);
                break;
          /*  case R.id.btn_settings:
                //  showToast(getActivity(),"Yet to implement feature");
                replaceFragment(getActivity(),MAIN_CONTAINER,new UserProfileHomeFragment(),TAG,UserProfileHomeFragment.TAG);
                break;*/

        }

    }
}
