package software.starling.com.officeapp.constant;

import android.app.Activity;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Harsh on 4/19/2018.
 */

public class Server {
    Activity activity;
    ServerLis listener;
    public Server(Activity activity) {
        this.activity = activity;
    }

       public  void requestServer(String url, final HashMap<String, String> params, String constTag, final ServerLis listner) {
//           Volley.newRequestQueue(activity).getCache().clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    listner.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listner.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
           int socketTimeout = 14000;//50 seconds - change to what you want
           RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
           request.setRetryPolicy(policy);
           Volley.newRequestQueue(activity).add(request);
    }

    public void getRequest(String Url) {
        Volley.newRequestQueue(activity).getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    listener.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });
        int socketTimeout = 14000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(activity).add(stringRequest);
    }
    public void registerListener(ServerLis modelData) {
        this.listener = modelData;
    }
}
