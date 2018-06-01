package software.starling.com.officeapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;

public class LogIn extends AppCompatActivity {
    Button btn_login;
    EditText et_employee_id;
    EditText et_login_password;
    TextView tv_forgot_password;
    String empId;
    String pass;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

      /*  if (ContextCompat.checkSelfPermission(LogIn.this, Manifest.permission.INTERNET) +
                ContextCompat.checkSelfPermission(LogIn.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE}, 1111);
        }*/

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        btn_login = findViewById(R.id.btn_login);
        et_employee_id = findViewById(R.id.et_employee_id);
        et_login_password = findViewById(R.id.et_login_password);
        //  tv_forgot_password = findViewById(R.id.tv_forgot_password);

        // MyApplication.getInstance().addToRequestQueue(stringRequest,"");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                empId = et_employee_id.getText().toString();
                pass = et_login_password.getText().toString();

                Log.d("ooo", "" + empId + "  " + pass);

//Save login values
              /*  SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("LogIn", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("id", empId);
                editor1.putString("pass", pass);
                editor1.commit();*/

                if (Helper.isCheckSelf(LogIn.this)) {
                    if (empId.isEmpty() || pass.isEmpty()) {
                        Toast.makeText(LogIn.this, "Please check the Employee id / Password", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            serverConnection();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Helper.setPermission(LogIn.this);
                }

            }
        });
    }

    public void serverConnection() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://officeapp.starlingsoftwares.com/api/Login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("harshit", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        JSONArray array = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            Log.v("wffffffffde", "" + object);

                            SharedPreferences preferences = getSharedPreferences("IdManager", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("emp_id", object.getString("PKEmployeeId"));
                            editor.commit();
                            MyPrecfence.getActiveInstance(LogIn.this).setPKEmployeeId(object.getString("PKEmployeeId"));
                            MyPrecfence.getActiveInstance(LogIn.this).setEmpCode(object.getString("EmpCode"));
                            MyPrecfence.getActiveInstance(LogIn.this).setFirstName(object.getString("FirstName"));
                            MyPrecfence.getActiveInstance(LogIn.this).setLastName(object.getString("LastName"));
                            MyPrecfence.getActiveInstance(LogIn.this).setPKDeptId(object.getString("PKDeptId"));
                            startActivity(new Intent(LogIn.this, Report.class));
                            finish();
                        }


                        Toast.makeText(LogIn.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LogIn.this, "Invalid User", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v("harshit", error.toString());
                Toast.makeText(LogIn.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("EmpCode", et_employee_id.getText().toString());
                param.put("Pwd", et_login_password.getText().toString());
                return param;
            }
        };
        int scoket = 14000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(scoket, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);


    }


}
