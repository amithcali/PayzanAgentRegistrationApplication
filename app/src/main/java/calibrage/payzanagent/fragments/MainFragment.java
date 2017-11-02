package calibrage.payzanagent.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = MainFragment.class.getSimpleName();
    View view;
    private Context context;
    Button btnNewAgent,btnAgentRequest,btnToverify,btnApprovedAgents;
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
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.main_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        btnNewAgent = (Button)view.findViewById(R.id.btn_newAgent);
        btnAgentRequest=  (Button)view.findViewById(R.id.btn_agentRequests);
        btnToverify = (Button)view.findViewById(R.id.btn_yetToVerify);
        btnApprovedAgents = (Button)view.findViewById(R.id.btn_approvedAgents);
        btnNewAgent.setOnClickListener(this);
        btnAgentRequest.setOnClickListener(this);
        btnToverify.setOnClickListener(this);
        btnApprovedAgents.setOnClickListener(this);
        return view;
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

               showToast(getActivity(),"Yet to implement feature");
                //replaceFragment(getActivity(), MAIN_CONTAINER, new AggrementDocumentsFragment(), TAG, AggrementDocumentsFragment.TAG);
                break;
            case R.id.btn_approvedAgents:
                showToast(getActivity(),"Yet to implement feature");
                break;

        }

    }
}
