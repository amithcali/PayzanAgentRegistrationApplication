package calibrage.payzanagent.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import calibrage.payzanagent.BuildConfig;
import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AgentBankInfo;
import calibrage.payzanagent.model.Branch;
import calibrage.payzanagent.model.BusinessCategoryModel;
import calibrage.payzanagent.networkservice.ApiConstants;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import calibrage.payzanagent.utils.CommonConstants;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;


public class BankDetailFragment extends BaseFragment {

    public static final String TAG = BankDetailFragment.class.getSimpleName();

    View view;
    private Button btnContinue;
    FragmentManager fragmentManager;
    private Context context;

    private Subscription operatorSubscription;
    Spinner spinnerCustom_bank;
    Spinner spinnerCustom_brach;
    int bankId;
    EditText accountName,accountNo,shiftCode;
    ArrayList<String> bankArrayList = new ArrayList<String>();
    ArrayList<String> branchArrayList = new ArrayList<String>();
    private ArrayList<Branch.ListResult> branchListResults = new ArrayList<>();
    private ArrayList<BusinessCategoryModel.ListResult> bankListResults = new ArrayList<>();
    public  static Toolbar toolbar;
    private AddAgent addAgent;
    private String straccountname,straccountno,strshiftcode;
    private AgentBankInfo agentBankInfo;


    public BankDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bank_detail, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        context = this.getActivity();
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.bankdetail_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        agentBankInfo = new AgentBankInfo();
        initCustomSpinner_bank();
        getRequestBank(CommonConstants.BANK_CATEGORY_ID);
        initCustomSpinner_branch();


        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        accountName = (EditText)view.findViewById(R.id.txt_accountholdername);
        accountNo = (EditText)view.findViewById(R.id.txt_accountno);
        shiftCode = (EditText)view.findViewById(R.id.txt_swift_code);

