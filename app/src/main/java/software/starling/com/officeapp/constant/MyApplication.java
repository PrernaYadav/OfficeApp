package software.starling.com.officeapp.constant;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by Harsh .
 */

public class MyApplication extends Application {

    public static MyApplication mInstance;
    private final String TAG = MyApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
       /* Fabric.with(this, new Crashlytics());
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("Kasrat.realm").build();
        Realm.setDefaultConfiguration(config);*/
    }


//    public Realm getDefaultRealm() {
//        return Realm.getDefaultInstance();
//    }


    public static DefaultRetryPolicy getDefaultRetryPolice() {
        return new DefaultRetryPolicy(20000, 10, 1);
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
