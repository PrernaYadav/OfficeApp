package software.starling.com.officeapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import software.starling.com.officeapp.NewLocation;
import software.starling.com.officeapp.R;
import software.starling.com.officeapp.adapter.BookEntery_Adapter;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.BookEntery;
import software.starling.com.officeapp.model.Tomorrow;
import software.starling.com.officeapp.model.W_D_Machine;

import static com.android.volley.Request.Method.GET;

public class MeasurementBookEntry extends AppCompatActivity implements ServerLis, getresponce {
    long length, bridth, height, area;

    Button btn_save_table, btn_next;
    EditText et_doi, et_nos, et_l, et_b, et_h;
    TextView et_qtys;
    ProgressDialog progressDialog;
    Spinner spinner;
    String PKMeasurementBookId;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> ulist = new ArrayList<>();
    String description, nos, l, b, h, qtys, spinner_pos;
    RecyclerView recyclerView;
    BookEntery_Adapter adapter;
    ArrayList<BookEntery> arrayList;
    LinearLayout layout;
    TextView textView_Add;
    Helper helper;
    Server server;
    String PKUnitId;
    AlertDialog alertDialog;
    ServerLis serverLis;
    Context context;
    String location;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_book_entry);
        arrayList = new ArrayList<>();
        context = MeasurementBookEntry.this;
        spinner = findViewById(R.id.spinner_mbe);
        et_doi = findViewById(R.id.et_doi);
        et_nos = findViewById(R.id.et_nos);
        et_l = findViewById(R.id.et_l);
        et_b = findViewById(R.id.et_b);
        et_h = findViewById(R.id.et_h);
        et_qtys = findViewById(R.id.et_qtys);
        btn_save_table = (Button) findViewById(R.id.btn_save_table);
        btn_next = findViewById(R.id.btn_next_table);

        arrayList = new ArrayList<>();
        helper = new Helper();
        server = new Server(this);
        context = MeasurementBookEntry.this;
        server.registerListener((ServerLis) this);
        // server.getRequest(Utils.MaterialBoughtOutUrl);
        helper.registerListener((getresponce) this);


        if (Helper.isDataConnected(this) || Helper.isWifiConnected(this)) {
            NewLocation newLocation = new NewLocation(this);
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> list = geocoder.getFromLocation(newLocation.getLatitude(), newLocation.getLongitude(), 1);
                location = list.get(0).getFeatureName() + "," + list.get(0).getSubLocality() + "," + list.get(0).getLocality();

                Log.v("ewafweqqwefcwe", location);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }


        recyclerView = findViewById(R.id.recycler_book);
        textView_Add = findViewById(R.id.add_item_bookentry);
        layout = findViewById(R.id.layout_book);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookEntery_Adapter(context, arrayList);
        recyclerView.setAdapter(adapter);


        getSpinner();


        btn_save_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                description = et_doi.getText().toString();
                nos = et_nos.getText().toString();
                l = et_l.getText().toString();
                b = et_b.getText().toString();
                h = et_h.getText().toString();
                qtys = et_qtys.getText().toString();

                if (description.isEmpty() || nos.isEmpty() || l.isEmpty() || b.isEmpty() || h.isEmpty() || qtys.isEmpty()) {
                    Toast.makeText(MeasurementBookEntry.this, "Please Fill all the field", Toast.LENGTH_SHORT).show();
                } else {

                    try {


                        saveTable();
                        // progressDialog.show();
                       /* serverConnection();
                        progressDialog.show();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        textView_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
                btn_save_table.setVisibility(View.VISIBLE);
            }
        });


        TextWatcher autoAddTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                length = TextUtils.isEmpty(et_l.getText().toString()) ? 0 : Integer.parseInt(et_l.getText().toString());
                bridth = TextUtils.isEmpty(et_b.getText().toString()) ? 0 : Integer.parseInt(et_b.getText().toString());
                height = TextUtils.isEmpty(et_h.getText().toString()) ? 0 : Integer.parseInt(et_h.getText().toString());
                // dueamounta = TextUtils.isEmpty(Due_Amount.getText().toString()) ? 0 : Integer.parseInt(Due_Amount.getText().toString());

                String amcalculation = String.valueOf(length * bridth * height);
                et_qtys.setText(amcalculation);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        et_l.addTextChangedListener(autoAddTextWatcher);
        et_b.addTextChangedListener(autoAddTextWatcher);
        et_h.addTextChangedListener(autoAddTextWatcher);
        //  et_amount.addTextChangedListener(autoAddTextWatcher);

        /*SharedPreferences sharedPreferences = getSharedPreferences("Petty", MODE_PRIVATE);
        id = sharedPreferences.getString("tp", "");
        id = sharedPreferences.getString("iddd", "");
        Log.v("tttt", "" + id);*/


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyPrecfence.getActiveInstance(context).getPlan().equals("p") || MyPrecfence.getActiveInstance(context).getPetty().equals("t")) {
                    MyPrecfence.getActiveInstance(context).setPlan("");
                    MyPrecfence.getActiveInstance(context).setPetty("");
                    Intent intent = new Intent(MeasurementBookEntry.this, FinishApp.class);
                    startActivity(intent);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MeasurementBookEntry.this);

                    builder.setMessage("Please fill petty cash & Tomorrow Planning form  ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Warring");
                    alert.show();
                }

            }
        });

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ulist);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_pos = String.valueOf(spinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getSpinner() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(MeasurementBookEntry.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://officeappapi.starlingsoftwares.com/api/Unit", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("Data");
                    int i;
                    for (i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        PKUnitId = object1.getString("PKUnitId");
                        String UnitName = object1.getString("UnitName");
                        Log.v("vgvggg", PKUnitId);
                        Log.v("HHJHJHJHH", UnitName);

                        ulist.add(object1.getString("UnitName"));
                        adapter.notifyDataSetChanged();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        ulist.add("Choose Value");
        int TimeOut = 14000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(TimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    public void saveTable() {
        progressDialog.show();
        final String description = et_doi.getText().toString();
        final String nos = et_nos.getText().toString();
        final String l = et_l.getText().toString();
        final String b = et_b.getText().toString();
        final String h = et_h.getText().toString();
        final String qtys = et_qtys.getText().toString();
        Date d = new Date();
        String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
        final HashMap<String, String> param = new HashMap<>();
        //  param.put("PKWorkId", MyPrecfence.getActiveInstance(MeasurementBookEntry.this).getWorkID());
        SharedPreferences preferences = getSharedPreferences("IdManager", 0);
        param.put("PKProjectId", preferences.getString("project_id", null));
        param.put("PKEmployeeId", preferences.getString("emp_id", null));
        param.put("Item", description);
        param.put("Number", nos);
        param.put("PKUnitId", PKUnitId);
        param.put("Length", l);
        param.put("Breadth", b);
        param.put("Height", h);
        param.put("Quantity", qtys);
        param.put("Date", Date);
        //param.put("Latitude", "1212");
        if (location != null) {
            param.put("Location", location);
        } else {
            param.put("Location", "Location not Found");
        }


        Log.v("dgrfyudguy", PKUnitId);
        // Toast.makeText(this, "" + PKUnitId, Toast.LENGTH_SHORT).show();
        //  Log.v("workid", MyPrecfence.getActiveInstance(MeasurementBookEntry.this).getWorkID());
        // Log.v("projectid", MyPrecfence.getActiveInstance(MeasurementBookEntry.this).getProjectId());
        //  Log.v("employeeid", MyPrecfence.getActiveInstance(MeasurementBookEntry.this).getPKEmployeeId());
        Log.v("des", description);
        Log.v("number", nos);
        Log.v("length", l);
        Log.v("bridth", b);
        Log.v("height", h);
        Log.v("tarikh", Date);
        server.requestServer(Utils.MeasurementBookUrl, param, "", new ServerLis() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    PKMeasurementBookId = jsonObject.getString("PKMeasurementBookId");
                    //  Helper.ShowToast(context, "" + jsonObject.getString("PKMeasurementBookId"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // progressDialog.dismiss();
                Log.v("mberesponse", response);
                layout.setVisibility(View.GONE);
                btn_save_table.setVisibility(View.GONE);
                arrayList.add(new BookEntery(PKMeasurementBookId, description, nos, spinner_pos, l, b, h, qtys));
                adapter.notifyDataSetChanged();
                clear();
            }

            @Override
            public void onError(VolleyError error) {
                Log.v("ohhhno", error.toString());
            }
        });
    }

    public void clear() {
        et_doi.setText("");
        et_nos.setText("");

        et_l.setText("");
        et_b.setText("");
        et_h.setText("");
        et_qtys.setText("");
    }

    @Override
    public void onSuccess(String response) {

      /*  strings.add("Choose Value");
        Log.v("bought", response);
        String Data = "Data";
        helper.getObject(response, Data);*/

    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void getJson(JSONObject object) throws JSONException {

       /* Log.v("respo", "" + object);
        PKMaterialBoughtOutId = object.getString("PKMaterialBoughtOutId");
        strings.add(object.getString("Name"));
        arrayAdapter.notifyDataSetChanged();*/

    }
}