package calibrage.payzanagent.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.controls.CommonTextView;
import calibrage.payzanagent.controls.NoChangingBackgroundTextInputLayout;
import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AgentPersonalInfo;
import calibrage.payzanagent.model.AgentRequestModel;
import calibrage.payzanagent.model.BusinessCategoryModel;
import calibrage.payzanagent.model.DistrictModel;
import calibrage.payzanagent.model.GetPersonalInfoModel;
import calibrage.payzanagent.model.MandalModel;
import calibrage.payzanagent.model.PersonalInfoResponseModel;
import calibrage.payzanagent.model.PostUpdatePersonalInfo;
import calibrage.payzanagent.model.ProvinceModel;
import calibrage.payzanagent.model.StatesModel;
import calibrage.payzanagent.model.VillageModel;
import calibrage.payzanagent.networkservice.ApiConstants;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import calibrage.payzanagent.service.GPSTracker;
import calibrage.payzanagent.utils.CommonConstants;
import calibrage.payzanagent.utils.NCBTextInputLayout;
import calibrage.payzanagent.utils.SharedPrefsData;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static calibrage.payzanagent.utils.CommonUtil.MY_PERMISSIONS_REQUEST_LOCATION;


public class RegistrationViewFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener {

    public static final String TAG = RegistrationViewFragment.class.getSimpleName();

    private Button btnContinue;
    View view;
    FragmentManager fragmentManager;
    EditText edtFirstName, edtUserName, edtPassWord, edtMobile, edtEmail, edtAddress1, edtAddress2, edtLandMark, edtPincode, edtMiddleName, edtLastName, edtDOB;
    private AgentRequestModel agentRequestModel;
    private StatesModel statesModel;
    private Context context;
    Spinner spinnerBusinessCat, spinnerState, spinnerTitleType, spinnerProivne, spinnerDistrict, spinnerMandal, spinnerVillage,spinnerGender;
    private Subscription operatorSubscription;
    public static Toolbar toolbar;
    private ArrayList<AgentRequestModel.ListResult> listResults;
    private ArrayList<StatesModel.ListResult> listResultArrayList;
    private ArrayList<AgentRequestModel.ListResult> titleResultArrayList;
    MapView mMapView;
    private GoogleMap googleMap;
    private CommonTextView latlog;
    private GPSTracker gpsTracker;
    private Button personalButton, bankButton, idButton, documentButton,btnCancel;
    private boolean isNewAgent;
    private Double lat = 0.0, log = 0.0;
    LocationManager locationManager;
    String provider,aspNetId;
    int provinceId, districtId, mandalId, villageId;
    public AddAgent addAgent;
    private NCBTextInputLayout agentNameTXT;
    private NoChangingBackgroundTextInputLayout il_passqword;
    Location lastLocation;
    int intAgentRequestId;
    private int mYear, mMonth, mDay;
    private boolean is_exisisting_user = false;

    private String strfirstname, strusername, strpass, strmobile, stremail, straddress1, straddress2, strlandmark, strpin, currentDatetime, strlastname, strmiddlename = " ",strbundleMobile,strCreatedBy;
    private int agentRequestId;
    private AgentPersonalInfo agentPersonalInfo;

    ArrayList<String> businessArrayList = new ArrayList<String>();
    private ArrayList<BusinessCategoryModel.ListResult> businessListResults = new ArrayList<>();

    ArrayList<String> titleArrayList = new ArrayList<String>();
    private ArrayList<BusinessCategoryModel.ListResult> titleListResults = new ArrayList<>();

    ArrayList<String> genderArrayList = new ArrayList<String>();
    private ArrayList<BusinessCategoryModel.ListResult> genderListResults = new ArrayList<>();

    ArrayList<String> statesArrayList = new ArrayList<String>();
    private ArrayList<StatesModel.ListResult> stateListResults = new ArrayList<>();

    ArrayList<String> provinceArrayList = new ArrayList<String>();
    private ArrayList<ProvinceModel.ListResult> provinceListResults = new ArrayList<>();

    ArrayList<String> districtArrayList = new ArrayList<String>();
    private ArrayList<DistrictModel.ListResult> districtListResults = new ArrayList<>();

    ArrayList<String> mandalArrayList = new ArrayList<String>();
    private ArrayList<MandalModel.ListResult> mandalListResults = new ArrayList<>();

    ArrayList<String> villageArrayList = new ArrayList<String>();
    private ArrayList<VillageModel.ListResult> villageListResults = new ArrayList<>();

    ArrayList<String> updateArrayList = new ArrayList<String>();
   // private ArrayList<GetPersonalInfoModel.RE> updateListResults = new ArrayList<>();
    private GetPersonalInfoModel.Result updateListResults;

    RegistrationViewFragment.CustomSpinnerAdapter customSpinnerAdapter;
    private Subscription mRegisterSubscription;

    //String id;
    private String parentAspNetId;
    private String createdBy;
    private int id;
    private String dateOfBirth;
    private boolean isFirstTime = false;

    public RegistrationViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration_view, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        context = this.getActivity();
        initMap(savedInstanceState);
//        HomeActivity.toolbar.setTitle(getResources().getString(R.string.register_sname));
//        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        checkLocationPermission(context);

        /*initStateSpinner();
        getRequestState(CommonConstants.STATES_ID);*/
        initBusinessCatSpinner();
        initTitleSpinner();
        initGenderSpinner();
        initProvinceSpinner();
        initDistrictSpinner();
        initMandalSpinner();
        initVillageSpinner();
        //id = "9f6027ef-af05-43a5-b812-cfdd42a5fb4e";
        //id = "9989147171";


