package calibrage.payzanagent.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import calibrage.payzanagent.Calib.ui.IScreen;


/***
 * @author ankur.goyal
 * API Error handle of get@ application
 */

public class VolleyErrorListener implements Response.ErrorListener {
    private final int action;
    private final IScreen screen;
    private ErrorModel errorModel_setdata;

    public VolleyErrorListener(final IScreen screen, final int action) {
        this.screen = screen;
        this.action = action;

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String str = "";
        int code = 0;
        ErrorModel errorModel = new ErrorModel();

        if (error != null && error.networkResponse != null && error.networkResponse.data != null) {

            try {
                str = new String(error.networkResponse.data, "UTF-8");
                code = error.networkResponse.statusCode;
                Log.e("Code", "" + code);
                Log.e("str", "" + str);
                errorModel = jsonToArray(str, code);
                if (errorModel != null) {
                    if (code == 401)
                   // common.CommonUtils.mAuthenticationCounter.start();
                    screen.updateUi(false, action, errorModel);
                } else {
                    errorModel = new ErrorModel();
                  if (code >= 402 && code < 500) {
                        errorModel.setStatusMessage("Bad Request");
                        screen.updateUi(false, action, errorModel);
                    } else if (code >= 500){
                        errorModel.setStatusMessage("Server error");
                        screen.updateUi(false, action, errorModel);
                    }
                }
                return;
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        } else if (screen != null)
            if (error instanceof NoConnectionError) {
                Log.e("VolleyError", "here1");
                try {
                    Log.e("Response", "" + error);
                    str = new String(error.networkResponse.data, "UTF-8");
                    Log.e("VolleyError", str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                errorModel.setStatusMessage(Constant.NoConnectionError);
                screen.updateUi(false, action, errorModel);
            } else if (error instanceof AuthFailureError) {
                errorModel.setStatusMessage(Constant.AuthFailureError);
                screen.updateUi(false, action, errorModel);
            } else if (error instanceof NetworkError) {
                errorModel.setStatusMessage(Constant.NetworkError);
                screen.updateUi(false, action, errorModel);
            } else if (error instanceof ParseError) {
                errorModel.setStatusMessage(Constant.ParseError);
                screen.updateUi(false, action, errorModel);
            } else if (error instanceof ServerError) {
                errorModel.setStatusMessage(Constant.ServerError);
                screen.updateUi(false, action, errorModel);
            } else if (error instanceof TimeoutError) {
                errorModel.setStatusMessage(Constant.TimeoutError);
                screen.updateUi(false, action, errorModel);
            }
    }

    /***
     *
     * @param jsonInput
     * @param statusCode
     * @return
     */
    private ErrorModel jsonToArray(String jsonInput, int statusCode) {
        try {
            JSONObject jsonObject = new JSONObject(jsonInput);
             errorModel_setdata = new ErrorModel();
            if (jsonObject.has("timestamp"))
            errorModel_setdata.setTimestamp(jsonObject.optInt("timestamp"));
            if (jsonObject.has("statusCode"))
            errorModel_setdata.setStatusCode(jsonObject.optInt("statusCode"));
            if (jsonObject.has("error"))
            errorModel_setdata.setError(jsonObject.optString("error"));
            if (jsonObject.has("statusMessage"))
            errorModel_setdata.setStatusMessage(jsonObject.optString("statusMessage"));
            if (jsonObject.has("error_description"))
            errorModel_setdata.setError_description(jsonObject.optString("error_description"));
            if (jsonObject.has("message"))
            errorModel_setdata.setStatusMessage(jsonObject.optString("message"));

            return  errorModel_setdata;
        } catch (Exception e) {
            Log.e("","exception is : "+e.toString());
            return  errorModel_setdata;
//            return_icon null;
        }
    }
}
