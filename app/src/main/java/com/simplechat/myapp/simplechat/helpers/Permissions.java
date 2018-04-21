package com.simplechat.myapp.simplechat.helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean permissionsValidate( int requestCode, Activity activity, String[] permissions){

        if (Build.VERSION.SDK_INT >= 23){

            List<String> permissionsList = new ArrayList<String>();

// verify if the permission has already been granted
            for (String permission : permissions){
                Boolean validatePermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if ( !validatePermission ) permissionsList.add(permission);
            }
// check if no permission need to be granted
            if (permissionsList.isEmpty()) return true;
// convert permissions list to array
            String[] permissionsArray = new String[permissionsList.size()];
            permissionsList.toArray( permissionsArray);
// request permission
            ActivityCompat.requestPermissions(activity, permissionsArray, requestCode);
        }

        return true;
    }
}
