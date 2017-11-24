package calibrage.payzanagent.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import calibrage.payzanagent.BuildConfig;
import calibrage.payzanagent.Calib.ext.GsonObjectRequest;
import calibrage.payzanagent.Calib.ext.RequestManager;
import calibrage.payzanagent.Calib.ui.IScreen;
import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.CustomPhotoGalleryActivity;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.adapter.DocsAdapter;
import calibrage.payzanagent.adapter.ImageAdapter;
import calibrage.payzanagent.interfaces.DeleteIdproofListiner;
import calibrage.payzanagent.interfaces.DeleteImageListiner;
import calibrage.payzanagent.interfaces.DocListiner;
import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AddAgentResponseModel;
import calibrage.payzanagent.model.AgentDoc;
import calibrage.payzanagent.model.DocDeleteModel;
import calibrage.payzanagent.model.GetBankInfoModel;
import calibrage.payzanagent.model.GetDocumentsResponseModel;
import calibrage.payzanagent.model.IdProofDeleteModel;
import calibrage.payzanagent.model.LoginResponseModel;
import calibrage.payzanagent.model.UpdateAgentRequestModel;
import calibrage.payzanagent.model.UpdateAgentRequestResponceModel;
import calibrage.payzanagent.model.UploadDocumentResponseModel;
import calibrage.payzanagent.networkservice.ApiConstants;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import calibrage.payzanagent.utils.CommonConstants;
import calibrage.payzanagent.utils.CommonUtil;
import calibrage.payzanagent.utils.Event;
import calibrage.payzanagent.utils.FileDownloader;
import calibrage.payzanagent.utils.SharedPrefsData;
import calibrage.payzanagent.utils.VolleyErrorListener;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static calibrage.payzanagent.utils.CommonConstants.AGENT_REQUEST_ID;


public class AggrementDocumentsFragment extends BaseFragment implements DeleteImageListiner, View.OnClickListener, DocListiner {

    public static final String TAG = AggrementDocumentsFragment.class.getSimpleName();
    private static final int MEGABYTE = 1024 * 1024;
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
    private RecyclerView imagesRecylerView, docsRecyclerview;
    private ArrayList<Bitmap> imagesArrayList = new ArrayList<>();
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public ContentValues values;
    public Uri imageUri;
    public Bitmap thumbnail;
    public String imageurl, currentDatetime, pdfPath;
    public int photoCount;
    private AgentDoc agentDoc;
    ImageAdapter imageAdapter;
    ProgressBar progressBar;
    private Button personalButton, bankButton, idButton, documentButton, uploadDoc, btnCancel;
    private ArrayList<Pair<String, String>> filePathArray;
    private ArrayList<GetDocumentsResponseModel.ListResult> docIdproofList = new ArrayList<>();
    private int count = 0;
    private boolean isUpdate = false;
    private Subscription operatorSubscription;

    public AggrementDocumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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
        textView = (TextView) view.findViewById(R.id.txtPath);
        btnFinish = (Button) view.findViewById(R.id.btn_finish);
        uploadDoc = (Button) view.findViewById(R.id.uploadDoc);
        imageView = (ImageView) view.findViewById(R.id.view_image);
        lnrImages = (LinearLayout) view.findViewById(R.id.lnrImages);
        docsRecyclerview = (RecyclerView) view.findViewById(R.id.recylerview_documnets);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        personalButton = (Button) view.findViewById(R.id.btn_personal);
        bankButton = (Button) view.findViewById(R.id.btn_bank);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        idButton = (Button) view.findViewById(R.id.btn_id);
        documentButton = (Button) view.findViewById(R.id.btn_doc);
        filePathArray = new ArrayList<>();

     /*   personalButton.setOnClickListener(this);
        bankButton.setOnClickListener(this);
        idButton.setOnClickListener(this);*/
        documentButton.setOnClickListener(this);

