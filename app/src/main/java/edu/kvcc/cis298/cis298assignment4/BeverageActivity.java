package edu.kvcc.cis298.cis298assignment4;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.List;

//Class extnds from the singleFragmentActivity we created
public class BeverageActivity extends SingleFragmentActivity {

    //Key for use in sending data from the list activity to this activity
    private static final String EXTRA_BEVERAGE_ID = "edu.kvcc.cis298.cis298assignment4.beverage_id";
    private static final String TAG = "BEVERAGE_ACTIVITY";
    private static final int REQUEST_CODE = 100;

    //Static method to get a properly formatted intent to get this activity started
    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, BeverageActivity.class);
        intent.putExtra(EXTRA_BEVERAGE_ID, id);
        return intent;
    }

    //Overridden method originally defined in the singleFragmentActivity to create the fragment that will get hosted
    @Override
    protected Fragment createFragment() {
        requestContactsPermission();
        requestInternetPermission();
        String beverageId = getIntent().getStringExtra(EXTRA_BEVERAGE_ID);
        return BeverageFragment.newInstance(beverageId);
    }

    public void requestInternetPermission(){
            Log.i(TAG, "Request Permission started");
            //Begin by checking if the application has permission to access Contacts
            int hasInternetPermission = this.checkSelfPermission(Manifest.permission.INTERNET);

            //If the application does not have permission to access Contacts as the user to grant
            //permission to access Contacts
            //If the application does not have permission to access Contacts as the user to grant
            //permission to access Contacts
            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {

                //Tell the user that the application needs permission to access Contacts
                if (!shouldShowRequestPermissionRationale(Manifest.permission.INTERNET)) {
                    showMessageOKCancel("You need to allow access to Internet",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.INTERNET},
                                            REQUEST_CODE);
                                }
                            });
                    return;
                }

                //Request permission
                requestPermissions(new String[]{Manifest.permission.INTERNET}, REQUEST_CODE);
                return;
            }
    }

    public void requestContactsPermission(){
        Log.i(TAG, "Request Permission started");
        //Begin by checking if the application has permission to access Contacts
        int hasReadContactPermission = this.checkSelfPermission(Manifest.permission.READ_CONTACTS);

        //If the application does not have permission to access Contacts as the user to grant
        //permission to access Contacts
        if (hasReadContactPermission != PackageManager.PERMISSION_GRANTED) {

            //Tell the user that the application needs permission to access Contacts
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                showMessageOKCancel("You need to allow access to Contacts",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                        REQUEST_CODE);
                            }
                        });
                return;
            }

            //Request permission
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
            return;
        }

        Log.i(TAG,"Request Permissions Finished");
    }

    //Show dialog box to inform the user that the application needs permission to access Contacts
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}
