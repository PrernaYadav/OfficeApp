package software.starling.com.officeapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.adapter.Material_Bought;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.M_Bought;

public class MaterialBought extends AppCompatActivity implements ServerLis, getresponce {
    Button btn_save_mb, btn_next_mb;
    long a,b,c;
    EditText et_material_bought_quantity,et_material_bought_rate;
    TextView et_materila_bought_amount;
    Spinner spinner_material_bought;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> strings = new ArrayList<>();
    String Spinner_Pos;
    String PKMaterialBoughtId;
    Server server;
    ServerLis serverLis;
    Helper helper;
    Context context;
    String quantity, amount,rate;
    Material_Bought adapter;
    ArrayList<M_Bought> arrayList;
    RecyclerView recyclerView;
    LinearLayout layout;
    TextView textView_addItem;
    String PKMaterialBoughtOutId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_bought);


        context = MaterialBought.this;
        server = new Server(this);
        server.registerListener((ServerLis) this);
        server.getRequest(Utils.MaterialBoughtOutUrl);
        helper = new Helper();
        helper.registerListener((getresponce) this);
        arrayList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        btn_save_mb = findViewById(R.id.btn_save_mb);
        btn_next_mb = findViewById(R.id.btn_next_mb);
        et_material_bought_quantity = findViewById(R.id.et_material_bought_quantity);
        et_material_bought_rate = findViewById(R.id.et_material_bought_rate);
        et_materila_bought_amount = findViewById(R.id.et_materila_bought_amount);
        spinner_material_bought = findViewById(R.id.spinner_material_bought);
        layout = findViewById(R.id.layout_m_bought);
        textView_addItem = findViewById(R.id.add_item_bought);


        recyclerView = findViewById(R.id.recycler_view_m_bought);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Material_Bought(context,arrayList);
        recyclerView.setAdapter(adapter);

        btn_next_mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaterialBought.this, PettyCash.class);
                startActivity(intent);
            }
        });
        btn_save_mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity = et_material_bought_quantity.getText().toString();
                rate = et_material_bought_rate.getText().toString();
                amount = et_materila_bought_amount.getText().toString();
                if (quantity.isEmpty() || amount.isEmpty() || Spinner_Pos.contains("Choose Value") || rate.isEmpty()) {
                    Helper.ShowToast(context, "Please Fill Details");
                } else {
                    SaveToServer();
                }
            }
        });
        textView_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Spinner_Pos.contains("Choose value")) {
                    Helper.ShowToast(context, "Please Complete Form !!");
                } else {
                    btn_save_mb.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);

                }

            }
        });
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, strings);
        spinner_material_bought.setAdapter(arrayAdapter);
        spinner_material_bought.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner_Pos = String.valueOf(spinner_material_bought.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TextWatcher autoAddTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                a = TextUtils.isEmpty(et_material_bought_quantity.getText().toString()) ? 0 : Integer.parseInt(et_material_bought_quantity.getText().toString());
                b = TextUtils.isEmpty(et_material_bought_rate.getText().toString()) ? 0 : Integer.parseInt(et_material_bought_rate.getText().toString());

                String amcalculation = String.valueOf(a * b);
                et_materila_bought_amount.setText(amcalculation);
                            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        et_material_bought_quantity.addTextChangedListener(autoAddTextWatcher);
        et_material_bought_rate.addTextChangedListener(autoAddTextWatcher);

    }

    /*
     * Save Data On Server*/
    public void SaveToServer() {
        progressDialog.show();
        quantity = et_material_bought_quantity.getText().toString();
        amount = et_materila_bought_amount.getText().toString();
        rate=et_material_bought_rate.getText().toString();
        Date d = new Date();
        String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
        HashMap<String, String> param = new HashMap<>();
        param.put("PKMaterialBoughtOutId", PKMaterialBoughtOutId);
        Log.v("spidhhhd", PKMaterialBoughtOutId);

        SharedPreferences preferences = getSharedPreferences("IdManager", 0);
        param.put("PKProjectId", preferences.getString("project_id", null));
        param.put("PKEmployeId", preferences.getString("emp_id", null));

        param.put("Quantity", quantity);
        param.put("Rate", rate);
        param.put("Amount", amount);
        param.put("Date", Date);

        server.requestServer(Utils.MaterialBoughtUrl, param, "", new ServerLis() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    PKMaterialBoughtId=jsonObject.getString("PKMaterialBoughtId");
                    Log.v("PKMaterialBoughtId",PKMaterialBoughtId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

              //  progressDialog.dismiss();
                Log.v("boughtseccess", response);
                layout.setVisibility(View.GONE);
                btn_save_mb.setVisibility(View.GONE);
                arrayList.add(new M_Bought(PKMaterialBoughtId,Spinner_Pos, quantity,rate, amount));
                adapter.notifyDataSetChanged();
                clear();
                Helper.ShowToast(context, "Saved Successfully");
            }

            @Override
            public void onError(VolleyError error) {
                Helper.ShowToast(context, "oops-Some Error?");
            }
        });
    }

    /* Get Spinner Value
     * */
    @Override
    public void onSuccess(String response) {
        progressDialog.dismiss();
        MyPrecfence.getActiveInstance(MaterialBought.this).setMatterialBought(response);
        strings.add("Choose Value");
        Log.v("bought", response);
        String Data = "Data";
        helper.getObject(response, Data);
    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void getJson(JSONObject object) throws JSONException {
        Log.v("respo", "" + object);
        PKMaterialBoughtOutId = object.getString("PKMaterialBoughtOutId");
        strings.add(object.getString("Name"));
        arrayAdapter.notifyDataSetChanged();
    }

    public void clear() {
        et_material_bought_quantity.setText("");
        et_materila_bought_amount.setText("");
    }
}
