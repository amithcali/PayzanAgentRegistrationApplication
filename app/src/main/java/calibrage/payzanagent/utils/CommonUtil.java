package calibrage.payzanagent.utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import calibrage.payzanagent.R;

/**
 * Created by Calibrage11 on 10/27/2017.
 */

public  class CommonUtil {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static void getAddressByLocation(final Context context, final double latitude, final double longitude, final boolean onlyVillage) {

                try {
                    Geocoder coder = new Geocoder(context, Locale.getDefault());
                    List<Address> addrList = coder.getFromLocation(latitude, longitude, 1);
                    if (!addrList.isEmpty()) {
                        Address addr = addrList.get(0);
                        String countryCode = addr.getCountryCode();
                        String postalCode = addr.getPostalCode();
                        String locality = addr.getLocality();
                        String area = addr.getAdminArea();

                        String add1 = addr.getAddressLine(0);
                        String add2 = addr.getAddressLine(1);

                        StringBuilder addressBuilder = new StringBuilder();
                        if (!TextUtils.isEmpty(add1)) {
                            addressBuilder.append("House/Door No: " + add1);
                        }

                        if (!TextUtils.isEmpty(add2)) {
                            addressBuilder.append(", \n");
                            addressBuilder.append(add2);
                        }

                        addressBuilder.append(", \n");
                        addressBuilder.append(area);
                        addressBuilder.append(", \n");
                        addressBuilder.append(locality);
                        addressBuilder.append(", \n");
                        addressBuilder.append("Pincode: " + postalCode);


                }
            } catch (IOException e) {
                    e.printStackTrace();
                }

    }

    public  static void  displayDialogWindow(String s, AlertDialog alertDialog, Context context) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogRootView = layoutInflater.inflate(R.layout.dialog_custom,null );
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(dialogRootView);
        alertDialog = alertDialogBuilder.create();
        final TextView textView = (TextView)dialogRootView.findViewById(R.id.description);
        final TextView okBtn = (TextView)dialogRootView.findViewById(R.id.okBtn);
        final AlertDialog finalAlertDialog = alertDialog;
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalAlertDialog.dismiss();
            }
        });
        textView.setText(s);
        // create an alert dialog

//        alertDialog.getWindow()
//                .getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
                return true;
            }
        });

    }


}
