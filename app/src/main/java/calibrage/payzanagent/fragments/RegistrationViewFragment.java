package calibrage.payzanagent.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.adapter.AgentRequetAdapter;
import calibrage.payzanagent.controls.CommonTextView;
import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AgentPersonalInfo;
import calibrage.payzanagent.model.AgentRequestModel;
import calibrage.payzanagent.model.BusinessCategoryModel;
import calibrage.payzanagent.model.StatesModel;
import calibrage.payzanagent.networkservice.ApiConstants;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import calibrage.payzanagent.service.GPSTracker;
import calibrage.payzanagent.utils.CommonConstants;
import calibrage.payzanagent.utils.CommonUtil;
import calibrage.payzanagent.utils.SharedPrefsData;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static calibrage.payzanagent.utils.CommonUtil.MY_PERMISSIONS_REQUEST_LOCATION;


public class RegistrationViewFragment extends BaseFragment implements OnMapReadyCallback {

    public static final String TAG = RegistrationViewFragment.class.getSimpleName();

    private Button btnContinue;
    View view;
    FragmentManager fragmentManager;
    EditText edtAgentName, edtAgencyName, edtIdNo, edtUserName, edtPassWord, edtMobile, edtEmail, edtProvince, edtAddress,  edtPincode;
    //edtState,
    private AgentRequestModel agentRequestModel;
    private StatesModel statesModel;
    private Context context;
    Spinner spinnerCustom,spinnerState;
    private Subscription operatorSubscription;
    public static Toolbar toolbar;
    private ArrayList<AgentRequestModel.ListResult> listResults;
    ArrayList<String> businessArrayList = new ArrayList<String>();
    private ArrayList<StatesModel.ListResult> listResultArrayList;
    ArrayList<String> statesArrayList = new ArrayList<String>();
    MapView mMapView;
    private GoogleMap googleMap;
    private CommonTextView latlog;
    private GPSTracker gpsTracker;
    private Double lat = 0.0, log = 0.0;
    LocationManager locationManager;
    String provider;
    public AddAgent addAgent;
    Location lastLocation;
    private String stragentname,stragencyname,strid,strusername,strpass,strmobile,stremail,strprovince,straddress,strpin,currentDatetime;
    private AgentPersonalInfo agentPersonalInfo;
    private ArrayList<BusinessCategoryModel.ListResult> businessListResults =new ArrayList<>();
    private ArrayList<StatesModel.ListResult> stateListResults =new ArrayList<>();




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
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.register_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        checkLocationPermission(context);
        initCustomSpinner();
        initStateSpinner();
        getRequest(CommonConstants.BUSINESS_CATEGORY_ID);
        getRequestState(CommonConstants.STATES_ID);
        listResults = new ArrayList();
        listResultArrayList = new ArrayList();
        addAgent = new AddAgent();
        agentPersonalInfo = new AgentPersonalInfo();
        currentDatetime = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("datetime");
       // Log.d(TAG, "dateanddtime"+currentDatetime);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        edtAgentName = (EditText) view.findViewById(R.id.edt_Agentname);
        edtAgencyName = (EditText) view.findViewById(R.id.edt_Agencyname);
        edtIdNo = (EditText) view.findViewById(R.id.edt_idno);
        edtUserName = (EditText) view.findViewById(R.id.edt_userName);
        edtPassWord = (EditText) view.findViewById(R.id.edt_password);
        edtMobile = (EditText) view.findViewById(R.id.edt_mobileno);
        edtEmail = (EditText) view.findViewById(R.id.edt_email);
        edtProvince = (EditText) view.findViewById(R.id.edt_provinceName);
        edtAddress = (EditText) view.findViewById(R.id.edt_address);
       // edtState = (EditText) view.findViewById(R.id.edt_state);
        edtPincode = (EditText) view.findViewById(R.id.edt_pincode);
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
                    if(lat ==0.0 && log ==0.0 && lastLocation!= null ){
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
                if (isValidateUi()) {
                   addAgentPersonalInfo();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("personalinfo", addAgent);
                    Fragment fragment = new BankDetailFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(getActivity(), MAIN_CONTAINER, fragment, TAG, BankDetailFragment.TAG);
//                    ReplcaFragment(fragment);
                    //    login();
                    //ReplcaFragment(new BankDetailFragment());

                   /* fragmentManager.beginTransaction()
                            .replace(R.id.content_frame,  fragment,"BankTag")
                            .commit();*/
                }

                // startActivity(new Intent(RegistrationView.this, BankDetailsActivity.class));
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            agentRequestModel = bundle.getParcelable("request");
            int pos = bundle.getInt("position");
            String name;
            if (agentRequestModel.getListResult().get(pos).getMiddleName() != null) {

                name = agentRequestModel.getListResult().get(pos).getTitleType() + " " + agentRequestModel.getListResult().get(pos).getFirstName() + agentRequestModel.getListResult().get(pos).getMiddleName() + " " + agentRequestModel.getListResult().get(pos).getLastName();
            } else {
                name = agentRequestModel.getListResult().get(pos).getTitleType() + " " + agentRequestModel.getListResult().get(pos).getFirstName() + agentRequestModel.getListResult().get(pos).getLastName();
            }

            edtAgentName.setText(name);
            edtIdNo.setText(String.valueOf(agentRequestModel.getListResult().get(pos).getId()));
            edtMobile.setText(agentRequestModel.getListResult().get(pos).getMobileNumber());
            edtEmail.setText(agentRequestModel.getListResult().get(pos).getEmail());
            edtProvince.setText(agentRequestModel.getListResult().get(pos).getProvinceName());
            edtAddress.setText(agentRequestModel.getListResult().get(pos).getAddressLine1() + "," + agentRequestModel.getListResult().get(pos).getAddressLine2());
            //edtState.setText(agentRequestModel.getListResult().get(pos).getDistrictName());


        }

