package software.starling.com.officeapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import software.starling.com.officeapp.adapter.RecyclerItemClickListener;
import software.starling.com.officeapp.adapter.WorkDone_mPower;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.MySave;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.W_done_m;

/*Harsh Created*/
public class WorkDoneManPower extends AppCompatActivity implements ServerLis, getresponce {

    long un, up, sn, sp, tams, tamu;
    String PKAgencyId, PKDailyManWorkId;


    ArrayList<W_done_m> arrayList;
    WorkDone_mPower manPower_adap;
    RecyclerView recyclerView;
    Button btn_save_wdmp, btn_next_wdmp;
    EditText et_s_nos, et_s_payment, et_us_nos, et_us_payment;
    Spinner spinner_workname, et_agency;
    ProgressDialog progressDialog;
    String skilledNos, skilledPayment, unskilledNos, unskilledPayment, agencyName, amount, spinnerValue, workid;

    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter, arrayAdapter1;
    String Spinner_Value, spiner_pos;
    LinearLayout layout;
    TextView textView_add_item, et_amount;
    ServerLis serverLis;
    Server server;
    Helper helper;
    ArrayList<String> val = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_done_man_power);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


      /*  progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Please Wait");


        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Please Wait");
*/

        arrayList = new ArrayList<>();


        SetSpinnerData();
        getAgencyValue();
        server = new Server(WorkDoneManPower.this);
        server.registerListener((ServerLis) this);
        server.getRequest(Utils.AgenciesUrl);
        helper = new Helper();
        helper.registerListener((getresponce) this);
        et_s_nos = findViewById(R.id.et_s_nos);
        et_s_payment = findViewById(R.id.et_s_payment);
        et_us_nos = findViewById(R.id.et_us_nos);
        et_us_payment = findViewById(R.id.et_us_payment);
        et_agency = findViewById(R.id.et_agency);
        et_amount = findViewById(R.id.et_amount);

        btn_save_wdmp = findViewById(R.id.btn_save_wdmp);
        btn_next_wdmp = findViewById(R.id.btn_next_wdmp);
        layout = findViewById(R.id.layout);
        textView_add_item = findViewById(R.id.add_item);
        spinner_workname = findViewById(R.id.spinner_workname);

        skilledNos = et_s_nos.getText().toString();
        skilledPayment = et_s_payment.getText().toString();
        unskilledNos = et_us_nos.getText().toString();
        unskilledPayment = et_us_payment.getText().toString();

        amount = et_amount.getText().toString();


        adapter = new ArrayAdapter<>(WorkDoneManPower.this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner_workname.setAdapter(adapter);


        spinner_workname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner_Value = String.valueOf(spinner_workname.getItemAtPosition(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arrayAdapter1 = new ArrayAdapter<>(WorkDoneManPower.this, android.R.layout.simple_spinner_dropdown_item, val);
        et_agency.setAdapter(arrayAdapter1);

        et_agency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spiner_pos = String.valueOf(et_agency.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        textView_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (skilledNos.isEmpty() || skilledPayment.isEmpty() || unskilledNos.isEmpty() || unskilledPayment.isEmpty()) {
                    Toast.makeText(WorkDoneManPower.this, "Please fill Details | Save", Toast.LENGTH_SHORT).show();
                } else
                    btn_save_wdmp.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
            }


        });
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        manPower_adap = new WorkDone_mPower(this, arrayList);
        Log.v("array", "" + arrayList);
        recyclerView.setAdapter(manPower_adap);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        // adapter.notifyDataSetChanged();
        btn_save_wdmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                skilledNos = et_s_nos.getText().toString();
                skilledPayment = et_s_payment.getText().toString();
                unskilledNos = et_us_nos.getText().toString();
                unskilledPayment = et_us_payment.getText().toString();

                amount = et_amount.getText().toString();

                String val = spinner_workname.getSelectedItem().toString();

                if (val.contains("Site Allocation") || skilledNos.isEmpty() || skilledPayment.isEmpty() || unskilledNos.isEmpty() || unskilledPayment.isEmpty()) {
                    Toast.makeText(WorkDoneManPower.this, "Fill the Complete form", Toast.LENGTH_LONG).show();
                } else {
                    SaveTableWdmp();

                }
            }
        });
