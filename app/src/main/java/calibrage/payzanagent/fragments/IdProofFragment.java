package calibrage.payzanagent.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Collections;
import java.util.List;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.adapter.IdproofAdapter;
import calibrage.payzanagent.interfaces.DeleteIdproofListiner;
import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AgentIdProof;
import calibrage.payzanagent.model.AgentRequestModel;
import calibrage.payzanagent.model.BankInfoResponseModel;
import calibrage.payzanagent.model.BusinessCategoryModel;
import calibrage.payzanagent.model.GetBankInfoModel;
import calibrage.payzanagent.model.GetIdproofModel;
import calibrage.payzanagent.model.IdProofModel;
import calibrage.payzanagent.model.IdProofResponseModel;
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

public class IdProofFragment extends BaseFragment implements View.OnClickListener,DeleteIdproofListiner {

    public static final String TAG = IdProofFragment.class.getSimpleName();

    View view;
    private Button btnContinue;
    FragmentManager fragmentManager;
    public  static Toolbar toolbar;
    private Context context;
    String personalIdNumber,financialIdNumber,currentDatetime;
    EditText numberpersonal,numberfinancial;
    Spinner spinnerCustom_personalId,spinnerCustom_finacialId;
    private Subscription operatorSubscription;
    private Button personalButton,bankButton,idButton,documentButton;
    private ArrayList<AgentRequestModel.ListResult> listResults;
    ArrayList<String> businessArrayList = new ArrayList<String>();
    private ArrayList<GetIdproofModel.ListResult> agentIdproofList = new ArrayList<>();

    ArrayList<String> financiaStringArrayList = new ArrayList<String>();
   // private AddAgent addAgent;
  //  AgentIdProof agentidProof;
    private AgentIdProof idProof;
    private ArrayList<IdProofModel> idProofArrayList;
    private IdProofModel agentIdProof,agentFinancialProof;
    private ArrayList<BusinessCategoryModel.ListResult> businessListResults =new ArrayList<>();
    private ArrayList<BusinessCategoryModel.ListResult> financialListResults =new ArrayList<>();
    private RecyclerView  recylerView;

    public IdProofFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view =  inflater.inflate(R.layout.fragment_id_proof, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        btnContinue = (Button)view.findViewById(R.id.btn_continue);
        numberpersonal = (EditText)view.findViewById(R.id.txt_number);
        numberfinancial = (EditText)view.findViewById(R.id.txt_number_financial);
        recylerView = (RecyclerView) view.findViewById(R.id.recylerview);
        context = this.getActivity();
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.agentrequest_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        currentDatetime = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("datetime");
      //  agentIdProofArrayList = new ArrayList<>();
        listResults =new  ArrayList();
        idProofArrayList = new ArrayList<>();
        agentFinancialProof = new IdProofModel();
        agentIdProof = new IdProofModel();
        idProof = new AgentIdProof();
        initCustomSpinner_personalId();
        getRequest(CommonConstants.PERSONALID_CATEGORY_ID);
        personalButton = (Button)view.findViewById(R.id.btn_personal);
        bankButton = (Button)view.findViewById(R.id.btn_bank);
        idButton = (Button)view.findViewById(R.id.btn_id);
        documentButton = (Button)view.findViewById(R.id.btn_doc);

       /* personalButton.setOnClickListener(this);
        bankButton.setOnClickListener(this);*/
        idButton.setOnClickListener(this);
        documentButton.setOnClickListener(this);

        initCustomSpinner_financialId();
        getRequestFinacial(CommonConstants.FINANCIALID_CATEGORY_ID);

        getAgentIdproofInfo("dce0a289-5803-46fb-ae19-13f737fed7c3");

        fragmentManager = getActivity().getSupportFragmentManager();
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidateUi()) {
                  //  addIdProofDetails();
                    postIdInfo();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                  /*  Bundle bundle = new Bundle();
                    bundle.putParcelable("idproof", addAgent);
                    Fragment fragment = new AggrementDocumentsFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, AggrementDocumentsFragment.TAG);*/

                }

