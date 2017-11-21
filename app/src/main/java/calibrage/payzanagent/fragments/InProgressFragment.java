package calibrage.payzanagent.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.adapter.AgentRequetAdapter;
import calibrage.payzanagent.adapter.InProgressRequetAdapter;
import calibrage.payzanagent.interfaces.RequestClickListiner;
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

public class InProgressFragment extends BaseFragment implements RequestClickListiner {

    public static final String TAG = InProgressFragment.class.getSimpleName();

    View view;
    private RecyclerView recyclerView;
    FragmentManager fragmentManager;
    private Context context;
    TextView noRecords;
    private Subscription operatorSubscription;
    private ArrayList<AgentRequestModel.ListResult> listResults;
    private AgentRequestModel agentRequestModelBundle;
    public  static Toolbar toolbar;
    private EditText search;
    private   InProgressRequetAdapter inProgressRequetAdapter;
    private List<String> list = new ArrayList<String>();

    public InProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        view = inflater.inflate(R.layout.fragment_in_progress, container, false) ;
        context = this.getActivity();
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.inprogressrequest_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_card_inprogress);
        noRecords = (TextView)view.findViewById(R.id.no_records);
        search = (EditText)view.findViewById(R.id.search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fragmentManager = getActivity().getSupportFragmentManager();
        if (isOnline(getActivity())) {
            getRequest(CommonConstants.USERID+"/"+CommonConstants.STATUSTYPE_ID_IN_PROGRESS+","+CommonConstants.STATUSTYPE_ID_SUBMIT_FOR_REVIEW);
        } else {
            showToast(getActivity(), getString(R.string.no_internet));
        }
      //  addTextListener();


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
                        inProgressRequetAdapter = new InProgressRequetAdapter(context, listResults);
                        recyclerView.setAdapter(inProgressRequetAdapter);
                        inProgressRequetAdapter.setOnAdapterListener(InProgressFragment.this);
                        agentRequestModelBundle = agentRequestModel;
                        for (int i = 0; i <listResults.size(); i++) {
                            list.add(listResults.get(i).getMobileNumber());
                        }

                    }

                });

    }

    public void addTextListener(){

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<String> filteredList = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {

                    final String text = list.get(i).toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(list.get(i));
                    }
                }

                inProgressRequetAdapter = new InProgressRequetAdapter(context, listResults);
                recyclerView.setAdapter(inProgressRequetAdapter);
                inProgressRequetAdapter.setOnAdapterListener(InProgressFragment.this);
                inProgressRequetAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }


    @Override
    public void onAdapterClickListiner(int pos,boolean b) {
             Bundle bundle = new Bundle();
        bundle.putParcelable("request", agentRequestModelBundle);
        bundle.putInt("position",pos);
        Fragment fragment = new RegistrationViewFragment();
        fragment.setArguments(bundle);
//        ReplcaFragment(fragment);
        replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, RegistrationViewFragment.TAG);
       // fragmentManager.beginTransaction().replace(R.id.content_frame, fragment,"AgentTag").commit();
      //  replaceFragment(getActivity(), MAIN_CONTAINER, new RegistrationViewFragment(), TAG, RegistrationViewFragment.TAG);

    }
}