//        Log.v("gfdsgh", MyPrecfence.getActiveInstance(WorkDoneManPower.this).getProjectId());

        btn_next_wdmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(WorkDoneManPower.this, WorkDoneMachine.class);
                startActivity(intent);
            }
        });

        TextWatcher autoAddTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sn = TextUtils.isEmpty(et_s_nos.getText().toString()) ? 0 : Integer.parseInt(et_s_nos.getText().toString());
                sp = TextUtils.isEmpty(et_s_payment.getText().toString()) ? 0 : Integer.parseInt(et_s_payment.getText().toString());
                un = TextUtils.isEmpty(et_us_nos.getText().toString()) ? 0 : Integer.parseInt(et_us_nos.getText().toString());
                up = TextUtils.isEmpty(et_us_payment.getText().toString()) ? 0 : Integer.parseInt(et_us_payment.getText().toString());

                String amcalculation = String.valueOf(sn * sp);
                tams = Integer.parseInt(amcalculation);

                String duecalculation = String.valueOf(un * up);
                tamu = Integer.parseInt(duecalculation);

                String total = String.valueOf(tams + tamu);
                et_amount.setText(total);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        et_s_nos.addTextChangedListener(autoAddTextWatcher);
        et_s_payment.addTextChangedListener(autoAddTextWatcher);
        et_us_nos.addTextChangedListener(autoAddTextWatcher);
        et_us_payment.addTextChangedListener(autoAddTextWatcher);


    }

    public void SetSpinnerData() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://officeapp.starlingsoftwares.com/api/Works",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  list.add("Choose value");


                        Log.v("responce", response);

                        try {

                            JSONObject object1 = new JSONObject(response);
                            JSONArray jsonArray = object1.getJSONArray("Data");

                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject object = jsonArray.getJSONObject(i);

                                workid = object.getString("PKWorkId");
                                MyPrecfence.getActiveInstance(WorkDoneManPower.this).setPWorkID(workid);

                                list.add(object.getString("WorkName"));
                                adapter.notifyDataSetChanged();

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
        int TimeOut = 14000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(TimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    public void SaveTableWdmp() {


        skilledNos = et_s_nos.getText().toString();
        skilledPayment = et_s_payment.getText().toString();
        unskilledNos = et_us_nos.getText().toString();
        unskilledPayment = et_us_payment.getText().toString();

        amount = et_amount.getText().toString();
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://officeapp.starlingsoftwares.com/api/DailyManWork", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                      progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.v("wdmp", response);
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        PKDailyManWorkId = jsonObject.getString("PKDailyManWorkId");
                        Log.v("check", PKDailyManWorkId);
                        layout.setVisibility(View.GONE);
                        btn_save_wdmp.setVisibility(View.GONE);
                        arrayList.add(new W_done_m(PKDailyManWorkId, Spinner_Value, skilledNos, skilledPayment, unskilledNos, unskilledPayment, spiner_pos, amount));


                       /* et_s_nos.setText("");
                        et_s_payment.setText("");
                        et_us_nos.setText("");
                        et_us_payment.setText("");

                        et_s_payment.setText("");
                        et_amount.setText("");*/
                        Toast.makeText(WorkDoneManPower.this, "Data Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WorkDoneManPower.this, "Fill All the Field First", Toast.LENGTH_SHORT).show();
                    }
                    manPower_adap.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v("errorcoeo", error.toString());
                Toast.makeText(WorkDoneManPower.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                SharedPreferences preferences = getSharedPreferences("IdManager", 0);
                param.put("PKProjectId", preferences.getString("project_id", null));
                param.put("EmpId", preferences.getString("emp_id", null));
                param.put("PKWorkId", workid);
                param.put("SkilledNumber", skilledNos);
                param.put("SkilledPayment", skilledPayment);
                param.put("UnskilledNumber", unskilledNos);
                param.put("UnskilledPayment", unskilledPayment);
                param.put("Agency", PKAgencyId);
                param.put("Amount", amount);
                Date d = new Date();
                String seq = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
                param.put("Date", seq);
                Log.v("parmanu", "" + param);
                return param;

            }


        };
        int TimeOut = 14000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(TimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void getAgencySpinner() {
        // et_agency


    }

    public void getAgencyValue() {


    }

    @Override
    public void onSuccess(String response) {

        MyPrecfence.getActiveInstance(getApplicationContext()).setAgengy(response);
        Log.i("dekhresponce", response);
        val.clear();
        try {
            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("Data");
            val.add("Choose Agency Name ");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object1 = array.getJSONObject(i);

                PKAgencyId = object1.getString("PKAgencyId");
                String AgencyName = object1.getString("AgencyName");

                String d = object1.getString("AgencyName");
                MySave mySave = new MySave();
                mySave.setName(d);
                val.add(AgencyName);


            }
            arrayAdapter1.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void getJson(JSONObject object) throws JSONException {
        //  val.clear();


    }
}