                //startActivity(new Intent(BankDetailsActivity.this,IdProofActivity.class));
            }
        });
       /* Bundle bundle = getArguments();
        if (bundle != null) {
            agentId = bundle.getInt("agentid");
            Log.d(TAG, "onCreateView: agentid"+agentId)
            //addAgent = bundle.getParcelable("bankinfo");
        }
*/
        return view;
    }

  /*  private void closeTab() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("IdTag");
        if (fragment != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        HomeActivity.toolbar.setNavigationIcon(null);
        HomeActivity.toolbar.setTitle("");

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new BankDetailFragment(),"BankTag")
                .commit();

    }*/

 /*   private void addIdProofDetails() {

       agentIdProof.setModified(currentDatetime);
        agentIdProof.setModifiedBy(CommonConstants.USERID);
        agentIdProof.setIsActive(true);
        agentIdProof.setId(0);
        agentIdProof.setAgentId(null);
        agentIdProof.setCreated(currentDatetime);
        agentIdProof.setCreatedBy(CommonConstants.USERID);
        agentIdProof.setIsActive(true);
        agentIdProof.setIdProofTypeId(25);
        agentIdProof.setIdProofNumber("11254232");
        agentFinancialProof.setModified(currentDatetime);
        agentFinancialProof.setModifiedBy(CommonConstants.USERID);
        agentFinancialProof.setIsActive(true);
        agentFinancialProof.setId(0);
        agentFinancialProof.setAgentId(null);
        agentFinancialProof.setCreated(currentDatetime);
        agentFinancialProof.setCreatedBy(CommonConstants.USERID);
        agentFinancialProof.setIdProofTypeId(27);
        agentFinancialProof.setIdProofNumber("0987654321");
                agentIdProofArrayList.add(agentIdProof);
                agentIdProofArrayList.add(agentFinancialProof);
        addAgent.setAgentIdProofs(agentIdProofArrayList);

    }*/
    private JsonObject addIdProofDetails() {

       agentIdProof.setIdProofNumber(personalIdNumber);
       agentIdProof.setIdProofTypeId(businessListResults.get(spinnerCustom_personalId.getSelectedItemPosition()-1).getId());
        agentFinancialProof.setIdProofTypeId(financialListResults.get(spinnerCustom_finacialId.getSelectedItemPosition()-1).getId());
       agentFinancialProof.setIdProofNumber(financialIdNumber);
        idProofArrayList.add(agentFinancialProof);
        idProofArrayList.add(agentIdProof);
        idProof.setIdProofs((List<IdProofModel>) idProofArrayList);
        idProof.setAgentId(CommonConstants.AGENT_ID);
        idProof.setCreatedBy(CommonConstants.USERID);
        idProof.setModifiedBy(CommonConstants.USERID);
        return new Gson().toJsonTree(idProof)
                .getAsJsonObject();


    }



    private void postIdInfo() {
        showDialog(getActivity(), "Authenticating...");
        JsonObject object = addIdProofDetails();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        operatorSubscription = service.postIdInfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IdProofResponseModel>() {
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
                    public void onNext(IdProofResponseModel idProofResponseModel) {
                        hideDialog();
                        if(idProofResponseModel.getIsSuccess())
                        {
                            showToast(context,idProofResponseModel.getEndUserMessage());
                            replaceFragment(getActivity(), MAIN_CONTAINER, new AggrementDocumentsFragment(), TAG, AggrementDocumentsFragment.TAG);

                        }else {
                            showToast(context,idProofResponseModel.getEndUserMessage());
                        }

                    }
                });

    }


    private void getAgentIdproofInfo(String agentId) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.GetAgentIdproofInfo(ApiConstants.GET_AGENT_IDPROOF + agentId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetIdproofModel>() {
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
                    public void onNext(GetIdproofModel idproofModel) {
                        agentIdproofList = (ArrayList<GetIdproofModel.ListResult>) idproofModel.getListResult();
                        IdproofAdapter idproofAdapter = new IdproofAdapter(context,idproofModel.getListResult()) ;
                        idproofAdapter.setOnAdapterListener(IdProofFragment.this);
                        recylerView.setLayoutManager(new LinearLayoutManager(context));
                        recylerView.setAdapter(idproofAdapter);
                    }
                });
                }





    private boolean isValidateUi() {
        boolean status = true;
        personalIdNumber = numberpersonal.getText().toString().trim();
        financialIdNumber = numberfinancial.getText().toString();
        if (spinnerCustom_personalId.getSelectedItemPosition() == 0) {
            status = false;
            Toast.makeText(context, "Select personal id type", Toast.LENGTH_SHORT).show();
        }else if (personalIdNumber.isEmpty()) {
            status = false;
            numberpersonal.setError("Id number is required");
            numberpersonal.requestFocusFromTouch();
           // Toast.makeText(context, "Id number is required", Toast.LENGTH_SHORT).show();
        }else if (spinnerCustom_finacialId.getSelectedItemPosition() == 0) {
            status = false;
            Toast.makeText(context, "Select financial id type", Toast.LENGTH_SHORT).show();
        }else if (financialIdNumber.isEmpty()) {
            status = false;
            numberfinancial.setError("Id number is required");
            numberfinancial.requestFocusFromTouch();
          //  Toast.makeText(context, "Id number is required", Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    private void getRequestFinacial(String financialidCategoryId) {
        showDialog(getActivity(), "Authenticating...");
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBusinessRequest(ApiConstants.BUSINESS_CAT_REQUESTS + Integer.parseInt(financialidCategoryId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BusinessCategoryModel>() {
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
                    public void onNext(BusinessCategoryModel businessCategoryModel) {
                        hideDialog();
                        Log.d("response", businessCategoryModel.getIsSuccess().toString());
                        financialListResults = (ArrayList<BusinessCategoryModel.ListResult>) businessCategoryModel.getListResult();
                        for (int i = 0; i <businessCategoryModel.getListResult().size() ; i++) {
                            financiaStringArrayList.add(businessCategoryModel.getListResult().get(i).getDescription());
                        }
                        IdProofFragment.CustomSpinnerAdapter customSpinnerAdapter =new IdProofFragment.CustomSpinnerAdapter(getActivity(), financiaStringArrayList);
                        financiaStringArrayList.add(0,"--Select Financial Id Proof--");
                        spinnerCustom_finacialId.setAdapter(customSpinnerAdapter);

                    }

                });
    }


    private void getRequest(String providerType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBusinessRequest(ApiConstants.BUSINESS_CAT_REQUESTS + Integer.parseInt(providerType))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BusinessCategoryModel>() {
                    @Override
                    public void onCompleted() {
                       // Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                        businessListResults = (ArrayList<BusinessCategoryModel.ListResult>) businessCategoryModel.getListResult();
                        businessArrayList.add(0,"--Select Personal Id Proof--");
                        for (int i = 0; i <businessCategoryModel.getListResult().size() ; i++) {
                            businessArrayList.add(businessCategoryModel.getListResult().get(i).getDescription());

                        }

                        IdProofFragment.CustomSpinnerAdapterFinancial customSpinnerAdapterFinancial =new IdProofFragment.CustomSpinnerAdapterFinancial(getActivity(), businessArrayList);

                        spinnerCustom_personalId.setAdapter(customSpinnerAdapterFinancial);
                    }

                });

    }

    private void initCustomSpinner_personalId() {
        spinnerCustom_personalId = (Spinner)view.findViewById(R.id.spinner_personalId);

        spinnerCustom_personalId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

               //  Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initCustomSpinner_financialId() {

         spinnerCustom_finacialId = (Spinner)view.findViewById(R.id.spinner_financialId);

        spinnerCustom_finacialId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

               // Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
          /*  case R.id.btn_personal:
                showToast(getActivity(),"Please Fill The Personal Details");

                // replaceFragment(getActivity(), MAIN_CONTAINER, new RegistrationViewFragment(), TAG, RegistrationViewFragment.TAG);

                break;
            case R.id.btn_bank:
                showToast(getActivity(),"Please Fill The Personal Details");
                //replaceFragment(getActivity(),MAIN_CONTAINER,new AgentRequestsFragment(),TAG,AgentRequestsFragment.TAG);
                break;*/
            case R.id.btn_id:
                showToast(getActivity(),"Please Fill The Identity Proof Details");
                // replaceFragment(getActivity(),MAIN_CONTAINER,new InProgressFragment(),TAG,InProgressFragment.TAG);
                break;
            case R.id.btn_doc:
           //     showToast(getActivity(),"Please Fill The Personal Details");
                if (isValidateUi()) {
                    //  addIdProofDetails();
                    postIdInfo();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                  /*  Bundle bundle = new Bundle();
                    bundle.putParcelable("idproof", addAgent);
                    Fragment fragment = new AggrementDocumentsFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, AggrementDocumentsFragment.TAG);*/
                }

                break;
        }
    }

    @Override
    public void onAdapterClickListiner(int pos) {

    }

    class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
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

    public void ReplcaFragment(android.support.v4.app.Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

     class CustomSpinnerAdapterFinancial extends BaseAdapter implements SpinnerAdapter{
         private final Context activity;
         private ArrayList<String> asr;

         public CustomSpinnerAdapterFinancial(Context context, ArrayList<String> asr) {
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
