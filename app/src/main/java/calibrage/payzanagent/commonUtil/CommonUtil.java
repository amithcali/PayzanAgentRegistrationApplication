package calibrage.payzanagent.commonUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Calibrage19 on 06-10-2017.
 */

public class CommonUtil {
    private static String LOG_TAG = "";
    static Pattern pattern = null;
    static Matcher matcher;
    public static final Pattern VEHICLE_NUMBER_PATTERN = Pattern.compile("^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$");
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-zA-Z]).{6,20})";
    public static DecimalFormat twoDForm = new DecimalFormat("#.##");
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    public static String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
    };
    public static final int PERMISSION_CODE = 100;

    public static boolean isEmpty(String myeditText) {
        return myeditText.trim().length() == 0;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean passwordValidate(final String password, Context context) {
        /*
         * Pattern pattern = null; Matcher matcher;
		 */
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        if (matcher.matches() == false)
            Toast.makeText(context, "Password Must contain: Minimum 6 characters and atleast one number.", Toast.LENGTH_SHORT).show();
        return matcher.matches();

    }

    public static File getPayzanFilePath() {
        String root = Environment.getExternalStorageDirectory().toString();
        File rootDirectory = new File(root + "/PayZan" + "/profile");
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }
        File finalFile = new File(root, "" + ".jpg");
        return finalFile;
    }

    public static boolean spinnerSelect(Spinner spinner, int position, Context context) {
        if (position == 0 || position <= 0) {
            Toast.makeText(context, "Please select " + spinner, Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    public static boolean spinnerPositinCondition(String spinner, int minimum, int maximum, Context context) {
        if (minimum >= maximum) {
            Toast.makeText(context, "Please select " + spinner, Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    public static String[] fromMap(LinkedHashMap<String, String> inputMap, String type) {
        Collection c = inputMap.values();
        Iterator itr = c.iterator();
        int size = inputMap.size() + 1;
        String[] toMap = new String[size];
        toMap[0] = "-- Select " + type + " --";
        int iCount = 1;
        while (iCount < size && itr.hasNext()) {
            toMap[iCount] = itr.next().toString();
            iCount++;
        }
        while (iCount < size && itr.hasNext()) {
            toMap[iCount] = itr.next().toString();
            iCount++;
        }
        return toMap;
    }

    public static double getPercentage(double n, double total) {
        double proportion;
        proportion = ((n - total) / (n + total)) * 100;
        return Math.abs(Math.round(proportion));
    }

    public static int parseIntValue(String inputValue) {
        if (!TextUtils.isEmpty(inputValue) && TextUtils.isDigitsOnly(inputValue)) {
            try {
                return Integer.parseInt(inputValue);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return 0;
    }

    public static String convertToDays(String inputStr) {
        int resultInt = 0;
        String resultStr = inputStr.replaceAll("\\D+", "");
        if (inputStr.contains("Week") || inputStr.contains("Weeks")) {
            resultInt = parseIntValue(resultStr) * 7;
            resultStr = String.valueOf(resultInt);
        } else if (inputStr.contains("Month") || inputStr.contains("Months")) {
            resultInt = parseIntValue(resultStr) * 30;
            resultStr = String.valueOf(resultInt);
        }
        return resultStr;
    }

    public static boolean isEmptySpinner(final Spinner inputSpinner) {
        if (null == inputSpinner) return true;
        if (inputSpinner.getSelectedItemPosition() == -1 || inputSpinner.getSelectedItemPosition() == 0) {
            return true;
        }
        return false;
    }

    public static byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static void hideSoftKeyboard(final AppCompatActivity appCompatActivity) {
        if (appCompatActivity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) appCompatActivity.getSystemService(appCompatActivity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(appCompatActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static LinkedHashMap<String, Object> toMap(JSONObject object) throws JSONException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static String printKeyHash(Activity context) {


        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

//    public  static void  displayDialogWindow(String s, AlertDialog alertDialog, Context context) {
//        // get prompts.xml view
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View dialogRootView = layoutInflater.inflate(R.layout.dialog_custom,null );
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setView(dialogRootView);
//        alertDialog = alertDialogBuilder.create();
//        final TextView textView = (TextView)dialogRootView.findViewById(R.id.description);
//        final TextView okBtn = (TextView)dialogRootView.findViewById(R.id.okBtn);
//        final AlertDialog finalAlertDialog = alertDialog;
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finalAlertDialog.dismiss();
//            }
//        });
//        textView.setText(s);
//        // create an alert dialog
//
////        alertDialog.getWindow()
////                .getAttributes().windowAnimations = R.style.DialogAnimation;
//        alertDialog.show();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {
//
//            @Override
//            public boolean onKey(DialogInterface arg0, int keyCode,
//                                 KeyEvent event) {
//                // TODO Auto-generated method stub
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//                }
//                return true;
//            }
//        });
//
//    }

    public static boolean areAllPermissionsAllowed(final Context context, final String[] permissions) {
        boolean isAllPermissionsGranted = false;
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_GRANTED) {
                isAllPermissionsGranted = true;
            }
        }
        return isAllPermissionsGranted;
    }

//    public static Drawable buildCounterDrawable(Context context,int count, int backgroundImageId) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
//        view.setBackgroundResource(backgroundImageId);
//
//        if (count == 0) {
//            //View counterTextPanel = view.findViewById(R.id.counterValuePanel);
//            //counterTextPanel.setVisibility(View.GONE);
//        } else {
//            TextView textView = (TextView) view.findViewById(R.id.count);
//            textView.setText("" + count);
//        }
//
//        view.measure(
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//
//        view.setDrawingCacheEnabled(true);
//        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//        view.setDrawingCacheEnabled(false);
//
//        return new BitmapDrawable(context.getResources(), bitmap);
//    }
}