        if (isOnline(getActivity())) {
          //  getRequestPersonalInfo(CommonConstants.USER_NAME);
          //  getRequestPersonalInfo(id);
            getRequest(CommonConstants.BUSINESS_CATEGORY_ID);
            getRequestTitle(CommonConstants.TITLE_ID);
            getRequestProvince(CommonConstants.PROVINCE_NAME);
            getGenderType(CommonConstants.GENDER_ID);

        } else {
            showToast(getActivity(), getString(R.string.no_internet));
        }

        listResults = new ArrayList();
        listResultArrayList = new ArrayList();
        updateArrayList = new ArrayList();
        titleResultArrayList = new ArrayList();
        provinceArrayList = new ArrayList();
        addAgent = new AddAgent();
        agentPersonalInfo = new AgentPersonalInfo();
        currentDatetime = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("datetime");
        // Log.d(TAG, "dateanddtime"+currentDatetime);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        edtFirstName = (EditText) view.findViewById(R.id.edt_firstName);
        edtMiddleName = (EditText) view.findViewById(R.id.edt_middleName);
        edtLastName = (EditText) view.findViewById(R.id.edt_lastName);
        // edtAgencyName = (EditText) view.findViewById(R.id.edt_Agencyname);
        // edtIdNo = (EditText) view.findViewById(R.id.edt_idno);
        edtUserName = (EditText) view.findViewById(R.id.edt_userName);
        edtPassWord = (EditText) view.findViewById(R.id.edt_password);
        edtMobile = (EditText) view.findViewById(R.id.edt_mobileno);
        edtEmail = (EditText) view.findViewById(R.id.edt_email);
        edtAddress1 = (EditText) view.findViewById(R.id.edt_address1);
        edtAddress2 = (EditText) view.findViewById(R.id.edt_address2);
        edtLandMark = (EditText) view.findViewById(R.id.edt_land_mark);
        edtPincode = (EditText) view.findViewById(R.id.edt_pincode);
        edtDOB = (EditText) view.findViewById(R.id.edt_DOB);
        btnCancel = (Button)view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        personalButton = (Button) view.findViewById(R.id.btn_personal);
        bankButton = (Button) view.findViewById(R.id.btn_bank);
        idButton = (Button) view.findViewById(R.id.btn_id);
        il_passqword = (NoChangingBackgroundTextInputLayout)view.findViewById(R.id.il_passqword);
        documentButton = (Button) view.findViewById(R.id.btn_doc);
      //  edtPassWord.setVisibility(View.VISIBLE);

        il_passqword.setVisibility(View.VISIBLE);

        edtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();

            }
        });


        personalButton.setOnClickListener(this);
        bankButton.setOnClickListener(this);
       /* idButton.setOnClickListener(this);
        documentButton.setOnClickListener(this);
*/


        //edtProvince = (EditText) view.findViewById(R.id.edt_provinceName);

        // edtState = (EditText) view.findViewById(R.id.edt_state);

        latlog = (CommonTextView) view.findViewById(R.id.latlog);
        latlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsTracker = new GPSTracker(context);
                if (gpsTracker.canGetLocation()) {
                    lat = gpsTracker.getLatitude();
                    log = gpsTracker.getLongitude();
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (lat == 0.0 && log == 0.0 && lastLocation != null) {
                        lat = lastLocation.getLatitude();
                        log = lastLocation.getLongitude();
                    }
                } else {
                    gpsTracker.showSettingsAlert();
                }
                LatLng sydney = new LatLng(lat, log);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

            }
        });


        fragmentManager = getActivity().getSupportFragmentManager();
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //         replaceFragment(getActivity(), MAIN_CONTAINER, new BankDetailFragment(), TAG, BankDetailFragment.TAG);
                if(isOnline(getActivity())){
                    if (isValidateUi()) {
                        if(is_exisisting_user){
                            updatePersonalInfo();
                        }else {
                            postPersonalInfo();
                        }

                        // addAgentPersonalInfo();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("personalinfo", addAgent);
//                    Fragment fragment = new BankDetailFragment();
//                    fragment.setArguments(bundle);
                        //  replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, BankDetailFragment.TAG);

                    }
                }else {
                    showToast(getActivity(), getString(R.string.no_internet));
                }


                // startActivity(new Intent(RegistrationView.this, BankDetailsActivity.class));
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            isNewAgent = false;
            agentRequestModel = bundle.getParcelable("request");
            int pos = bundle.getInt("position");
            intAgentRequestId = agentRequestModel.getListResult().get(pos).getId();
            String name;
            //agentRequestModel.getListResult().get(pos).getTitleType() + " " +
            if (agentRequestModel.getListResult().get(pos).getMiddleName() != null) {

                edtMiddleName.setText(agentRequestModel.getListResult().get(pos).getMiddleName());

               // name =  + " " +  + " " + ;
            } else {
               // name = agentRequestModel.getListResult().get(pos).getFirstName() + " " + agentRequestModel.getListResult().get(pos).getLastName();

                edtMiddleName.setText("");
            }
            // edtIdNo.setText(String.valueOf(agentRequestModel.getListResult().get(pos).getId()));
            edtFirstName.setText(agentRequestModel.getListResult().get(pos).getFirstName());
            edtLastName.setText(agentRequestModel.getListResult().get(pos).getLastName());
            edtMobile.setText(agentRequestModel.getListResult().get(pos).getMobileNumber());
            strbundleMobile = agentRequestModel.getListResult().get(pos).getMobileNumber();
            edtEmail.setText(agentRequestModel.getListResult().get(pos).getEmail());
            edtAddress1.setText(agentRequestModel.getListResult().get(pos).getAddressLine1());
            edtAddress2.setText(agentRequestModel.getListResult().get(pos).getAddressLine2());
            //edtState.setText(agentRequestModel.getListResult().get(pos).getDistrictName());


        } else {
            isNewAgent = true;
        }


        return view;
    }

    private void updatePersonalInfo() {
        showDialog(getActivity(), "Authenticating...");
        JsonObject object = updatePersonalInfoObject();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        mRegisterSubscription = service.updatePersonalInfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PersonalInfoResponseModel>() {
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
                        Toast.makeText(getActivity(), "failupdate", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(PersonalInfoResponseModel personalInfoResponseModel) {
                        hideDialog();
                        if (personalInfoResponseModel.getIsSuccess()) {
                            showToast(context, personalInfoResponseModel.getEndUserMessage());
                            CommonConstants.AGENT_ID = personalInfoResponseModel.getResult().getAspNetUserId();
                            replaceFragment(getActivity(), MAIN_CONTAINER, new BankDetailFragment(), TAG, BankDetailFragment.TAG);

                        } else {
                            showToast(context, personalInfoResponseModel.getEndUserMessage());
                        }

                    }
                });

    }

    private void getRequestPersonalInfo(String providerType) {
        showDialog(getActivity(), "Authenticating...");
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getUpdatePersonalInfoRequest(ApiConstants.PERSONAL_INFO_REQUESTS +providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetPersonalInfoModel>() {
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
                        Toast.makeText(context, "failpersoanalinfo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(GetPersonalInfoModel getPersonalInfoModel) {
                        hideDialog();
                        Log.d("response", getPersonalInfoModel.getIsSuccess().toString());
                        updateListResults = getPersonalInfoModel.getResult();
                        if(updateListResults!=null){
                            is_exisisting_user = true;
                            spinnerTitleType.setSelection(titleArrayList.indexOf(getPersonalInfoModel.getResult().getTitleTypeName()));
                            edtFirstName.setText(getPersonalInfoModel.getResult().getFirstName());
                            if(getPersonalInfoModel.getResult().getMiddleName() == null){
                                edtMiddleName.setText(" ");
                            }else {
                                edtMiddleName.setText(getPersonalInfoModel.getResult().getMiddleName());
                            }
                            edtLastName.setText(getPersonalInfoModel.getResult().getLastName());
                            spinnerBusinessCat.setSelection(businessArrayList.indexOf(getPersonalInfoModel.getResult().getBusinessCategoryName()));
                            edtMobile.setText(getPersonalInfoModel.getResult().getPhone());
                          //  edtPassWord.setVisibility(View.GONE);
                            il_passqword.setVisibility(View.GONE);
                           // edtPassWord.setText(getPersonalInfoModel.getResult().getPhone());
                            spinnerGender.setSelection(genderArrayList.indexOf(getPersonalInfoModel.getResult().getGenderType()));
                            edtEmail.setText(getPersonalInfoModel.getResult().getEmail());

                            dateOfBirth = getPersonalInfoModel.getResult().getDOB();
                            edtDOB.setText(formatDateTimeUi());
                            edtAddress1.setText(getPersonalInfoModel.getResult().getAddress1());
                            edtAddress2.setText(getPersonalInfoModel.getResult().getAddress2());
                            edtLandMark.setText(getPersonalInfoModel.getResult().getLandmark());
                            spinnerProivne.setSelection(provinceArrayList.indexOf(getPersonalInfoModel.getResult().getProvinceName()));
                            spinnerDistrict.setSelection(districtArrayList.indexOf(getPersonalInfoModel.getResult().getDistrictName()));
                            spinnerMandal.setSelection(mandalArrayList.indexOf(getPersonalInfoModel.getResult().getMandalName()));
                            spinnerVillage.setSelection(villageArrayList.indexOf(getPersonalInfoModel.getResult().getVillageName()));
                            edtPincode.setText(""+getPersonalInfoModel.getResult().getPostCode());
                            strCreatedBy = getPersonalInfoModel.getResult().getCreatedBy();
                            agentRequestId = getPersonalInfoModel.getResult().getAgentRequestId();
                            if(getPersonalInfoModel.getResult().getAspNetUserId()!=null){
                                aspNetId = getPersonalInfoModel.getResult().getAspNetUserId();
                            }
//                            if(getPersonalInfoModel.getResult().getParentAspNetUserId()!=null){
//                                parentAspNetId = getPersonalInfoModel.getResult().getParentAspNetUserId();
//                            }
                            createdBy = getPersonalInfoModel.getResult().getCreated().toString();
                            CommonConstants.AGENT_ID = getPersonalInfoModel.getResult().getAspNetUserId();
                            id = getPersonalInfoModel.getResult().getId();
                            btnContinue.setText("Update");
                        }
                        else {
                            is_exisisting_user = false;
                            btnContinue.setText("Continue");
                        }


                    }

                });
    }

    private void initGenderSpinner() {

        spinnerGender = (Spinner) view.findViewById(R.id.spinner_gender_cat);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                //villageId = (int) parent.getSelectedItemId();

                // getRequestBranch(String.valueOf(bankId));
                //    Toast.makeText(parent.getContext(), "bankkkkkkk" +bankId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initVillageSpinner() {
        spinnerVillage = (Spinner) view.findViewById(R.id.spinner_village);
        spinnerVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                //villageId = (int) parent.getSelectedItemId();
                if (!villageListResults.isEmpty()) {
                    edtPincode.setText(String.valueOf(villageListResults.get(position).getPostCode()));
                }
                // getRequestBranch(String.valueOf(bankId));
                //    Toast.makeText(parent.getContext(), "bankkkkkkk" +bankId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initMandalSpinner() {
        spinnerMandal = (Spinner) view.findViewById(R.id.spinner_mandal);
        spinnerMandal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                //  mandalId = (int) parent.getSelectedItemId();

                //    Toast.makeText(parent.getContext(), "bankkkkkkk" +bankId, Toast.LENGTH_LONG).show();
                if (!mandalListResults.isEmpty()) {
                    mandalId = mandalListResults.get((int) parent.getSelectedItemId()).getId();
                    if (isOnline(getActivity())) {
                        getRequestVillage(String.valueOf(mandalId));
                    }else {
                        showToast(getActivity(),getString(R.string.no_internet));
                    }


                } else {
                    Toast.makeText(parent.getContext(), "Input Not Valid", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRequestVillage(String providerType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getVillageRequest(ApiConstants.VILLAGE_REQUESTS + providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VillageModel>() {
                    @Override
                    public void onCompleted() {
                        //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "failrequestvillage", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(VillageModel villageModel) {
                        hideDialog();
                        Log.d("response", villageModel.getIsSuccess().toString());
                        villageListResults = (ArrayList<VillageModel.ListResult>) villageModel.getListResult();
                        villageArrayList = new ArrayList();
                        if (villageModel.getListResult().size() > 0) {

                            for (int i = 0; i < villageModel.getListResult().size(); i++) {
                                villageArrayList.add(villageModel.getListResult().get(i).getName());

                            }
                        } else {
                            villageArrayList.clear();
                            edtPincode.setText(" ");
                            customSpinnerAdapter.notifyDataSetChanged();
                        }

                        customSpinnerAdapter = new RegistrationViewFragment.CustomSpinnerAdapter(getActivity(), villageArrayList);
                        spinnerVillage.setAdapter(customSpinnerAdapter);
                        if (CommonConstants.Is_New_Agent_Request){

                        }else {
                            if(!isFirstTime){
                                getRequestPersonalInfo(strbundleMobile);
                                isFirstTime =true;
                            }

                        }

                    }

                });
    }

    private void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edtDOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        formatDateTime();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    private void initDistrictSpinner() {
        spinnerDistrict = (Spinner) view.findViewById(R.id.spinner_district);
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                //  districtId = (int) parent.getSelectedItemId();

                //    Toast.makeText(parent.getContext(), "bankkkkkkk" +bankId, Toast.LENGTH_LONG).show();
                if (!districtListResults.isEmpty()) {
                    districtId = districtListResults.get((int) parent.getSelectedItemId()).getId();
                    if (isOnline(getActivity())){
                        getRequestMandal(String.valueOf(districtId));
                    }else {
                        showToast(getActivity(),getString(R.string.no_internet));
                    }

                } else {
                    Toast.makeText(parent.getContext(), "Input Not Valid", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRequestMandal(String providerType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getMandalRequest(ApiConstants.MANDAL_REQUESTS + providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MandalModel>() {
                    @Override
                    public void onCompleted() {
                        //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "failrequestmandal", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MandalModel mandalModel) {
                        hideDialog();
                        Log.d("response", mandalModel.getIsSuccess().toString());
                        mandalListResults = (ArrayList<MandalModel.ListResult>) mandalModel.getListResult();

                        mandalArrayList = new ArrayList();
                        if (mandalModel.getListResult().size() > 0) {

                            for (int i = 0; i < mandalModel.getListResult().size(); i++) {
                                mandalArrayList.add(mandalModel.getListResult().get(i).getName());

                            }
                        } else {
                            mandalArrayList.clear();
                            villageArrayList.clear();
                            edtPincode.setText(" ");
                            customSpinnerAdapter.notifyDataSetChanged();
                        }


                        customSpinnerAdapter = new RegistrationViewFragment.CustomSpinnerAdapter(getActivity(), mandalArrayList);
                        spinnerMandal.setAdapter(customSpinnerAdapter);
                    }

                });
    }


    private void getRequestProvince(String providerType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getProvinceRequest(ApiConstants.PROVINCE_REQUESTS + providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProvinceModel>() {
                    @Override
                    public void onCompleted() {
                        //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "failprovince", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ProvinceModel provinceModel) {
                        hideDialog();
                        Log.d("response", provinceModel.getIsSuccess().toString());
                        provinceListResults = (ArrayList<ProvinceModel.ListResult>) provinceModel.getListResult();
                        //  provinceArrayList.add(0,"--Select Proviance--");
                        for (int i = 0; i < provinceModel.getListResult().size(); i++) {
                            provinceArrayList.add(provinceModel.getListResult().get(i).getName());

                        }

                        customSpinnerAdapter = new RegistrationViewFragment.CustomSpinnerAdapter(getActivity(), provinceArrayList);
                        spinnerProivne.setAdapter(customSpinnerAdapter);
                    }

                });

    }

    private void initProvinceSpinner() {

        spinnerProivne = (Spinner) view.findViewById(R.id.spinner_province);
        spinnerProivne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                if (!provinceListResults.isEmpty()) {
                    provinceId = provinceListResults.get((int) (parent.getSelectedItemId())).getId();
                    if (isOnline(getActivity())){
                        getRequestDistrict(String.valueOf(provinceId));
                    }else {
                        showToast(getActivity(),getString(R.string.no_internet));
                    }

                } else {
                    Toast.makeText(parent.getContext(), "Input Not Valid", Toast.LENGTH_LONG).show();
                }

                //    Toast.makeText(parent.getContext(), "bankkkkkkk" +bankId, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getRequestDistrict(String providerType) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getDistrictRequest(ApiConstants.DISTRICT_REQUESTS + providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DistrictModel>() {
                    @Override
                    public void onCompleted() {
                        //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "faildistrict", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DistrictModel districtModel) {
                        hideDialog();
                        Log.d("response", districtModel.getIsSuccess().toString());
                        districtListResults = (ArrayList<DistrictModel.ListResult>) districtModel.getListResult();
                        //   bankArrayList.add(0,"--Select Bank--");
                        districtArrayList = new ArrayList();
                        if (districtModel.getListResult().size() > 0) {
                            for (int i = 0; i < districtModel.getListResult().size(); i++) {
                                districtArrayList.add(districtModel.getListResult().get(i).getName());

                            }
                        } else {
                            districtArrayList.clear();
                            mandalArrayList.clear();
                            villageArrayList.clear();
                            edtPincode.setText(" ");
                            customSpinnerAdapter.notifyDataSetChanged();
                        }


                        customSpinnerAdapter = new RegistrationViewFragment.CustomSpinnerAdapter(getActivity(), districtArrayList);
                        spinnerDistrict.setAdapter(customSpinnerAdapter);
                    }

                });
    }

    private void getRequestTitle(String titleType) {
        showDialog(getActivity(), "Authenticating...");
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBusinessRequest(ApiConstants.BUSINESS_CAT_REQUESTS + Integer.parseInt(titleType))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BusinessCategoryModel>() {
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
                        Toast.makeText(context, "failtitle", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BusinessCategoryModel businessCategoryModel) {
                        hideDialog();
                        Log.d("response", businessCategoryModel.getIsSuccess().toString());
                        titleListResults = (ArrayList<BusinessCategoryModel.ListResult>) businessCategoryModel.getListResult();
                        titleArrayList.add(0, "--Select Title--");
                        for (int i = 0; i < businessCategoryModel.getListResult().size(); i++) {
                            titleArrayList.add(businessCategoryModel.getListResult().get(i).getDescription());
                        }
                        RegistrationViewFragment.CustomSpinnerAdapter customSpinnerAdapter = new RegistrationViewFragment.CustomSpinnerAdapter(getActivity(), titleArrayList);

                        spinnerTitleType.setAdapter(customSpinnerAdapter);
                    }

                });
    }
    private void getGenderType(String titleType) {
        showDialog(getActivity(), "Authenticating...");
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBusinessRequest(ApiConstants.BUSINESS_CAT_REQUESTS + Integer.parseInt(titleType))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BusinessCategoryModel>() {
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
                        Toast.makeText(context, "failgendertype", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BusinessCategoryModel businessCategoryModel) {
                        hideDialog();
                        Log.d("response", businessCategoryModel.getIsSuccess().toString());
                        genderListResults = (ArrayList<BusinessCategoryModel.ListResult>) businessCategoryModel.getListResult();
                        genderArrayList.add(0, "--Select Gender--");
                        for (int i = 0; i < businessCategoryModel.getListResult().size(); i++) {
                            genderArrayList.add(businessCategoryModel.getListResult().get(i).getDescription());
                        }
                        RegistrationViewFragment.CustomSpinnerAdapter customSpinnerAdapter = new RegistrationViewFragment.CustomSpinnerAdapter(getActivity(), genderArrayList);
                        spinnerGender.setAdapter(customSpinnerAdapter);


                    }

                });
    }

    private void initTitleSpinner() {
        spinnerTitleType = (Spinner) view.findViewById(R.id.spinner_title_type);
        // Spinner Drop down elements


        spinnerTitleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

   /* private void getRequestState(String providerType) {

        showDialog(getActivity(), "Authenticating...");
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getStates(ApiConstants.STATE_REQUESTS + providerType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StatesModel>() {
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
                    public void onNext(StatesModel statesModel) {
                        hideDialog();
                    //    Log.d("response", statesModel.getIsSuccess().toString());
                        stateListResults = (ArrayList<StatesModel.ListResult>) statesModel.getListResult();
                        for (int i = 0; i < statesModel.getListResult().size(); i++) {
                            statesArrayList.add(statesModel.getListResult().get(i).getName());
                        }
                        RegistrationViewFragment.CustomSpinnerAdapter customSpinnerAdapter = new RegistrationViewFragment.CustomSpinnerAdapter(getActivity(), statesArrayList);
                        statesArrayList.add(0,"--Select State--");
                        spinnerState.setAdapter(customSpinnerAdapter);
                    }

                });


    }*/

  /*  private void initStateSpinner() {
        spinnerState = (Spinner) view.findViewById(R.id.spinner_state);
        // Spinner Drop down elements


        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                // Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/


    private JsonObject addAgentPersonalInfo() {
//        addAgent.setEmail();
//        addAgent.setMobileNumber();
        //addAgent.setPassword(strpass);
        addAgent.setUserName(strusername);
        agentPersonalInfo.setFirstName(strfirstname);
        agentPersonalInfo.setPhone(strmobile);
        agentPersonalInfo.setAgentBusinessCategoryId(businessListResults.get(spinnerBusinessCat.getSelectedItemPosition() - 1).getId());
        agentPersonalInfo.setEmail(stremail);
        agentPersonalInfo.setAddress1(straddress1);
        agentPersonalInfo.setAddress2(straddress2);
        agentPersonalInfo.setPhone(strmobile);
        if (isNewAgent) {
            agentPersonalInfo.setAgentRequestId(null);
            Log.d(TAG, "addAgentPersonalInfo: " + "  printing nulllll");
        } else {
            agentPersonalInfo.setAgentRequestId(intAgentRequestId);
            Log.d(TAG, "addAgentPersonalInfo: " + intAgentRequestId);
        }
        agentPersonalInfo.setLandmark(strlandmark);
       // agentPersonalInfo.setIsActive(true);
        agentPersonalInfo.setParentAspNetUserId("");
        agentPersonalInfo.setPassword(strpass);
        agentPersonalInfo.setTitleTypeId(titleListResults.get(spinnerTitleType.getSelectedItemPosition() - 1).getId());
        agentPersonalInfo.setGenderTypeId(genderListResults.get(spinnerGender.getSelectedItemPosition()-1).getId());
//        if (titleListResults.get(spinnerTitleType.getSelectedItemPosition() - 1).getId() == 17) {
//            agentPersonalInfo.setGenderTypeId(Integer.parseInt(CommonConstants.GENDER_TYPE_MALE));
//        } else {
//
//        }
        agentPersonalInfo.setVillageId(villageListResults.get(spinnerVillage.getSelectedItemPosition()).getId());
        agentPersonalInfo.setParentAspNetUserId(null);
       // agentPersonalInfo.setId(0);
        agentPersonalInfo.setMiddleName(strmiddlename);
        agentPersonalInfo.setLastName(strlastname);
        agentPersonalInfo.setDOB(formatDateTime());
//        agentPersonalInfo.setIsActive(true);
//        agentPersonalInfo.setCreated(currentDatetime);
      //  agentPersonalInfo.setCreatedBy(CommonConstants.USERID);
        //agentPersonalInfo.setModified(currentDatetime);
      //  agentPersonalInfo.setModifiedBy(CommonConstants.USERID);
        agentPersonalInfo.setFirstName(strfirstname);

        //  addAgent.setAgentPersonalInfo(agentPersonalInfo);
        Log.d(TAG, "addAgentPersonalInfo: " + addAgent.toString());
        return new Gson().toJsonTree(agentPersonalInfo)
                .getAsJsonObject();

    }
    private JsonObject updatePersonalInfoObject() {
        PostUpdatePersonalInfo postUpdatePersonalInfo = new PostUpdatePersonalInfo();
        postUpdatePersonalInfo.setFirstName(strfirstname);
        postUpdatePersonalInfo.setPhone(strmobile);
        postUpdatePersonalInfo.setAgentBusinessCategoryId(businessListResults.get(spinnerBusinessCat.getSelectedItemPosition() - 1).getId());
        postUpdatePersonalInfo.setEmail(stremail);
        postUpdatePersonalInfo.setAddress1(straddress1);
        postUpdatePersonalInfo.setAddress2(straddress2);
        postUpdatePersonalInfo.setPhone(strmobile);
        postUpdatePersonalInfo.setAgentRequestId(agentRequestId);
//        if (isNewAgent) {
//
//            Log.d(TAG, "addAgentPersonalInfo: " + "  printing nulllll");
//        } else {
//            agentPersonalInfo.setAgentRequestId(intAgentRequestId);
//            Log.d(TAG, "addAgentPersonalInfo: " + intAgentRequestId);
//        }
        postUpdatePersonalInfo.setLandmark(strlandmark);
       // agentPersonalInfo.setIsActive(true);
        postUpdatePersonalInfo.setParentAspNetUserId(parentAspNetId);
        postUpdatePersonalInfo.setAspNetUserId(aspNetId);
        postUpdatePersonalInfo.setId(id);

        postUpdatePersonalInfo.setTitleTypeId(titleListResults.get(spinnerTitleType.getSelectedItemPosition() - 1).getId());
        postUpdatePersonalInfo.setGenderTypeId(genderListResults.get(spinnerGender.getSelectedItemPosition()-1).getId());
//        if (titleListResults.get(spinnerTitleType.getSelectedItemPosition() - 1).getId() == 17) {
//            agentPersonalInfo.setGenderTypeId(Integer.parseInt(CommonConstants.GENDER_TYPE_MALE));
//        } else {
//
//        }
        postUpdatePersonalInfo.setVillageId(villageListResults.get(spinnerVillage.getSelectedItemPosition()).getId());
        postUpdatePersonalInfo.setParentAspNetUserId(null);
       // agentPersonalInfo.setId(0);
        postUpdatePersonalInfo.setMiddleName(strmiddlename);
        postUpdatePersonalInfo.setLastName(strlastname);
        postUpdatePersonalInfo.setDOB(currentDatetime);
        postUpdatePersonalInfo.setIsActive(true);
//        postUpdatePersonalInfo.setCreated(createdBy);
//        postUpdatePersonalInfo.setCreatedBy(strCreatedBy);
//        postUpdatePersonalInfo.setModified(currentDatetime);
//        postUpdatePersonalInfo.setModifiedBy(CommonConstants.USERID);
        postUpdatePersonalInfo.setFirstName(strfirstname);
        postUpdatePersonalInfo.setEducationTypeId(null);


        //  addAgent.setAgentPersonalInfo(agentPersonalInfo);
        //Log.d(TAG, "addAgentPersonalInfo: " + addAgent.toString());
        return new Gson().toJsonTree(postUpdatePersonalInfo)
                .getAsJsonObject();

    }

    private void postPersonalInfo() {
        showDialog(getActivity(), "Authenticating...");
        JsonObject object = addAgentPersonalInfo();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        mRegisterSubscription = service.postPersonalInfo(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PersonalInfoResponseModel>() {
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
                        Toast.makeText(getActivity(), "failpost", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(PersonalInfoResponseModel personalInfoResponseModel) {
                        hideDialog();
                        if (personalInfoResponseModel.getIsSuccess()) {
                            showToast(context, personalInfoResponseModel.getEndUserMessage());
                            CommonConstants.AGENT_REQUEST_ID = String.valueOf(personalInfoResponseModel.getResult().getAgentRequestId());
                            CommonConstants.AGENT_ID = personalInfoResponseModel.getResult().getAgentId();
                            replaceFragment(getActivity(), MAIN_CONTAINER, new BankDetailFragment(), TAG, BankDetailFragment.TAG);

                        } else {
                            showToast(context, personalInfoResponseModel.getEndUserMessage());
                        }

                    }
                });

    }

    /* private void addAgentPersonalInfo() {
 //        addAgent.setEmail();
 //        addAgent.setMobileNumber();
         addAgent.setPassword("Amith123@");
         addAgent.setUserName("8222090799");
         agentPersonalInfo.setFirstName("amith");
         agentPersonalInfo.setPhone("8222090799");
         agentPersonalInfo.setAgentBusinessCategoryId(14);
         agentPersonalInfo.setEmail("amith123456787@gmail.com");
         agentPersonalInfo.setAddress1("amith");
         agentPersonalInfo.setAddress2("amith");
         agentPersonalInfo.setEmail("amith");
         agentPersonalInfo.setPhone("8121098723");
         if (isNewAgent){
             agentPersonalInfo.setAgentRequestId(null);
             Log.d(TAG, "addAgentPersonalInfo: "+"  printing nulllll");
         }else {
             agentPersonalInfo.setAgentRequestId(10);
             Log.d(TAG, "addAgentPersonalInfo: "+intAgentRequestId);
         }
         agentPersonalInfo.setLandmark("amith");
         agentPersonalInfo.setIsActive(true);
         agentPersonalInfo.setAspNetUserId("test");
         agentPersonalInfo.setTitleTypeId(17);
         agentPersonalInfo.setGenderTypeId(20);
         agentPersonalInfo.setVillageId(6);
         agentPersonalInfo.setParentAspNetUserId(null);
         agentPersonalInfo.setId(0);
         agentPersonalInfo.setMiddleName(" ");
         agentPersonalInfo.setLastName(" ");
         agentPersonalInfo.setDOB("2017-10-30T17:15:42.569Z");
         agentPersonalInfo.setIsActive(true);
         agentPersonalInfo.setCreated(currentDatetime);
         agentPersonalInfo.setCreatedBy(CommonConstants.USERID);
         agentPersonalInfo.setModified(currentDatetime);
         agentPersonalInfo.setModifiedBy(CommonConstants.USERID);
         agentPersonalInfo.setFirstName(stragentname);

         addAgent.setAgentPersonalInfo(agentPersonalInfo);
         Log.d(TAG, "addAgentPersonalInfo: "+addAgent.toString());

     }
 */
    private boolean isValidateUi() {
        boolean status = true;
        strfirstname = edtFirstName.getText().toString().trim();
        strmiddlename = edtMiddleName.getText().toString().trim();
        strlastname = edtLastName.getText().toString().trim();
      //  strusername = edtUserName.getText().toString();
        strpass = edtPassWord.getText().toString();
        strmobile = edtMobile.getText().toString();
        stremail = edtEmail.getText().toString();
        straddress1 = edtAddress1.getText().toString();
        straddress2 = edtAddress2.getText().toString();
        strlandmark = edtLandMark.getText().toString();
        strpin = edtPincode.getText().toString();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        /* stragencyname = edtAgencyName.getText().toString();
        strid=edtIdNo.getText().toString();*/
/*  if (spinnerBusinessCat.getSelectedItemPosition() == 0) {
            status = false;
             Toast.makeText(context, "select title", Toast.LENGTH_SHORT).show();
        } else
                            */
        if (spinnerTitleType.getSelectedItemPosition() == 0) {
            status = false;
            Toast.makeText(context, "Select Title Type", Toast.LENGTH_SHORT).show();
        } else if (strfirstname.isEmpty() || edtFirstName.getText().length() < 3) {
            status = false;
            edtFirstName.setError("FirstName is required");
            edtFirstName.requestFocusFromTouch();
            //   Toast.makeText(context, "AgentName is required", Toast.LENGTH_SHORT).show();
        } else if (strlastname.isEmpty()) {
            status = false;
            edtLastName.setError("LastName is required");
            edtLastName.requestFocusFromTouch();
            //   Toast.makeText(context, "AgentName is required", Toast.LENGTH_SHORT).show();
        } else if (spinnerBusinessCat.getSelectedItemPosition() == 0) {
            status = false;
            Toast.makeText(context, "Select business category", Toast.LENGTH_SHORT).show();
        } /*else if (strusername.isEmpty() || edtUserName.getText().length() < 4) {
            status = false;
            edtUserName.setError("UserName is required");
            edtUserName.requestFocusFromTouch();
            //Toast.makeText(context, "UserName is required", Toast.LENGTH_SHORT).show();
        } */else if ((strpass.isEmpty() || edtPassWord.getText().length() < 4)&& !is_exisisting_user) {
            status = false;
            edtPassWord.setError("Password is required");
            edtPassWord.requestFocusFromTouch();
            // Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show();
        } else if (strmobile.isEmpty() || edtMobile.getText().length() < 10) {
            status = false;
            edtMobile.setError("MobileNumber is required");
            edtMobile.requestFocusFromTouch();
            //Toast.makeText(context, "MobileNumber is required", Toast.LENGTH_SHORT).show();
        } else if (stremail.isEmpty() || !emailPattern.matcher(stremail).matches()) {
            status = false;
            edtEmail.setError("Valid Email is required");
            edtEmail.requestFocusFromTouch();
            //Toast.makeText(context, "Email is required", Toast.LENGTH_SHORT).show();
        } else if (straddress1.isEmpty() || edtAddress1.getText().length() < 3) {
            status = false;
            edtAddress1.setError("Address is required");
            edtAddress1.requestFocusFromTouch();
            //Toast.makeText(context, "Province is required", Toast.LENGTH_SHORT).show();
        } else if (straddress2.isEmpty() || edtAddress2.getText().length() < 3) {
            status = false;
            edtAddress2.setError("Address is required");
            edtAddress2.requestFocusFromTouch();
            //  Toast.makeText(context, "Address is required", Toast.LENGTH_SHORT).show();
        } else if (strlandmark.isEmpty() || edtLandMark.getText().length() < 3) {
            status = false;
            edtLandMark.setError("Landmark is required");
            edtLandMark.requestFocusFromTouch();
            //  Toast.makeText(context, "Address is required", Toast.LENGTH_SHORT).show();
        }else if (provinceListResults.isEmpty()){
            status = false;
            Toast.makeText(context, "Provinance is required", Toast.LENGTH_SHORT).show();
        }
        else if (districtListResults.isEmpty()){
            status = false;
            Toast.makeText(context, "District is required", Toast.LENGTH_SHORT).show();
        } else if (mandalListResults.isEmpty()){
            status = false;
            Toast.makeText(context, "Mandal is required", Toast.LENGTH_SHORT).show();
        }
        else if (villageListResults.isEmpty()){
            status = false;
            Toast.makeText(context, "Village is required", Toast.LENGTH_SHORT).show();
        } else if (strpin.isEmpty()) {
            status = false;
            edtPincode.setError("Postalcode is required");
            edtPincode.requestFocusFromTouch();
            // Toast.makeText(context, "Pincode is required", Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    public boolean checkLocationPermission(final Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(context)
                        .setTitle("location")
                        .setMessage("location")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) RegistrationViewFragment.this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void initMap(Bundle savedInstanceState) {

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(lat, log);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }

    private void getRequest(String providerType) {
        showDialog(getActivity(), "Authenticating...");
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getBusinessRequest(ApiConstants.BUSINESS_CAT_REQUESTS + Integer.parseInt(providerType))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BusinessCategoryModel>() {
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
                        Toast.makeText(context, "failbusinesscategory", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BusinessCategoryModel businessCategoryModel) {
                        hideDialog();
                        Log.d("response", businessCategoryModel.getIsSuccess().toString());
                        businessListResults = (ArrayList<BusinessCategoryModel.ListResult>) businessCategoryModel.getListResult();
                        for (int i = 0; i < businessCategoryModel.getListResult().size(); i++) {
                            businessArrayList.add(businessCategoryModel.getListResult().get(i).getDescription());
                        }
                        RegistrationViewFragment.CustomSpinnerAdapter customSpinnerAdapter = new RegistrationViewFragment.CustomSpinnerAdapter(getActivity(), businessArrayList);
                        businessArrayList.add(0, "--Select Business Category--");
                        spinnerBusinessCat.setAdapter(customSpinnerAdapter);
                    }

                });


    }

    private void initBusinessCatSpinner() {

        spinnerBusinessCat = (Spinner) view.findViewById(R.id.spinner_business_cat);
        // Spinner Drop down elements


        spinnerBusinessCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_personal:
                showToast(getActivity(), "Please Fill The Personal Details");

                // replaceFragment(getActivity(), MAIN_CONTAINER, new RegistrationViewFragment(), TAG, RegistrationViewFragment.TAG);

                break;
            case R.id.btn_bank:
                if (is_exisisting_user) {
                    //      addAgentPersonalInfo();
                    replaceFragment(getActivity(), MAIN_CONTAINER, new BankDetailFragment(), TAG, BankDetailFragment.TAG);

                }else{
                    if(isValidateUi()){
                        postPersonalInfo();
                        // addAgentPersonalInfo();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("personalinfo", addAgent);
//                    Fragment fragment = new BankDetailFragment();
//                    fragment.setArguments(bundle);
                //  replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, BankDetailFragment.TAG);


                //replaceFragment(getActivity(),MAIN_CONTAINER,new AgentRequestsFragment(),TAG,AgentRequestsFragment.TAG);
                break;
            case R.id.btn_cancel:
              //  showToast(getActivity(),"Please Fill The Personal Details");
                 replaceFinal(getActivity(),MAIN_CONTAINER,new MainFragment(),TAG,MainFragment.TAG);
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

    public String formatDateTime() {
        String date = null;
        String strCurrentDate = edtDOB.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date newDate = format.parse(strCurrentDate);
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            date = format.format(newDate);
            Log.d(TAG, "formatDateTime: "+date);
            return date;
        } catch (Exception e) {
            return date;
        }

    }
    public String formatDateTimeUi() {
        String date = null;
       /* String strCurrentDate = edtDOB.getText().toString();*/
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date newDate = format.parse(dateOfBirth);
            format = new SimpleDateFormat("dd/MM/yyyy");
            date = format.format(newDate);
            Log.d(TAG, "formatDateTime: "+date);
            return date;
        } catch (Exception e) {
            return date;
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
