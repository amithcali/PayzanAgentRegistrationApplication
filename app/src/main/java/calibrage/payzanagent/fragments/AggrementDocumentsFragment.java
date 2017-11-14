package calibrage.payzanagent.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.activity.CustomPhotoGalleryActivity;
import calibrage.payzanagent.activity.HomeActivity;
import calibrage.payzanagent.adapter.ImageAdapter;
import calibrage.payzanagent.model.AddAgent;
import calibrage.payzanagent.model.AddAgentResponseModel;
import calibrage.payzanagent.model.AgentDoc;
import calibrage.payzanagent.model.LoginResponseModel;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import calibrage.payzanagent.utils.CommonConstants;
import calibrage.payzanagent.utils.CommonUtil;
import calibrage.payzanagent.utils.SharedPrefsData;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;


public class AggrementDocumentsFragment extends BaseFragment {

    public static final String TAG = AggrementDocumentsFragment.class.getSimpleName();

    View view;
    private int PICK_IMAGE = 100;
    protected static final int CAMERA_REQUEST = 1;
    protected static final int PICK_IMAGE_MULTIPLE = 1;
    private static final int REQUEST_PHOTO = 100, REQUIRED_SIZE = 100;
    private Button btnContinue, btnFinish;
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
        btnContinue = (Button) view.findViewById(R.id.btn_add_documents);
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


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
                //openPhotoChooser();
                //startActivity(new Intent(BankDetailsActivity.this,IdProofActivity.class));
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAgentRequest();
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


    private void addAgentRequest() {
        JsonObject object = getLoginObject();
        //  Log.d(TAG, "addAgentRequest: "+object.toString());
        MyServices service = ServiceFactory.createRetrofitService(getActivity(), MyServices.class);
        mRegisterSubscription = service.addAgent(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddAgentResponseModel>() {
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
                                CommonUtil.displayDialogWindow("Record Added Sucessfully", alertDialog, context);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AddAgentResponseModel addAgentResponseModel) {
                        Toast.makeText(getActivity(), "sucess", Toast.LENGTH_SHORT).show();
                        CommonUtil.displayDialogWindow("Record Added Sucessfully", alertDialog, context);
                        //finish();
                    }
                });
    }

    private JsonObject getLoginObject() {
        addDoc();
        return new Gson().toJsonTree(addAgent)
                .getAsJsonObject();
    }

    private void addDoc() {
        for (int i = 0; i < imagesArrayList.size(); i++) {
            AgentDoc agentDoc = new AgentDoc();
            agentDoc.setAgentId("");
            agentDoc.setCreated(currentDatetime);
            agentDoc.setCreatedBy(CommonConstants.USERID);
            agentDoc.setFileBytes(Base64.encodeToString(getImageByteArray(imagesArrayList.get(i)), 0));
            agentDoc.setFileExtension(".jpg");
            agentDoc.setFileName("agentappdoc");
            agentDoc.setFileTypeId(Integer.parseInt(CommonConstants.FILE_TYPE_ID_IMAGES));
            agentDoc.setModified(currentDatetime);
            agentDoc.setModifiedBy(CommonConstants.USERID);
            addAgent.getAgentDocs().add(agentDoc);
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
//                    Intent intent = new Intent(Intent.ACTION_PICK, null);
//                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
//                    startActivityForResult(intent, 1);
                    photoCount = photoCount + 1;

                    dispatchTakePictureIntent(photoCount);
                } else if (options[item].equals("Choose from Gallery")) {
                  /*  Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/
                    Intent intent = new Intent(getActivity(), CustomPhotoGalleryActivity.class);
                    startActivityForResult(intent, 2);
                }/* else if (options[item].equals("Select File"))
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
       /* AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Upload Pictures");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getActivity(), CustomPhotoGalleryActivity.class);
                        startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
                       *//* Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                GALLERY_PICTURE);*//*

                    }
                });

     *//*   myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(
                                ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment
                                .getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(f));

                        startActivityForResult(intent,
                                CAMERA_REQUEST);

                    }
                });*//*
        myAlertDialog.show();*/
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
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
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

                //   Bundle extras = data.getExtras();
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//               // imageView.setImageBitmap(imageBitmap);
//

                //   imageAdapter.notifyDataSetChanged();

//                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data");
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);//
//                }
//                Log.d("image",""+imagesArrayList.size());
                // Log.d("image",""+imagesArrayList.get(0));
//                File f = new File(Environment.getExternalStorageDirectory().toString());
//                for (File temp : f.listFiles()) {
//                    if (temp.getName().equals("temp.jpg")) {
//                        f = temp;
//                        break;
//                    }
//                }
//                try {
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
//                            bitmapOptions);
//
//                    imageView.setImageBitmap(bitmap);
//
//                    String path = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } else if (requestCode == 2) {
                imagesPathList = new ArrayList<String>();
                String[] imagesPath = data.getStringExtra("data").split("\\|");
                try {
                    lnrImages.removeAllViews();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < imagesPath.length; i++) {
                    imagesPathList.add(imagesPath[i]);
                    yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
                    imagesArrayList.add(yourbitmap);
//                    ImageView imageView = new ImageView(getActivity());
//                    imageView.setImageBitmap(yourbitmap);
//                    imageView.setAdjustViewBounds(true);
//                    lnrImages.addView(imageView);
                }

             /*   Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
               // Log.d("path of image from gallery", picturePath+"");
                imageView.setImageBitmap(thumbnail);*/
            }/*else if (requestCode == 3){
                // Get the Uri of the selected file
                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                textView.setText("Selected File Path"+" "+":"+" "+uriString);
                String displayName = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                }
            }*/

        }
        ImageAdapter imageAdapter = new ImageAdapter(context, imagesArrayList);
        imagesRecylerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        imagesRecylerView.setAdapter(imageAdapter);
    }

    public byte[] getImageByteArray(Bitmap bitmap) {
        try {
            if (bitmap != null) {
//                Bitmap bm = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

                return stream.toByteArray();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}


/*

      public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_MULTIPLE ) {
                imagesPathList = new ArrayList<String>();
                String[] imagesPath = data.getStringExtra("data").split("\\|");
                try {
                    lnrImages.removeAllViews();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < imagesPath.length; i++) {
                    imagesPathList.add(imagesPath[i]);
                    yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageBitmap(yourbitmap);
                    imageView.setAdjustViewBounds(true);
                    lnrImages.addView(imageView);
                }
            }
        }


    }
*/




