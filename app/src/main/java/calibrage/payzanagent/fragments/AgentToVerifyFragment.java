package calibrage.payzanagent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.model.AgentRequestModel;
import rx.Subscription;

public class AgentToVerifyFragment extends BaseFragment{

   public static final String TAG = AgentToVerifyFragment.class.getSimpleName();

   View view;
    private RecyclerView recyclerView;
    FragmentManager fragmentManager;
    private Context context;
    private Subscription operatorSubscription;
    private ArrayList<AgentRequestModel.ListResult> listResults;
    private AgentRequestModel agentRequestModelBundle;
    public  static Toolbar toolbar;

    public AgentToVerifyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_agent_to_verify, container, false);

        return view;
    }


}