        getDocuments(CommonConstants.AGENT_ID);
        imagesRecylerView = (RecyclerView) view.findViewById(R.id.imagesRecylerView);
        context = this.getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        HomeActivity.toolbar.setTitle(getResources().getString(R.string.register_sname));
        HomeActivity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white_new));
        currentDatetime = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("datetime");
//        RequestManager.initializeWith(context, new RequestManager.Config("data/data/predento/pics",
//                5242880, 4));

        btnAddDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdate){
                    submitRequest();
                }else{
                    showToast(context,"Upload files is manditory");
                }

            }
        });
        uploadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialog(getActivity(), "Adding Agent...");
                // progressBar.setVisibility(View.VISIBLE);
                if (filePathArray.size() > 0) {
                    postDocuments();
                } else {
                    showToast(context, "Please add Documents");
                }

                //getData(Event.AddAgent);

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


    private void getDocuments(String agentId) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        operatorSubscription = service.getAgentDocuments(ApiConstants.GET_AGENT_DOCUMENTS + agentId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetDocumentsResponseModel>() {
                    @Override
                    public void onCompleted() {
                        //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            hideDialog();
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
                    public void onNext(GetDocumentsResponseModel getDocumentsResponseModel) {
                        hideDialog();
                        if (!getDocumentsResponseModel.getListResult().isEmpty()) {
                            isUpdate = true;
                            docIdproofList = (ArrayList<GetDocumentsResponseModel.ListResult>) getDocumentsResponseModel.getListResult();
                            DocsAdapter docsAdapter = new DocsAdapter(context, getDocumentsResponseModel.getListResult());
                            docsAdapter.setOnAdapterListener(AggrementDocumentsFragment.this);
                            docsRecyclerview.setLayoutManager(new LinearLayoutManager(context));
                            docsRecyclerview.setAdapter(docsAdapter);

                            showToast(getActivity(), getDocumentsResponseModel.getListResult().get(0).getFileLocation());
                        } else {
                            isUpdate = false;
                            // btnContinue.setText("Continue");
                        }

                    }

                });
    }

    private void postDocuments() {
        showDialog(getActivity(), "Authenticating...");
        JsonObject object = addDoc(count);
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        mRegisterSubscription = service.uploadDocument(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadDocumentResponseModel>() {
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
                        Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UploadDocumentResponseModel uploadDocumentResponseModel) {
                        hideDialog();
                        count++;
                        if (count == filePathArray.size()) {
                            Toast.makeText(context, "Files  Uploading Finished Sucessfully", Toast.LENGTH_SHORT).show();
                            getDocuments(CommonConstants.AGENT_ID);
                            imagesArrayList.clear();
                            imageAdapter.notifyDataSetChanged();
                        } else {
                            postDocuments();
                        }
                    }
                });
    }


    private JsonObject addDoc(int pos) {
        //  List<AgentDoc> agentDocList = new ArrayList<>();
        // for (int i = 0; i < imagesArrayList.size(); i++) {
        AgentDoc agentDoc = new AgentDoc();
        agentDoc.setAgentId(CommonConstants.AGENT_ID);
        agentDoc.setCreatedBy(CommonConstants.USERID);
        agentDoc.setFileBytes(null);
        agentDoc.setBase64File(convertFileToByteArray(filePathArray.get(pos).first, filePathArray.get(pos).second));

        // agentDoc.set(addAgent.getAgentPersonalInfo().getFirstName());
        if (filePathArray.get(pos).second.equalsIgnoreCase(CommonConstants.FILE_TYPE_ID_IMAGES)) {
            agentDoc.setFileTypeId(Integer.parseInt(CommonConstants.FILE_TYPE_ID_IMAGES));
            agentDoc.setFileExtension(".JPEG");
        } else {
            agentDoc.setFileTypeId(Integer.parseInt(CommonConstants.FILE_TYPE_ID_DOCUMENTS));
            agentDoc.setFileExtension(".pdf");
        }
        // agentDoc.setModified(currentDatetime);
        agentDoc.setModifiedBy(CommonConstants.USERID);
        agentDoc.setIsActive(true);

        //  agentDoc.setFileLocation("App");
        //addAgent.setAgentDocs(agentDoc);
        //  agentDocList.add(agentDoc);
        //  }
//        if(agentDocList.isEmpty()){
//            showToast(context,"Please Select Document");
//        }
//        else{
//            addAgent.setAgentDocs(agentDocList);
//        }
        return
                new Gson().toJsonTree(agentDoc)
                        .getAsJsonObject();

    }

    private void showConformationDialog(final int pos) {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(context);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (!imagesArrayList.isEmpty()) {
                            imagesArrayList.remove(pos);
                            filePathArray.remove(pos);
                            imageAdapter.notifyDataSetChanged();
                        }
                        ;
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void submitRequest() {
        showDialog(getActivity(), "Authenticating...");
        JsonObject object = updateAgentStatus();
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        mRegisterSubscription = service.AgentUpdateRequest(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateAgentRequestResponceModel>() {
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
                    public void onNext(UpdateAgentRequestResponceModel updateAgentRequestResponceModel) {
                        hideDialog();
                        if (updateAgentRequestResponceModel.getIsSuccess()) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                            CommonUtil.displayDialogWindow(updateAgentRequestResponceModel.getEndUserMessage(), alertDialog, context);
                            replaceFinal(getActivity(), MAIN_CONTAINER, new MainFragment(), TAG, MainFragment.TAG);


                        } else {
                            Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                            CommonUtil.displayDialogWindow(updateAgentRequestResponceModel.getEndUserMessage(), alertDialog, context);
                        }

                    }
                });
    }

    private JsonObject updateAgentStatus() {
        UpdateAgentRequestModel updateAgentRequestModel = new UpdateAgentRequestModel();
        updateAgentRequestModel.setAgentRequestId(Integer.parseInt(AGENT_REQUEST_ID));
        updateAgentRequestModel.setStatusTypeId(Integer.valueOf(CommonConstants.STATUSTYPE_ID_SUBMIT_FOR_REVIEW));
        updateAgentRequestModel.setAssignToUserId(SharedPrefsData.getInstance(context).getStringFromSharedPrefs("userid"));
        updateAgentRequestModel.setComments("");
        updateAgentRequestModel.setId(null);
        updateAgentRequestModel.setIsActive(true);
        updateAgentRequestModel.setCreatedBy(SharedPrefsData.getInstance(context).getStringFromSharedPrefs("userid"));
        updateAgentRequestModel.setModifiedBy(SharedPrefsData.getInstance(context).getStringFromSharedPrefs("userid"));
        updateAgentRequestModel.setCreated(currentDatetime);
        updateAgentRequestModel.setModified(currentDatetime);
        return new Gson().toJsonTree(updateAgentRequestModel)
                .getAsJsonObject();
    }


    private void startDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Select File", "Cancel"};

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
                } else if (options[item].equals("Select File")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(intent, 3);

                    //Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
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

                        filePathArray.add(Pair.create(imageurl, CommonConstants.FILE_TYPE_ID_IMAGES));
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
                    filePathArray.add(Pair.create(imageurl, CommonConstants.FILE_TYPE_ID_IMAGES));
                    imagesPathList.add(imagesPath[i]);
                    yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
                    imagesArrayList.add(yourbitmap);

                }

            } else if (requestCode == 3) {

             /*   pdfPath =  data.getData().getPath();
                textView.setText(pdfPath);*/

                Uri filePath = data.getData();
                //  File file = new File(filePath.getPath());

                //textView.setText(filePath.toString());

                String pathis = getPath2(context, filePath);
                filePathArray.add(Pair.create(pathis, CommonConstants.FILE_TYPE_ID_DOCUMENTS));
                imagesArrayList.add(null);
                //  convertFileToByteArray(pathis, CommonConstants.FILE_TYPE_ID_DOCUMENTS);
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
                //   showDialog(getActivity(), "Adding Images...");
                //   Bitmap bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), uri);
                // BitmapFactory.decodeFile(imagesPath[i]);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                value = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
                //  hideDialog();
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return value;
    }

    /*public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }*/
    public static String convertFileToByteArray(String pdfPath, String isImage) {
        String val = null;
        if (isImage.equalsIgnoreCase(CommonConstants.FILE_TYPE_ID_DOCUMENTS)) {

            byte[] byteArray = null;
            try {
                File f = new File(pdfPath);

                if (f.exists()) {
                    InputStream inputStream = new FileInputStream(f);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] b = new byte[1024 * 10000];
                    int bytesRead = 0;

                    while ((bytesRead = inputStream.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }

                    byteArray = bos.toByteArray();

                    Log.e("Byte array", ">" + byteArray);

                    val = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                }

                return val;

            } catch (IOException e) {
                e.printStackTrace();
                return val;
            }

        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(pdfPath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            String value = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
            return value;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

//    @Override
//    public void updateUi(boolean status, int actionID, Object serviceResponse) {
//      //  hideDialog();
//        progressBar.setVisibility(View.GONE);
//        if (serviceResponse instanceof AddAgentResponseModel) {
//            AddAgentResponseModel addAgentResponseModel;
//            addAgentResponseModel = (AddAgentResponseModel) serviceResponse;
//            if(addAgentResponseModel.getIsSuccess()){
//                CommonUtil.displayDialogWindow(addAgentResponseModel.getEndUserMessage(),alertDialog,context);
//                replaceFinal(getActivity(), MAIN_CONTAINER, new MainFragment(), TAG, MainFragment.TAG);
//            }else {
//                CommonUtil.displayDialogWindow(addAgentResponseModel.getEndUserMessage(),alertDialog,context);
//            }
//
//        }
//    }
//
//    @Override
//    public void onEvent(int eventId, Object eventData) {
//
//
//    }
//
//    @Override
//    public void getData(int actionID) {
//        HashMap<String, String> reqHeader = new HashMap<String, String>();
//        reqHeader.put("Content-Type", "application/json; charset=utf-8");
//       // showDialog(getActivity(),"");
//
//        RequestManager.addRequest(new GsonObjectRequest<AddAgentResponseModel>
//                (BuildConfig.LOCAL_URL+ ApiConstants.ADD_AGENT, reqHeader, getLoginObject(),
//                        AddAgentResponseModel.class, new VolleyErrorListener(this,
//                        Event.AddAgent)) {
//
//            @Override
//            protected void deliverResponse(AddAgentResponseModel response) {
//                updateUi(true, Event.AddAgent, response);
//            }
//        });
//    }

    @Override
    public void onAdapterClickListiner(int pos, boolean isPopUp) {

        if (isPopUp) {
            showImageDialog(pos);
        } else {

            showConformationDialog(pos);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
  /*          case R.id.btn_personal:
                showToast(getActivity(),"Please Fill The Personal Details");
                // replaceFragment(getActivity(), MAIN_CONTAINER, new RegistrationViewFragment(), TAG, RegistrationViewFragment.TAG);
                break;
            case R.id.btn_bank:
                showToast(getActivity(),"Please Fill The Personal Details");
                //replaceFragment(getActivity(),MAIN_CONTAINER,new AgentRequestsFragment(),TAG,AgentRequestsFragment.TAG);
                break;
            case R.id.btn_id:
                showToast(getActivity(),"Please Fill The Personal Details");
                // replaceFragment(getActivity(),MAIN_CONTAINER,new InProgressFragment(),TAG,InProgressFragment.TAG);
                break;*/
            case R.id.btn_cancel:
                //  showToast(getActivity(),"Please Fill The Personal Details");
                replaceFinal(getActivity(), MAIN_CONTAINER, new MainFragment(), TAG, MainFragment.TAG);
                break;
            case R.id.btn_doc:
                showToast(getActivity(), "Please Add Documents");
                //replaceFragment(getActivity(),MAIN_CONTAINER,new ApprovedAgentsFragment(),TAG,ApprovedAgentsFragment.TAG);
                break;
        }
    }

    @SuppressLint("NewApi")
    public static String getPath2(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private void showConformationDialogDelete(final int pos) {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(context);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteId(pos);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteId(int pos) {
        showDialog(getActivity(), "Authenticating...");
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        operatorSubscription = service.deletedoc(BuildConfig.LOCAL_URL + ApiConstants.DELETE_ID_DOC + docIdproofList.get(pos).getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DocDeleteModel>() {
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
                        Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DocDeleteModel docDeleteModel) {
                        hideDialog();
                        if (docDeleteModel.getIsSuccess()) {
                            showToast(context, docDeleteModel.getEndUserMessage());
                            //   replaceFragment(getActivity(), MAIN_CONTAINER, new IdProofFragment(), TAG, IdProofFragment.TAG);
                            getDocuments(CommonConstants.AGENT_ID);
                        } else {
                            showToast(context, docDeleteModel.getEndUserMessage());
                        }

                    }
                });

    }

    @Override
    public void onDocAdapterClickListiner(int pos, boolean isDelete, boolean isPdf) {
        if (isDelete)
            showConformationDialogDelete(pos);
        else if (isPdf)
            new DownloadPdfFile(context, docIdproofList.get(pos).getFileUrl(), docIdproofList.get(pos).getFileName(), docIdproofList.get(pos).getFileExtension()).execute();
        else if (!isPdf)
            new DownloadImageFile(context, docIdproofList.get(pos).getFileUrl(), docIdproofList.get(pos).getFileName(), docIdproofList.get(pos).getFileExtension()).execute();
    }


    private class DownloadPdfFile extends AsyncTask<String, Void, Void> {

        private Context context;
        private String path;
        private String fileName;
        private String extension;

        private DownloadPdfFile(Context context, String path, String fileName, String extension) {
            this.context = context;
            this.path = path;
            this.fileName = fileName;
            this.extension = extension;

        }

        @Override
        protected Void doInBackground(String... strings) {
            showDialogAsk(getActivity(), "downloading file");
            String fileUrl = path.replace("\\", "/");
            String fileName = this.fileName;  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "payZanSalesExecutive");
            if (!folder.exists()) {
                folder.mkdir();
            }

            File pdfFile = new File(folder, fileName + extension);

            try {
                pdfFile.createNewFile();


            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!pdfFile.exists()) {
                downloadFile(fileUrl, pdfFile);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(context, "File exists in Folder", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideDialogAsk();
        }

        public void downloadFile(String fileUrl, File directory) {
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadImageFile extends AsyncTask<String, Void, Void> {

        private Context context;
        private String path;
        private String fileName;
        private String extension;

        private DownloadImageFile(Context context, String path, String fileName, String extension) {
            this.context = context;
            this.path = path;
            this.fileName = fileName;
            this.extension = extension;

        }

        @Override
        protected Void doInBackground(String... strings) {
            showDialogAsk(getActivity(), "downloading file");
            String fileUrl = path;   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = this.fileName;  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "payZanSalesExecutive");
            if (!folder.exists()) {
                folder.mkdir();
            }

            File pdfFile = new File(folder, fileName + extension);

            try {
                pdfFile.createNewFile();


            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!pdfFile.exists()) {
                downloadFile(fileUrl, pdfFile);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(context, "File exists in Folder", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideDialogAsk();
        }

        public void downloadFile(String fileUrl, File directory) {
            try {
                URL url = new URL(fileUrl);
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                try {
                    FileOutputStream out = new FileOutputStream(directory);
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
    }

}






