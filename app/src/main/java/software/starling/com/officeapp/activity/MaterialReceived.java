package software.starling.com.officeapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import software.starling.com.officeapp.R;
import software.starling.com.officeapp.adapter.Material_Recived;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.Matterial;

public class MaterialReceived extends AppCompatActivity implements ServerLis, getresponce {


    TextView tv_material, tv_quantity, tv_amount;
    @BindView(R.id.et_material_received_quantity)
    EditText et_material_received_quantity;
    /* @BindView(R.id.et_material_received_amount)
     EditText et_amount;*/
    @BindView(R.id.spinner_material_received)
    Spinner spinner;
    @BindView(R.id.spinner_material_unit)
    Spinner unit;
    @BindView(R.id.btn_next_mr)
    Button btn_next_mr;
    @BindView(R.id.btn_save_mr)
    Button btn_save_mr;

    @BindView(R.id.recycler_view_recv)
    RecyclerView recyclerView;
    @BindView(R.id.layout_recv)
    LinearLayout layout;
    @BindView(R.id.add_item_recv)
    TextView add_item;
    Server server;
    ServerLis serverLis;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter1;
    String[] unitnamee = {"Choose Value", "Box", "Sack", "Number"};
    ArrayList<String> stringArrayList = new ArrayList();
    ArrayList<String> strings1 = new ArrayList<>();
    ArrayList<String> strings = new ArrayList<>();
    Helper helper;
    String PKMaterialAIPLId;
    String et_Amount, et_quantity;
    Context context;
    String Spinnerposition, Spinnerposition1;
    Material_Recived adapter;
    ArrayList<Matterial> arrayList;
    ProgressDialog progressDialog;
    String PKMatReceivedId, unitnameId;
    // String[] unitArray = {"Choose Value", "BOX", "SACK", "NUMBER"};
    String PKUnitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_received);
        ButterKnife.bind(this);


        helper = new Helper();
        arrayList = new ArrayList<>();
        context = MaterialReceived.this;

/*
        arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, unitArray);
        unit.setAdapter(arrayAdapter1);*/

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        getSpinner();
        setSpinner();
        getData();
        CallApi();

        arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stringArrayList);
        unit.setAdapter(arrayAdapter1);
        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinnerposition1 = (String) unit.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_save_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  et_Amount = et_amount.getText().toString();
                et_quantity = et_material_received_quantity.getText().toString();
                if (et_quantity.isEmpty()) {
                    Helper.ShowToast(context, "Please fill All details");
                } else {
                    saveToServer();
                }

            }
        });
        btn_next_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaterialReceived.this, MaterialBought.class);
                startActivity(intent);
            }
        });
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Spinnerposition.contains("Choose Value")) {
                    Helper.ShowToast(context, "Please Fill Details");
                } else {
                    btn_save_mr.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Material_Recived(context, arrayList);
        recyclerView.setAdapter(adapter);
    }

    public void getSpinner() {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, strings);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinnerposition = (String) spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setSpinner() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.MaterialBoughtOutUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("materrrrrialll", response);
                progressDialog.dismiss();
                try {

                    JSONObject object1 = new JSONObject(response);
                    JSONArray jsonArray = object1.getJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.v("harsh", response);
                        //String wn = object.getString("WorkName");

                        unitnameId = object.getString("PKMaterialBoughtOutId");
                        MyPrecfence.getActiveInstance(MaterialReceived.this).setunitReceived(unitnameId);

                        strings1.add(object.getString("Name"));
                    }
                    arrayAdapter1.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        int TimeOut = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(TimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.GetUnitUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("prerna", response);
                progressDialog.dismiss();
                try {

                    JSONObject object1 = new JSONObject(response);
                    JSONArray jsonArray = object1.getJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.v("harsh", response);
                        //String wn = object.getString("WorkName");

                        String unitname = object.getString("UnitName");
                        PKUnitId = object.getString("PKUnitId");
                        MyPrecfence.getActiveInstance(MaterialReceived.this).setPKUnitId(PKUnitId);

                        stringArrayList.add(object.getString("UnitName"));
                    }
                    arrayAdapter1.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        int TimeOut = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(TimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void CallApi() {
        server = new Server(this);
        server.registerListener((ServerLis) this);
        server.getRequest(Utils.Matterial_Recived);
        helper.registerListener((getresponce) this);
    }

    @Override
    public void onSuccess(String response) {
        progressDialog.dismiss();
        MyPrecfence.getActiveInstance(MaterialReceived.this).setMatterial(response);
        strings.add("Choose Value");
        String Data = "Data";
        helper.getObject(response, Data);
        Log.v("spinnervallll", response);
    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void getJson(JSONObject object) throws JSONException {

        PKMaterialAIPLId = object.getString("PKMaterialAIPLId");
        strings.add(object.getString("Name"));
        arrayAdapter.notifyDataSetChanged();

    }

    public void saveToServer() {
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        Date d = new Date();
        // et_Amount = et_amount.getText().toString();
        et_quantity = et_material_received_quantity.getText().toString();
        String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
        param.put("PKMaterialAIPLId", PKMaterialAIPLId);
        //param.put("",unitnameId);
        Log.v("spinnerpos", PKMaterialAIPLId);


        SharedPreferences preferences = getSharedPreferences("IdManager", 0);
        param.put("PKProjectId", preferences.getString("project_id", null));
        param.put("PKEmployeeId", preferences.getString("emp_id", null));
        param.put("PKUnitId", MyPrecfence.getActiveInstance(context).getPKUnitId());
        param.put("Quantity", et_quantity);
        param.put("Date", Date);


        Log.v("HAM", "" + param);
        server.requestServer(Utils.MaterialReceived_Save, param, "", new ServerLis() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                Log.v("materialreceived", "" + response);
                Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    PKMatReceivedId = jsonObject.getString("PKMatReceivedId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // progressDialog.dismiss();
                Log.v("recvied", response);
                btn_save_mr.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                Log.v("responce", "" + et_quantity);
                arrayList.add(new Matterial(PKMatReceivedId, Spinnerposition, Spinnerposition1, et_quantity));
                Log.v("PKMatReceivedId", "" + PKMatReceivedId);
                adapter.notifyDataSetChanged();
                clear();

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public void clear() {
        //  et_amount.setText("");
        et_material_received_quantity.setText("");
        //  strings.add("Choose Value");
    }
}