        return view;
    }

    private void getRequestState(String providerType) {

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


    }

    private void initStateSpinner() {
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
    }


    private void addAgentPersonalInfo() {
        addAgent.setEmail(stremail);
        addAgent.setMobileNumber(strmobile);
        addAgent.setPassword(strpass);
        addAgent.setUserName(strusername);
        agentPersonalInfo.setFirstName(stragentname);
        agentPersonalInfo.setPhone(strmobile);
        agentPersonalInfo.setAgentBusinessCategoryId(businessListResults.get(spinnerCustom.getSelectedItemPosition()-1).getId());
        agentPersonalInfo.setEmail(stremail);
        agentPersonalInfo.setAddress1(straddress);
        agentPersonalInfo.setAddress2(straddress);
        agentPersonalInfo.setAgentRequestId(77);
        agentPersonalInfo.setLandmark(straddress);
        agentPersonalInfo.setIsActive(true);
        agentPersonalInfo.setAspNetUserId("test");
        agentPersonalInfo.setTitleTypeId(17);
        agentPersonalInfo.setGenderTypeId(20);
        agentPersonalInfo.setVillageId(1);
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

    private boolean isValidateUi() {
        boolean status = true;
        stragentname = edtAgentName.getText().toString().trim();
        stragencyname = edtAgencyName.getText().toString();
        strid=edtIdNo.getText().toString();
        strusername=edtUserName.getText().toString();
        strpass=edtPassWord.getText().toString();
        strmobile=edtMobile.getText().toString();
        stremail=edtEmail.getText().toString();
        strprovince=edtProvince.getText().toString();
        straddress=edtAddress.getText().toString();
        strpin=edtPincode.getText().toString();


        if (stragentname.isEmpty()) {
            status = false;
            Toast.makeText(context, "AgentName is required", Toast.LENGTH_SHORT).show();
        } else if (spinnerCustom.getSelectedItemPosition() == 0) {
            status = false;
            Toast.makeText(context, "select business category", Toast.LENGTH_SHORT).show();
        }else if (stragencyname.isEmpty()) {
            status = false;
            Toast.makeText(context, "AgencyName is required", Toast.LENGTH_SHORT).show();
        }else if (strid.isEmpty()) {
            status = false;
            Toast.makeText(context, "Id is required", Toast.LENGTH_SHORT).show();
        }else if (strusername.isEmpty()) {
            status = false;
            Toast.makeText(context, "UserName is required", Toast.LENGTH_SHORT).show();
        }else if (strpass.isEmpty()) {
            status = false;
            Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show();
        }else if (strmobile.isEmpty()) {
            status = false;
            Toast.makeText(context, "MobileNumber is required", Toast.LENGTH_SHORT).show();
        }else if (stremail.isEmpty()) {
            status = false;
            Toast.makeText(context, "Email is required", Toast.LENGTH_SHORT).show();
        }else if (strprovince.isEmpty()) {
            status = false;
            Toast.makeText(context, "Province is required", Toast.LENGTH_SHORT).show();
        }else if (straddress.isEmpty()) {
            status = false;
            Toast.makeText(context, "Address is required", Toast.LENGTH_SHORT).show();
        }else if (strpin.isEmpty()) {
            status = false;
            Toast.makeText(context, "Pincode is required", Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    public  boolean checkLocationPermission(final Context context) {
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
                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
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
                        businessArrayList.add(0,"--Select Business Category--");
                        spinnerCustom.setAdapter(customSpinnerAdapter);
                    }

                });


    }

    private void initCustomSpinner() {

        spinnerCustom = (Spinner) view.findViewById(R.id.spinner);
        // Spinner Drop down elements


        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
