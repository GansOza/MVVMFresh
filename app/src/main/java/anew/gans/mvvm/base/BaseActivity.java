package anew.gans.mvvm.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

//import java.security.Permissions;

import anew.gans.mvvm.base.Permissions;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by psk on 11/19/2017.
 */

public class BaseActivity extends AppCompatActivity
{
    private AlertDialog.Builder alertDialogBuilder;
    public OnPermissionListner onPermissionListner;
    final static int REQUEST_ID_MULTIPLE_PERMISSIONS=1000;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean isNetworkAvailable(final Activity activity) {


        if(checkNetworkState()) {
            return true;
        }else{
            if(alertDialogBuilder==null) {
                alertDialogBuilder = new AlertDialog.Builder(activity);
            }
            alertDialogBuilder.setMessage("Please turn on network connection").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // onBackPressed();

                    if(Build.VERSION.SDK_INT > 15)
                    {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);//android.provider.Settings.ACTION_SETTINGS //Intent.ACTION_MAIN
                        intent.setClassName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        startActivity(intent);
                    }
                }
            }).show();
            return false;
        }
    }
    /**
     * @return
     * @author atul patil
     * @createdon 2018-07-18 12:00
     * @purpose check and request permissions for read and RECEIVE_SMS
     */


    public boolean checkAndRequestPermissions(Activity activity, int[] reqPermitions) {

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (int i : reqPermitions) {

            switch (i) {

                /*case Permissions.SEND_SMS:
                    int permissionSendMessage = ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.SEND_SMS);
                    if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
                    }
                    break;
                case Permissions.RECEIVE_SMS:
                    int receiveSMS = ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.RECEIVE_SMS);
                    if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
                    }
                    break;

                case Permissions.READ_SMS:
                    int readSMS = ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.READ_SMS);

                    if (readSMS != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.READ_SMS);
                    }

                    break;*/

                case Permissions.CAMERA:

                    int camera = ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CAMERA);

                    if (camera != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.CAMERA);
                    }
                    break;
                case Permissions.WRITE_EXTERNAL_STORAGE:
                    int storage = ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (storage != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                case Permissions.READ_EXTERNAL_STORAGE:
                    int storagee = ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (storagee != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }

            }

        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:

                if(permissions.length == 0){
                    return;
                }
                boolean allPermissionsGranted = true;
                if(grantResults.length>0){
                    for(int grantResult: grantResults){
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            allPermissionsGranted = false;
                            break;
                        }
                    }
                }
                if(!allPermissionsGranted){
                    boolean somePermissionsForeverDenied = false;
                    for(String permission: permissions){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                            //denied
                            Log.e("denied", permission);
                            onPermissionListner.onDenied();
                        }else{
                            if(ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                                //allowed
                                Log.e("allowed", permission);
                                onPermissionListner.onGranted();
                            } else{
                                //set to never ask again
                                Log.e("set to never ask again", permission);
                                onPermissionListner.onDenied();
                                somePermissionsForeverDenied = true;
                            }
                        }
                    }
                    if(somePermissionsForeverDenied){
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("You have forcefully denied some of the required permission " +
                                        "for this action. Please open settings, go to permissions and allow them.")
                                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    }
                }
                break;

        }
    }
    public void setOnPermissionListner(OnPermissionListner onPermissionListner){
        this.onPermissionListner=onPermissionListner;
    }
    public boolean checkNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}