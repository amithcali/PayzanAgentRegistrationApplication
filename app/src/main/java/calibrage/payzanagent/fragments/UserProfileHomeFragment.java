package calibrage.payzanagent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.utils.SharedPrefsData;

public class UserProfileHomeFragment extends BaseFragment {
    public static final String TAG = UserProfileHomeFragment.class.getSimpleName();

    View  view;
    Button btnSignOut;
    private Context context;
    public UserProfileHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_user_profile_home, container, false);
        context = this.getActivity();
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.userprofile_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
       btnSignOut = (Button)view.findViewById(R.id.btn_sign_out);
       btnSignOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SharedPrefsData.getInstance(getActivity()).ClearData(getActivity());
               replaceFragment(getActivity(),MAIN_CONTAINER,new LoginFragment(),TAG,LoginFragment.TAG);
           }
       });
        return view ;
    }


}
