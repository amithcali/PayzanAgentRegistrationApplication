package calibrage.payzanagent.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.adapter.AgentRequetAdapter;
import calibrage.payzanagent.adapter.GenericAdapter;
import calibrage.payzanagent.interfaces.RequestClickListiner;
import calibrage.payzanagent.model.AgentRequestModel;
import calibrage.payzanagent.model.UpdateAgentRequestModel;
import calibrage.payzanagent.model.UpdateAgentRequestResponceModel;
import calibrage.payzanagent.networkservice.ApiConstants;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import calibrage.payzanagent.utils.CommonConstants;
import calibrage.payzanagent.utils.SharedPrefsData;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AgentRequestsFragment extends  BaseFragment implements RequestClickListiner {

    public static final String TAG = AgentRequestsFragment.class.getSimpleName();


    View view;
    private RecyclerView recyclerView;
    FragmentManager fragmentManager;
    private Context context;
    private Subscription operatorSubscription;
    private Subscription mRegisterSubscription;
    private String stComment,currentDatetime;
    private int position;
    TextView noRecords;
    private boolean isPickorHold;
    private ArrayList<AgentRequestModel.ListResult> listResults;
    private AgentRequestModel agentRequestModelBundle;
    public  static Toolbar toolbar;

    // implements OnRequestSelected
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        view = inflater.inflate(R.layout.fragment_agent_request, container, false);
        context = this.getActivity();
//        HomeActivity.toolbar.setTitle(getResources().getString(R.string.agentrequest_sname));
//        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        currentDatetime = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("datetime");
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_card);
        noRecords = (TextView)view.findViewById(R.id.no_records);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fragmentManager = getActivity().getSupportFragmentManager();
        String val= SharedPrefsData.getInstance(context).getStringFromSharedPrefs("mahesh");
       /* showToast(context, "Value :"+val);
        Log.d(TAG, "onCreateView: "+val);*/
        if (isOnline(getActivity())) {
            getRequest(CommonConstants.USERID+"/"+CommonConstants.STATUSTYPE_ID_NEW+","+CommonConstants.STATUSTYPE_ID_REJECTED+","+CommonConstants.STATUSTYPE_ID_HOLD);
        } else {
            showToast(getActivity(), getString(R.string.no_internet));
        }


        return view;

    }




    private void getRequest(String providerType) {
        showDialog(getActivity(), "Authenticating...");
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getRequest(ApiConstants.AGENT_REQUESTS + providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AgentRequestModel>() {
                    @Override
                    public void onCompleted() {
                       // Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                    public void onNext(AgentRequestModel agentRequestModel) {
                        hideDialog();
                       // Log.d("response", agentRequestModel.getIsSuccess().toString());

                        listResults = (ArrayList<AgentRequestModel.ListResult>) agentRequestModel.getListResult();
                        if (listResults.isEmpty()){
                            noRecords.setVisibility(View.VISIBLE);

                        }
                        else {
                            noRecords.setVisibility(View.GONE);
                        }
                        AgentRequetAdapter agentRequetAdapter = new AgentRequetAdapter(context, listResults);
                        recyclerView.setAdapter(agentRequetAdapter);
                        agentRequetAdapter.setOnAdapterListener(AgentRequestsFragment.this);
                        agentRequestModelBundle = agentRequestModel;

                    }

                });

    }

    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }


    @SuppressLint("ResourceType")
    @Override
    public void onAdapterClickListiner(int pos,boolean isPickorHold) {
        position = pos;
        this.isPickorHold = isPickorHold;
        final Dialog adb = new Dialog(getActivity());
        adb.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.row_comment_box, null);
        adb.setContentView(layout);
       /* adb.getWindow().setLayout(500, 200);*/
        adb.getWindow().setLayout((int) getResources().getDimension(R.dimen.comments_box_width), (int) getResources().getDimension(R.dimen.comments_box_height));
      //  adb.getWindow().setLayout(R.dimen.comments_box_width, R.dimen.comments_box_height);
        adb.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        adb.show();
        final EditText etComment = (EditText) layout.findViewById(R.id.etComment);
        Button btnSubmit = (Button) layout.findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                stComment = etComment.getText().toString();
                if (stComment.equalsIgnoreCase("")) {
                    showToast(getActivity(), "Please write comments.");

                } else {
                    submitRequest();
                    adb.dismiss();


                }
            }
        });
    /*    Bundle bundle = new Bundle();
        bundle.putParcelable("request", agentRequestModelBundle);
        bundle.putInt("position",pos);
        Fragment fragment = new RegistrationViewFragment();
        fragment.setArguments(bundle);
//        ReplcaFragment(fragment);
        replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, RegistrationViewFragment.TAG);
       // fragmentManager.beginTransaction().replace(R.id.content_frame, fragment,"AgentTag").commit();*/
    }

    private void submitRequest() {
        showDialog(getActivity(), "Authenticating...");
        JsonObject object = getLoginObject();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        mRegisterSubscription = service.AgentUpdateRequest(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateAgentRequestResponceModel>() {
                    @Override
                    public void onCompleted() {
                        // Toast.makeText(getActivity(), "check", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UpdateAgentRequestResponceModel updateAgentRequestResponceModel) {
                        hideDialog();
                        if(updateAgentRequestResponceModel.getIsSuccess())
                        {
                           /* Toast.makeText(getActivity(), "sucess", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onNext: Result :"+updateAgentRequestResponceModel.toString());*/
                            //showToast(getActivity(), "Updated Successfully.....!");
                            showToast(getActivity(),updateAgentRequestResponceModel.getEndUserMessage());
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                            getRequest(CommonConstants.USERID+"/"+CommonConstants.STATUSTYPE_ID_NEW+","+CommonConstants.STATUSTYPE_ID_REJECTED+","+CommonConstants.STATUSTYPE_ID_HOLD);

                        }else {
                            Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private JsonObject getLoginObject() {
        UpdateAgentRequestModel updateAgentRequestModel = new UpdateAgentRequestModel();
        updateAgentRequestModel.setAgentRequestId(listResults.get(position).getId());
        if(isPickorHold ){
            updateAgentRequestModel.setStatusTypeId(Integer.valueOf(CommonConstants.STATUSTYPE_ID_IN_PROGRESS));
        }else {
            updateAgentRequestModel.setStatusTypeId(Integer.valueOf(CommonConstants.STATUSTYPE_ID_HOLD));
        }

        updateAgentRequestModel.setAssignToUserId(SharedPrefsData.getInstance(context).getStringFromSharedPrefs("userid"));
        updateAgentRequestModel.setComments(stComment);
        updateAgentRequestModel.setId(null);
        updateAgentRequestModel.setIsActive(true);
        updateAgentRequestModel.setCreatedBy(SharedPrefsData.getInstance(context).getStringFromSharedPrefs("userid"));
        updateAgentRequestModel.setModifiedBy(SharedPrefsData.getInstance(context).getStringFromSharedPrefs("userid"));
        updateAgentRequestModel.setCreated(currentDatetime);
        updateAgentRequestModel.setModified(currentDatetime);
        return new Gson().toJsonTree(updateAgentRequestModel)
                .getAsJsonObject();
    }
}
