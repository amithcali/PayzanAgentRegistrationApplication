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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import calibrage.payzanagent.BuildConfig;
import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AgentBankInfo;
import calibrage.payzanagent.model.BankInfoResponseModel;
import calibrage.payzanagent.model.Branch;
import calibrage.payzanagent.model.BusinessCategoryModel;
import calibrage.payzanagent.model.GetBankInfoModel;
import calibrage.payzanagent.model.PersonalInfoResponseModel;
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

import static android.content.ContentValues.TAG;
import static android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR;
import static calibrage.payzanagent.utils.CommonConstants.Is_Update;


public class BankDetailFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = BankDetailFragment.class.getSimpleName();

    View view;
    private Button btnContinue;
    FragmentManager fragmentManager;
    private Context context;

    private Subscription operatorSubscription;
    Spinner spinnerCustom_bank;
    Spinner spinnerCustom_brach;
    int bankId;
    private Button personalButton, bankButton, idButton, documentButton;
    EditText accountName, accountNo, shiftCode;
    ArrayList<String> bankArrayList = new ArrayList<String>();
    ArrayList<String> branchArrayList = new ArrayList<String>();
    private ArrayList<Branch.ListResult> branchListResults = new ArrayList<>();
    private ArrayList<BusinessCategoryModel.ListResult> bankListResults = new ArrayList<>();
    public static Toolbar toolbar;
    private AddAgent addAgent;
    private String straccountname, straccountno, strshiftcode, currentDatetime;
    private AgentBankInfo agentBankInfo;
    BankDetailFragment.CustomSpinnerAdapter customSpinnerAdapter;


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
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        context = this.getActivity();
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.bankdetail_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        currentDatetime = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("datetime");
        agentBankInfo = new AgentBankInfo();
        initCustomSpinner_bank();
        personalButton = (Button) view.findViewById(R.id.btn_personal);
        bankButton = (Button) view.findViewById(R.id.btn_bank);
        idButton = (Button) view.findViewById(R.id.btn_id);
        documentButton = (Button) view.findViewById(R.id.btn_doc);

        bankButton.setOnClickListener(this);
        idButton.setOnClickListener(this);
       /* personalButton.setOnClickListener(this);
        documentButton.setOnClickListener(this);*/


        getRequestBank(CommonConstants.BANK_CATEGORY_ID);
        initCustomSpinner_branch();


        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        accountName = (EditText) view.findViewById(R.id.txt_accountholdername);
        accountNo = (EditText) view.findViewById(R.id.txt_accountno);
        shiftCode = (EditText) view.findViewById(R.id.txt_swift_code);

        fragmentManager = getActivity().getSupportFragmentManager();
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidateUi()) {
                    //    login();
                    //  agentBankDetails();
                    postBankInfo();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                   /* Bundle bundle = new Bundle();
                    bundle.putParcelable("bankinfo", addAgent);
                    Fragment fragment = new IdProofFragment();
                    fragment.setArguments(bundle);
                 //   ReplcaFragment(fragment);
                   *//* fragmentManager.beginTransaction()
                            .replace(R.id.content_frame,  fragment,"IdTag")
                            .commit();*//*
                    replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, IdProofFragment.TAG);*/

                }

                //startActivity(new Intent(BankDetailsActivity.this,IdProofActivity.class));
            }
        });
     /*   Bundle bundle = getArguments();
        if (bundle != null) {
            agentId = bundle.getInt("agentid");
            Log.d(TAG, "onCreateView: agentid"+agentId);
           // addAgent = bundle.getParcelable("personalinfo");
        }*/

        return view;
    }


    private JsonObject agentBankDetails() {
        agentBankInfo.setModifiedBy(CommonConstants.USERID);
        agentBankInfo.setModified(currentDatetime);
        agentBankInfo.setCreatedBy(CommonConstants.USERID);
        agentBankInfo.setCreated(currentDatetime);
        agentBankInfo.setIsActive(true);
        agentBankInfo.setAccountHolderName(straccountname);
        agentBankInfo.setAccountNumber(straccountno);
        agentBankInfo.setBankId("" + branchListResults.get(spinnerCustom_brach.getSelectedItemPosition()).getId());
        agentBankInfo.setAgentId("" + CommonConstants.AGENT_ID);
        agentBankInfo.setId(0);
        return new Gson().toJsonTree(agentBankInfo)
                .getAsJsonObject();
        //addAgent.setAgentBankInfo(agentBankInfo);
    }


    private void postBankInfo() {
        showDialog(getActivity(), "Authenticating...");
        JsonObject object = agentBankDetails();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        operatorSubscription = service.postBankInfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BankInfoResponseModel>() {
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
                    public void onNext(BankInfoResponseModel bankInfoResponseModel) {
                        hideDialog();
                        if (bankInfoResponseModel.getIsSuccess()) {
                            showToast(context, bankInfoResponseModel.getEndUserMessage());
                            replaceFragment(getActivity(), MAIN_CONTAINER, new IdProofFragment(), TAG, IdProofFragment.TAG);

                        } else {
                            showToast(context, bankInfoResponseModel.getEndUserMessage());
                        }

                    }
                });

    }




   /* private void agentBankDetails() {
        agentBankInfo.setModifiedBy(CommonConstants.USERID);
        agentBankInfo.setModified(currentDatetime);
        agentBankInfo.setCreatedBy(CommonConstants.USERID);
        agentBankInfo.setCreated(currentDatetime);
        agentBankInfo.setIsActive(true);
        agentBankInfo.setAccountHolderName("amithsai");
        agentBankInfo.setAccountNumber("1263545474");
        agentBankInfo.setBankId(""+15);
        agentBankInfo.setAgentId(null);
        agentBankInfo.setId(0);
        addAgent.setAgentBankInfo(agentBankInfo);
    }*/

    private boolean isValidateUi() {
        boolean status = true;
        straccountname = accountName.getText().toString().trim();
        straccountno = accountNo.getText().toString();
        strshiftcode = shiftCode.getText().toString();


        if (straccountname.isEmpty() || accountName.getText().length() < 4) {
            status = false;
            accountName.setError("AccountHolder Name is required");
            accountName.requestFocusFromTouch();
            //Toast.makeText(context, "AccountHolder Name is required", Toast.LENGTH_SHORT).show();
        } else if (straccountno.isEmpty()) {
            status = false;
            accountNo.setError("Account Number is required");
            accountNo.requestFocusFromTouch();
            //  Toast.makeText(context, "Account Number is required", Toast.LENGTH_SHORT).show();
        } else if (strshiftcode.isEmpty()) {
            status = false;
            shiftCode.setError("Shiftcode is required");
            shiftCode.requestFocusFromTouch();
            // Toast.makeText(context, "Shiftcode is required", Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    private void getRequestBranch(String providerType) {
        showDialog(getActivity(), "Authenticating...");
        String data = BuildConfig.LOCAL_URL + ApiConstants.BRANCH_REQUESTS + providerType;
        Log.d(TAG, "getRequestBranch: URL:" + data);

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

                        branchArrayList = new ArrayList();
                        if (branch.getListResult().size() > 0) {
                            for (int i = 0; i < branch.getListResult().size(); i++) {
                                branchArrayList.add(branch.getListResult().get(i).getBranchName());
                            }
                        } else {
                            branchArrayList.clear();
                            customSpinnerAdapter.notifyDataSetChanged();
                            shiftCode.setText("");
                        }

                        customSpinnerAdapter = new BankDetailFragment.CustomSpinnerAdapter(getActivity(), branchArrayList, false);
                        spinnerCustom_brach.setAdapter(customSpinnerAdapter);

                       // if (Is_Update) {
                            getAgentBankInfo("dce0a289-5803-46fb-ae19-13f737fed7c3");
                      //  }

                    }

                });
    }

    private void getRequestBank(String providerType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBusinessRequest(ApiConstants.BUSINESS_CAT_REQUESTS + providerType)
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
                        //   bankArrayList.add(0,"--Select Bank--");
                        for (int i = 0; i < businessCategoryModel.getListResult().size(); i++) {
                            bankArrayList.add(businessCategoryModel.getListResult().get(i).getDescription());

                        }

                        BankDetailFragment.CustomSpinnerAdapterBank customSpinnerAdapterBank = new BankDetailFragment.CustomSpinnerAdapterBank(getActivity(), bankArrayList);
                        spinnerCustom_bank.setAdapter(customSpinnerAdapterBank);
                    }

                });


    }

    private void getAgentBankInfo(String agentId) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.GetAgentBankInfo(ApiConstants.GET_AGENT_BANK_INFO + agentId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetBankInfoModel>() {
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
                    public void onNext(GetBankInfoModel getBankInfoModel) {
                        accountName.setText(getBankInfoModel.getListResult().get(0).getAccountHolderName());
                        accountNo.setText(getBankInfoModel.getListResult().get(0).getAccountHolderName());
                        spinnerCustom_bank.setSelection(bankArrayList.indexOf(getBankInfoModel.getListResult().get(0).getBankName()));
                        spinnerCustom_brach.setSelection(branchArrayList.indexOf(getBankInfoModel.getListResult().get(0).getBranchName()));
                    }

                });
    }

    private void initCustomSpinner_branch() {
        spinnerCustom_brach = (Spinner) view.findViewById(R.id.spinner_branch);

        spinnerCustom_brach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                if (!branchListResults.isEmpty()) {
                    shiftCode.setText(branchListResults.get(position).getSwiftCode());
                }

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

//                String item = parent.getItemAtPosition(position).toString();
                bankId = (int) parent.getSelectedItemId();
                // int i = 12;
                Log.d(TAG, "onItemSelected: " + bankId);
                bankId = bankListResults.get((int) parent.getSelectedItemId()).getId();
                getRequestBranch(String.valueOf(bankId));
                // getRequestBranch(String.valueOf(i));
                //    Toast.makeText(parent.getContext(), "bankkkkkkk" +bankId, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.btn_personal:
                showToast(getActivity(),"Please Fill The Personal Details");

                // replaceFragment(getActivity(), MAIN_CONTAINER, new RegistrationViewFragment(), TAG, RegistrationViewFragment.TAG);

                break;*/
            case R.id.btn_bank:
                showToast(getActivity(), "Please Fill The Bank Details");
                //replaceFragment(getActivity(),MAIN_CONTAINER,new AgentRequestsFragment(),TAG,AgentRequestsFragment.TAG);
                break;
            case R.id.btn_id:
                //   showToast(getActivity(),"Please Fill The Personal Details");
                if (isValidateUi()) {
                    //    login();
                    //  agentBankDetails();
                    postBankInfo();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                   /* Bundle bundle = new Bundle();
                    bundle.putParcelable("bankinfo", addAgent);
                    Fragment fragment = new IdProofFragment();
                    fragment.setArguments(bundle);
                 //   ReplcaFragment(fragment);
                   *//* fragmentManager.beginTransaction()
                            .replace(R.id.content_frame,  fragment,"IdTag")
                            .commit();*//*
                    replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, IdProofFragment.TAG);*/

                }


                // replaceFragment(getActivity(),MAIN_CONTAINER,new InProgressFragment(),TAG,InProgressFragment.TAG);
                break;
          /*  case R.id.btn_doc:
                showToast(getActivity(),"Please Fill The Personal Details");
                //replaceFragment(getActivity(),MAIN_CONTAINER,new ApprovedAgentsFragment(),TAG,ApprovedAgentsFragment.TAG);
                break;*/
        }
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

    private class CustomSpinnerAdapterBank extends BaseAdapter implements SpinnerAdapter {
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
