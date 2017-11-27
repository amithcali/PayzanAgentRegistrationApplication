package calibrage.payzanagent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.adapter.ApprovedAgentsAdapter;
import calibrage.payzanagent.adapter.InProgressRequetAdapter;
import calibrage.payzanagent.model.AgentRequestModel;
import calibrage.payzanagent.networkservice.ApiConstants;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import calibrage.payzanagent.utils.CommonConstants;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApprovedAgentsFragment extends BaseFragment {

    public static final String TAG = ApprovedAgentsFragment.class.getSimpleName();

    View view;
    private RecyclerView recyclerView;
    FragmentManager fragmentManager;
    private Context context;
    TextView noRecords;
    private Subscription operatorSubscription;
    private ArrayList<AgentRequestModel.ListResult> listResults;
    private AgentRequestModel agentRequestModelBundle;
    public  static Toolbar toolbar;

    public ApprovedAgentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
       view = inflater.inflate(R.layout.fragment_approved_agents, container, false) ;
        context = this.getActivity();
//        HomeActivity.toolbar.setTitle(getResources().getString(R.string.approvedagents_sname));
//        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_card_approved);
        noRecords = (TextView)view.findViewById(R.id.no_records);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fragmentManager = getActivity().getSupportFragmentManager();
        if (isOnline(getActivity())) {
            getRequest(CommonConstants.USERID+"/"+CommonConstants.STATUSTYPE_ID_APPROVED);
        } else {
            showToast(getActivity(), getString(R.string.no_internet));
        }


        return view ;
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
                        ApprovedAgentsAdapter approvedAgentsAdapter = new ApprovedAgentsAdapter(context, listResults);
                        recyclerView.setAdapter(approvedAgentsAdapter);
                        //approvedAgentsAdapter.setOnAdapterListener(ApprovedAgentsFragment.this);
                        agentRequestModelBundle = agentRequestModel;

                    }

                });
    }
    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }



}
