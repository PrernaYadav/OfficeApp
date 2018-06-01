package software.starling.com.officeapp.constant;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import software.starling.com.officeapp.activity.LogIn;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by Harsh .
 */

public class Helper {
    public getresponce serverLis;
    private static final int LOCATION_CODE = 101;

    public static boolean isDataConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo activeNetworkInfo = connectMan.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isCheckSelf(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;

    }

    public static void setPermission(Activity context) {
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
    }

    public static void ShowToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void Logcat(String Tag, String value)

    {
        Log.v(Tag, value);
    }

    public static ProgressDialog showProgressDialog(Activity activity, String msg) {
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        return dialog;
    }


    public static void dismissDialog(ProgressDialog dialog) {
        if (dialog == null) return;
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void getJson(String responce) {

        try {
            JSONArray array = new JSONArray(responce);
            int i;
            for (i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                serverLis.getJson(object);
                String v = object.getString("MachineName");
                Log.v("rerer", "" + v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getData(JSONObject object, String v, String Val) throws JSONException {
        v = object.getString(Val);
    }



    public void getObject(String responce, String Array)  {
        try {
            JSONObject object = new JSONObject(responce);
            JSONArray array = object.getJSONArray(Array);
            int i;
            for (i = 0; i < array.length(); i++) {
                JSONObject object1 = array.getJSONObject(i);
                serverLis.getJson(object1);



            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }
    public void registerListener(getresponce modelData) {
        this.serverLis = modelData;
    }
}