        fragmentManager = getActivity().getSupportFragmentManager();
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidateUi()) {
                    //    login();
                    agentBankDetails();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bankinfo", addAgent);
                    Fragment fragment = new IdProofFragment();
                    fragment.setArguments(bundle);
                 //   ReplcaFragment(fragment);
                   /* fragmentManager.beginTransaction()
                            .replace(R.id.content_frame,  fragment,"IdTag")
                            .commit();*/
                    replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, IdProofFragment.TAG);

                }

                //startActivity(new Intent(BankDetailsActivity.this,IdProofActivity.class));
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            addAgent = bundle.getParcelable("personalinfo");
        }
       /* view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    closeTab();

                    return true;
                } else {
                    return false;
                }
            }
        });*/
        return view;
    }

  /*  private void closeTab() {
//        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("BankTag");
//        if (fragment != null)
//            getActivity().getSupportFragmentManager().beginTransaction().remove(new BankDetailFragment()).commit();

        getActivity().getSupportFragmentManager().beginTransaction().remove(new BankDetailFragment()).commit();
        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("123");

        fragmentManager.beginTransaction()
                .add(R.id.content_frame, new RegistrationViewFragment(),"RequestTag")
                .commit();
    }*/

    private void agentBankDetails() {
        agentBankInfo.setModifiedBy(CommonConstants.USERID);
        agentBankInfo.setModified("2017-10-30T17:15:42.569Z");
        agentBankInfo.setCreatedBy(CommonConstants.USERID);
        agentBankInfo.setCreated("2017-10-30T17:15:42.569Z");
        agentBankInfo.setIsActive(true);
        agentBankInfo.setAccountHolderName(straccountname);
        agentBankInfo.setAccountNumber(straccountno);
        agentBankInfo.setBankId(""+bankListResults.get(spinnerCustom_bank.getSelectedItemPosition()).getId());
        agentBankInfo.setAgentId(null);
        agentBankInfo.setId(0);
        addAgent.setAgentBankInfo(agentBankInfo);
    }

    private boolean isValidateUi() {
        boolean status = true;
        straccountname = accountName.getText().toString().trim();
        straccountno = accountNo.getText().toString();
        strshiftcode=shiftCode.getText().toString();



        if (straccountname.isEmpty()) {
            status = false;
            Toast.makeText(context, "AccountHolder Name is required", Toast.LENGTH_SHORT).show();
        }else if (straccountno.isEmpty()) {
            status = false;
            Toast.makeText(context, "Account Number is required", Toast.LENGTH_SHORT).show();
        }else if (strshiftcode.isEmpty()) {
            status = false;
            Toast.makeText(context, "Shiftcode is required", Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    private void getRequestBranch(String providerType) {
        showDialog(getActivity(), "Authenticating...");
       String data= BuildConfig.LOCAL_URL+ApiConstants.BRANCH_REQUESTS+providerType ;
        Log.d(TAG, "getRequestBranch: URL:"+data);

        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBranchRequest(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Branch>() {
                    @Override
                    public void onCompleted() {
                        //Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                    public void onNext(Branch branch) {
                        hideDialog();
                        Log.d("response", branch.getIsSuccess().toString());
                        branchListResults = (ArrayList<Branch.ListResult>) branch.getListResult();
                        for (int i = 0; i < branch.getListResult().size(); i++) {
                            branchArrayList.add(branch.getListResult().get(i).getBranchName());
                        }
                        BankDetailFragment.CustomSpinnerAdapter customSpinnerAdapter = new BankDetailFragment.CustomSpinnerAdapter(getActivity(), branchArrayList, false);
                        spinnerCustom_brach.setAdapter(customSpinnerAdapter);

                    }

                });
    }

    private void getRequestBank(String providerType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBusinessRequest(ApiConstants.BUSINESS_CAT_REQUESTS + Integer.parseInt(providerType))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BusinessCategoryModel>() {
                    @Override
                    public void onCompleted() {
                     //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BusinessCategoryModel businessCategoryModel) {
                        Log.d("response", businessCategoryModel.getIsSuccess().toString());
                        bankListResults = (ArrayList<BusinessCategoryModel.ListResult>) businessCategoryModel.getListResult();
                        for (int i = 0; i < businessCategoryModel.getListResult().size(); i++) {
                            bankArrayList.add(businessCategoryModel.getListResult().get(i).getDescription());

                        }

                        BankDetailFragment.CustomSpinnerAdapterBank customSpinnerAdapterBank =new BankDetailFragment.CustomSpinnerAdapterBank(getActivity(), bankArrayList);
                        spinnerCustom_bank.setAdapter(customSpinnerAdapterBank);
                    }

                });


    }

    private void initCustomSpinner_branch() {
        spinnerCustom_brach = (Spinner) view.findViewById(R.id.spinner_branch);

        spinnerCustom_brach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                shiftCode.setText(branchListResults.get(position).getSwiftCode());
             //    Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item+parent.getSelectedItemId(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void initCustomSpinner_bank() {

        spinnerCustom_bank = (Spinner) view.findViewById(R.id.spinner_bank);
        spinnerCustom_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                bankId = (int) parent.getSelectedItemId();
                getRequestBranch(String.valueOf(bankId));
               //    Toast.makeText(parent.getContext(), "bankkkkkkk" +bankId, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;
        private boolean isbank;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr, boolean isbank) {
            this.asr = asr;
            activity = context;
            this.isbank = isbank;
        }


        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }


        @Override
        public View getDropDownView(final int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));

            return txt;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public View getView(final int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());

            txt.setGravity(Gravity.LEFT);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down24, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));

            return txt;
        }
    }

    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private class CustomSpinnerAdapterBank extends BaseAdapter implements SpinnerAdapter{
        private final Context activity;
        private ArrayList<String> asr;


        private CustomSpinnerAdapterBank(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
        }

        @Override
        public int getCount() {
            return asr.size();
        }

        @Override
        public Object getItem(int i) {
            return asr.get(i);
        }

        @Override
        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.LEFT);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down24, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

    }

}
