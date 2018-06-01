package software.starling.com.officeapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;

public class Report extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Button btn_next;
    Button btn_save;
    int flag = 0;
    Spinner ao_spinner;

    TextView et_project_customer, et_site_location, et_scope, et_activity_date, et_ao_data, et_completion_date;

    ProgressDialog progressDialog;

    Spinner spinner, spinner1, spinner2;
    String[] b = {"No", "Yes"};
    String[] yesArguments = {"Material Billing", "RA Billing", "Final Billing"};
    String id;
    String customer, location, scope, activityDate, aoDate, complitionDate;
    Date d;
    TextView two, three, four, five, six, seven, eight, nine, logout;


    ArrayList<String> list = new ArrayList<>();

    String spinnerValue, spinnerpos, pos;

    ArrayAdapter<String> adapter, adapter1, adapter2;
    String PKProjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait.....");
        progressDialog.setCancelable(false);
        


        server();
        progressDialog.show();
        spinner1 = findViewById(R.id.spinner_work_ready_for_billing);
        spinner2 = findViewById(R.id.spinner_work_ready_for_billing1);
        et_project_customer = findViewById(R.id.et_project_customer);
        et_site_location = findViewById(R.id.et_site_location);
        et_scope = findViewById(R.id.et_scope);
        et_activity_date = findViewById(R.id.et_activity_date);
        et_ao_data = findViewById(R.id.et_ao_data);
        et_completion_date = findViewById(R.id.et_completion_date);
        spinner = findViewById(R.id.ao_spinner);
        two = findViewById(R.id.two);
        two.setOnClickListener(this);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);

        String rr = "<font color='#000000'>PETTY CASH&nbsp&nbsp</font>" + "<font color='#FF0000'>*</font>";
        six.setText(Html.fromHtml(rr));

        seven = findViewById(R.id.seven);
        String rS = "<font color='#000000'>TOMORROW PLANNING&nbsp&nbsp</font>" + "<font color='#FF0000'>*</font>";
        seven.setText(Html.fromHtml(rS));

        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        logout = findViewById(R.id.logout);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        logout.setOnClickListener(this);

        d = new Date();
        String seq = String.valueOf(DateFormat.format("dd/MM/yyy", d.getTime()));
        et_activity_date.setText(seq);
        adapter = new ArrayAdapter<>(Report.this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);

       /* two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Report.this, WorkDoneMachine.class));
            }
        });*/


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = String.valueOf(spinner.getItemAtPosition(position));
                setData(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, b);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerValue = String.valueOf(spinner1.getItemAtPosition(position));
                spinnerValue = parent.getSelectedItem().toString();
              //  Helper.ShowToast(Report.this, spinnerValue);
                Log.v("Value", "" + spinnerValue);

                if (parent.getItemAtPosition(position).equals("Yes")) {
                    spinner2.setVisibility(View.VISIBLE);
                    Toast.makeText(Report.this, "Select Value", Toast.LENGTH_SHORT).show();

                } else {
                    spinner2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yesArguments);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerpos = String.valueOf(spinner2.getItemAtPosition(position));
               //
                //
                // Helper.ShowToast(Report.this, spinnerpos);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.draw);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 1) {
                    flag = 0;
                    Intent intent = new Intent(Report.this, WorkDoneManPower.class);
                    startActivity(intent);
                } else {
                    Helper.ShowToast(Report.this, "Please Fill/Save all The Information First");
                }

              /*  if (spinner.getSelectedItem().toString().equals("Choose AO Number")) {
                    Toast.makeText(Report.this, "Please Select AO Number", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Report.this, WorkDoneManPower.class);
                    startActivity(intent);
                }*/

            }
        });


        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = spinner.getSelectedItem().toString();
                customer = et_project_customer.getText().toString();
                location = et_site_location.getText().toString();
                scope = et_scope.getText().toString();
                activityDate = et_activity_date.getText().toString();
                aoDate = et_ao_data.getText().toString();
                complitionDate = et_completion_date.getText().toString();
                if (val.contains("Choose AO Number") || customer.isEmpty() || location.isEmpty() || scope.isEmpty() || activityDate.isEmpty() || aoDate.isEmpty()) {
                    Toast.makeText(Report.this, "Choose AO Number/Fill Details", Toast.LENGTH_LONG).show();
                } else {
                    SaveData();
                    flag = 1;
                    et_project_customer.setText("");
                    et_site_location.setText("");
                    et_scope.setText("");
                    //et_activity_date.setText("");
                    et_ao_data.setText("");
                    et_completion_date.setText("");
                }

            }
        });


    }

    public void server() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://officeapp.starlingsoftwares.com/api/AONumbers", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // progressDialog.show();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        progressDialog.dismiss();
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        list.add("Choose AO Number");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String Ao = object.getString("AONumber");
                            id = object.getString("PKProjectId");

                            SharedPreferences preferences = getSharedPreferences("IdManager", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("project_id", object.getString("PKProjectId"));
                            editor.commit();
                            Log.v("responce", id);
                            MyPrecfence.getActiveInstance(Report.this).setProjectId(id);

                            list.add(Ao);

                        }

                        adapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(Report.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("responce", error.toString());
            }
        });
        int scoket = 14000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(scoket, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

 /*       if (id == R.id.nav_wdmp) {
            // Handle the camera action
        } else if (id == R.id.nav_wdm) {
            startActivity(new Intent(Report.this, WorkDoneMachine.class));

        } else if (id == R.id.nav_mr) {
            startActivity(new Intent(Report.this, MaterialReceived.class));

        } else if (id == R.id.nav_mb) {
            startActivity(new Intent(Report.this, MaterialBought.class));

        } else if (id == R.id.nav_pettycash) {
            startActivity(new Intent(Report.this, PettyCash.class));

        } else if (id == R.id.nav_tp) {
            startActivity(new Intent(Report.this, TomorrowsPlanning.class));

        } else if (id == R.id.nav_hfcaiip) {
            startActivity(new Intent(Report.this, HindranceAndInternalProblem.class));

        } else if (id == R.id.nav_mbe) {
            startActivity(new Intent(Report.this, MeasurementBookEntry.class));

        } else if (id == R.id.nav_logout) {

            SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("LogIn", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.clear().commit();
            finish();


        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setData(final String value) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://officeapp.starlingsoftwares.com/api/Projects/ByAO", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.show();
                Log.v("reuu", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        progressDialog.dismiss();
                        /*  JSONArray jsonArray = jsonObject.getJSONArray("Data");*/
                        JSONObject object = jsonObject.getJSONObject("Data");
                        PKProjectId = object.getString("PKProjectId");
                        //   MyPrecfence.getActiveInstance(Report.this).setProjectId(PKProjectId);
                        String nameProject = object.getString("ProjectName");
                        String location = object.getString("Location");
                        String numberAo = object.getString("AONumber");
                        // String dateActivity = object.getString("ActivityDate");
                        String dateAo = object.getString("AODate");
                        String dateComplition = object.getString("CompletionDate");
                        String scope = object.getString("Scope");

                        et_project_customer.setText(nameProject);
                        et_site_location.setText(location);
                        et_scope.setText(scope);
                        et_completion_date.setText(dateComplition);
                        //et_activity_date.setText(dateActivity);
                        et_ao_data.setText(dateAo);


                    } else {
                        Toast.makeText(Report.this, "Invalid User", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("reuuddd", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("AONumber", value);
                return param;
            }
        };
        int scoket = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(scoket, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    public void SaveData() {
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://officeapp.starlingsoftwares.com/api/ProjectBillingStatus", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("Harshit", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        progressDialog.dismiss();
                        Toast.makeText(Report.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Harshit", String.valueOf(error));

                Toast.makeText(Report.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                SharedPreferences preferences = getSharedPreferences("IdManager", 0);
                d = new Date();
                String seq = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
                if (spinnerValue.equals("Yes")) {
                    param.put("BillingType", spinnerpos);
                }
                param.put("ProjectId", preferences.getString("project_id", null));
                param.put("EmpId", preferences.getString("emp_id", null));


                param.put("Status", spinnerValue);
                param.put("Date", seq);


                Log.v("date", seq);

                Log.v("ProjectId", id);
                Log.v("dedfsdfe", "" + param);
                // Log.v("Date", seq);
                return param;

            }
        };
        int scoket = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(scoket, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.two:
                startActivity(new Intent(Report.this, WorkDoneManPower.class));
                break;
            case R.id.three:
                startActivity(new Intent(Report.this, WorkDoneMachine.class));
                break;
            case R.id.four:
                startActivity(new Intent(Report.this, MaterialReceived.class));
                break;
            case R.id.five:
                startActivity(new Intent(Report.this, MaterialBought.class));
                break;
            case R.id.six:
                startActivity(new Intent(Report.this, PettyCash.class));
                break;
            case R.id.seven:
                startActivity(new Intent(Report.this, TomorrowsPlanning.class));
                break;
            case R.id.eight:
                startActivity(new Intent(Report.this, HindranceAndInternalProblem.class));
                break;
            case R.id.nine:
                startActivity(new Intent(Report.this, MeasurementBookEntry.class));
                break;
            case R.id.logout:
                SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("LogIn", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.clear().commit();
                finish();
                break;
        }
    }
}
