package calibrage.payzanagent.commonUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidations {
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

    public static boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
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
}
