package calibrage.payzanagent.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import calibrage.payzanagent.Calib.ext.GsonObjectRequest;
import calibrage.payzanagent.Calib.ext.RequestManager;
import calibrage.payzanagent.Calib.ui.IScreen;
import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.CustomPhotoGalleryActivity;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.adapter.ImageAdapter;
import calibrage.payzanagent.interfaces.DeleteImageListiner;
import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AddAgentResponseModel;
import calibrage.payzanagent.model.AgentDoc;
import calibrage.payzanagent.utils.CommonConstants;
import calibrage.payzanagent.utils.CommonUtil;
import calibrage.payzanagent.utils.Event;
import calibrage.payzanagent.utils.SharedPrefsData;
import calibrage.payzanagent.utils.VolleyErrorListener;
import rx.Subscription;

import static android.app.Activity.RESULT_OK;


public class AggrementDocumentsFragment extends BaseFragment implements IScreen,DeleteImageListiner {

    public static final String TAG = AggrementDocumentsFragment.class.getSimpleName();

    View view;
    private int PICK_IMAGE = 100;
    protected static final int CAMERA_REQUEST = 1;
    protected static final int PICK_IMAGE_MULTIPLE = 1;
    private static final int REQUEST_PHOTO = 100, REQUIRED_SIZE = 100;
    private Button btnAddDocuments, btnFinish;
    public static Toolbar toolbar;
    private Context context;
    private ArrayList<String> imagesPathList;
    private Bitmap yourbitmap;
    TextView textView;
    private AddAgent addAgent;
    FragmentManager fragmentManager;
    private LinearLayout lnrImages;
    String encodedString;
    private Subscription mRegisterSubscription;
    private AlertDialog alertDialog;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    private RecyclerView imagesRecylerView;
    private ArrayList<Bitmap> imagesArrayList = new ArrayList<>();
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public ContentValues values;
    public Uri imageUri;
    public Bitmap thumbnail;
    public String imageurl, currentDatetime;
    public int photoCount;
    private AgentDoc agentDoc;
    ImageAdapter imageAdapter;

    public AggrementDocumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_aggrement_documents, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        btnAddDocuments = (Button) view.findViewById(R.id.btn_add_documents);
        //  textView = (TextView)view.findViewById(R.id.txtPath);
        btnFinish = (Button) view.findViewById(R.id.btn_finish);
        imageView = (ImageView) view.findViewById(R.id.view_image);
        lnrImages = (LinearLayout) view.findViewById(R.id.lnrImages);
        imagesRecylerView = (RecyclerView) view.findViewById(R.id.imagesRecylerView);
        context = this.getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.register_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        currentDatetime = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("datetime");
        RequestManager.initializeWith(context, new RequestManager.Config("data/data/predento/pics",
                5242880, 4));

        btnAddDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getActivity(), "Adding Agent...");
                getData(Event.AddAgent);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            addAgent = bundle.getParcelable("idproof");
        }

        return view;
    }






    private String getLoginObject() {
        addDoc();
        Log.d(TAG, "getLoginObject: " + new Gson().toJson(addAgent));
        return
                new Gson().toJson(addAgent);
    }

    private void addDoc() {
        List<AgentDoc> agentDocList = new ArrayList<>();
        for (int i = 0; i < imagesArrayList.size(); i++) {
            AgentDoc agentDoc = new AgentDoc();
            agentDoc.setAgentId(null);
            agentDoc.setCreated(currentDatetime);
            agentDoc.setCreatedBy(CommonConstants.USERID);
            agentDoc.setFileBytes(null);
            agentDoc.setBase64File(getImageByteArray(imagesArrayList.get(0)));
            agentDoc.setFileExtension(".jpg");
            agentDoc.setFileName(addAgent.getAgentPersonalInfo().getFirstName());
            agentDoc.setFileTypeId(Integer.parseInt(CommonConstants.FILE_TYPE_ID_IMAGES));
            agentDoc.setModified(currentDatetime);
            agentDoc.setModifiedBy(CommonConstants.USERID);
            agentDoc.setIsActive(true);
            agentDoc.setFileLocation("App");
            //addAgent.setAgentDocs(agentDoc);
            agentDocList.add(agentDoc);
        }
        if(agentDocList.isEmpty()){
            showToast(context,"Please Select Document");
        }
        else{
            addAgent.setAgentDocs(agentDocList);
        }

    }


    private void startDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Documents.....");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    photoCount = photoCount + 1;

                    dispatchTakePictureIntent(photoCount);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(getActivity(), CustomPhotoGalleryActivity.class);
                    startActivityForResult(intent, 2);
                }
                /* else if (options[item].equals("Select File"))
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(intent,3);

                    *//*Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
*//*
                }*/ else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void dispatchTakePictureIntent(int imageCount) {
        values = new ContentValues();
        // addAgent.getAgentPersonalInfo().getFirstName()
        values.put(MediaStore.Images.Media.TITLE, imageCount);
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(
                                context.getContentResolver(), imageUri);
                        //imgView.setImageBitmap(thumbnail);
                        imageurl = getRealPathFromURI(imageUri);
                        imagesArrayList.add(thumbnail);
                        //imagesArrayList.add(imageBitmap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            } else if (requestCode == 2) {
                imagesPathList = new ArrayList<String>();
                String[] imagesPath = data.getStringExtra("data").split("\\|");
                try {
                    lnrImages.removeAllViews();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < imagesPath.length; i++) {
                    imageurl = imagesPath[i];
                    imagesPathList.add(imagesPath[i]);
                    yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
                    imagesArrayList.add(yourbitmap);

                }

            }

        }
         imageAdapter = new ImageAdapter(context, imagesArrayList);
        imageAdapter.setOnAdapterListener(AggrementDocumentsFragment.this);
        imagesRecylerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        imagesRecylerView.setAdapter(imageAdapter);

    }

    public String getImageByteArray(Bitmap bitmap) {
        String value = null;
        try {
            if (bitmap != null) {
//                Bitmap bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                 value = Base64.encodeToString( stream.toByteArray(), Base64.NO_WRAP);
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return value;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        hideDialog();
        if (serviceResponse instanceof AddAgentResponseModel) {
            AddAgentResponseModel addAgentResponseModel;
            addAgentResponseModel = (AddAgentResponseModel) serviceResponse;
            if(addAgentResponseModel.getIsSuccess()){
                CommonUtil.displayDialogWindow(addAgentResponseModel.getEndUserMessage(),alertDialog,context);
                replaceFinal(getActivity(), MAIN_CONTAINER, new MainFragment(), TAG, MainFragment.TAG);
            }

        }
    }

    @Override
    public void onEvent(int eventId, Object eventData) {


    }

    @Override
    public void getData(int actionID) {
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("Content-Type", "application/json; charset=utf-8");
       // showDialog(getActivity(),"");

        RequestManager.addRequest(new GsonObjectRequest<AddAgentResponseModel>
                ("http://payzandev1.azurewebsites.net/api/Agent/AddAgent", reqHeader, getLoginObject(),
                        AddAgentResponseModel.class, new VolleyErrorListener(this,
                        Event.AddAgent)) {

            @Override
            protected void deliverResponse(AddAgentResponseModel response) {
                updateUi(true, Event.AddAgent, response);
            }
        });
    }

    @Override
    public void onAdapterClickListiner(int pos,boolean isPopUp) {

        if(isPopUp){
            showImageDialog(pos);
        }else {
            if(!imagesArrayList.isEmpty()){
                imagesArrayList.remove(pos);
                imageAdapter.notifyDataSetChanged();
            }
        }

    }

    private void showImageDialog(int pos) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_showimage);


        final ImageView expand_image = (ImageView) dialog.findViewById(R.id.expand_image);
        expand_image.setImageBitmap(imagesArrayList.get(pos));

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

    }
}






