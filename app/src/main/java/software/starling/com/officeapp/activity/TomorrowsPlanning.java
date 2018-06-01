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

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.adapter.TomorrowPlan;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.Tomorrow;

public class TomorrowsPlanning extends AppCompatActivity implements ServerLis, getresponce {


    Button btn_save_tp, btn_next_tp;
    EditText et_planned_work, et_machnery_type, et_machnery_amount, et_manpower, et_fund, et_frf_type, et_frf_amount;
    TextView Add_Item;
    Context context;
    Spinner spinner_material;
    int flag = 0;
    RecyclerView recyclerView;
    ArrayAdapter<String> spin_adap;
    ArrayList<String> matlist = new ArrayList<>();
    String spin_pos;
    TomorrowPlan adapter;
    ArrayList<Tomorrow> arrayList;
    Server server;
    ServerLis serverLis;
    LinearLayout layout;
    Helper helper;
    ProgressDialog progressDialog;
    String PKTomorrowsPlanningId;
    String PKMaterialBoughtOutId;
    String paln_work, m_Type, m_Amount, manpower, fund, f_Type, f_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomorrows_planning);


        helper = new Helper();
        arrayList = new ArrayList<>();

        server = new Server(this);
        helper.registerListener((getresponce) this);
        server.getRequest(Utils.MaterialBoughtOutUrl);
        server.registerListener((ServerLis) this);
        context = TomorrowsPlanning.this;
        btn_save_tp = findViewById(R.id.btn_save_tp);
        btn_next_tp = findViewById(R.id.btn_next_tp);
        et_planned_work = findViewById(R.id.et_planned_work);
        et_machnery_type = findViewById(R.id.et_machnery_type);
        et_machnery_amount = findViewById(R.id.et_machnery_amount);
        et_manpower = findViewById(R.id.et_manpower);
        spinner_material = findViewById(R.id.et_material);
        et_fund = findViewById(R.id.et_fund);
        et_frf_type = findViewById(R.id.et_frf_type);
        et_frf_amount = findViewById(R.id.et_frf_amount);
        layout = findViewById(R.id.layout_tommorrw);
        Add_Item = findViewById(R.id.add_item_planing);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
       // progressDialog.show();

        recyclerView = findViewById(R.id.recyclerview_tomorrowplanning);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TomorrowPlan(context, arrayList);
        recyclerView.setAdapter(adapter);


      /*  SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Petty", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString("tp", "plan");
        editor1.commit();*/


        btn_save_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paln_work = et_planned_work.getText().toString();
                m_Type = et_machnery_type.getText().toString();
                m_Amount = et_machnery_amount.getText().toString();
                manpower = et_manpower.getText().toString();
                //  String mayterial = et_material.getText().toString();
                fund = et_fund.getText().toString();
                f_Type = et_frf_type.getText().toString();
                f_amount = et_frf_amount.getText().toString();
                if (paln_work.isEmpty() || m_Type.isEmpty() || m_Amount.isEmpty()
                        || manpower.isEmpty() || fund.isEmpty() || f_Type.isEmpty() || f_amount.isEmpty() || spin_pos.contains("Choose Value")) {
                    Helper.ShowToast(context, "Complete fill information");
                } else {
                    SaveToServer();
                    MyPrecfence.getActiveInstance(context).setPlan("t");
                    flag = 1;
                }
            }
        });
        Add_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String paln_work = et_planned_work.getText().toString();

                layout.setVisibility(View.VISIBLE);
                btn_save_tp.setVisibility(View.VISIBLE);


            }
        });
        btn_next_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paln_work = et_planned_work.getText().toString();
                m_Type = et_machnery_type.getText().toString();
                m_Amount = et_machnery_amount.getText().toString();
                manpower = et_manpower.getText().toString();
                //  String mayterial = et_material.getText().toString();
                fund = et_fund.getText().toString();
                f_Type = et_frf_type.getText().toString();
                f_amount = et_frf_amount.getText().toString();

                if (flag == 1) {
                    flag = 0;
                    Intent intent = new Intent(TomorrowsPlanning.this, HindranceAndInternalProblem.class);
                    startActivity(intent);
                } else {
                    Helper.ShowToast(TomorrowsPlanning.this, "Fill/Save the Form First");
                }

              /*  if (paln_work.isEmpty() || m_Type.isEmpty() || m_Amount.isEmpty() || manpower.isEmpty() || spin_pos.contains("Choose Value") || fund.isEmpty() || f_Type.isEmpty() || f_amount.isEmpty()) {
                    Helper.ShowToast(TomorrowsPlanning.this, "Fill the Form First");
                } else {
                    Intent intent = new Intent(TomorrowsPlanning.this, HindranceAndInternalProblem.class);
                    startActivity(intent);
                }*/

            }
        });

        spin_adap = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, matlist);
        spinner_material.setAdapter(spin_adap);
        spinner_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_pos = String.valueOf(spinner_material.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void SaveToServer() {

        Date d = new Date();
        final String paln_work = et_planned_work.getText().toString();
        final String m_Type = et_machnery_type.getText().toString();
        final String m_Amount = et_machnery_amount.getText().toString();
        final String manpower = et_manpower.getText().toString();
        // final String mayterial = et_material.getText().toString();
        final String fund = et_fund.getText().toString();
        final String f_Type = et_frf_type.getText().toString();
        final String f_amount = et_frf_amount.getText().toString();
        String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
        final HashMap<String, String> param = new HashMap<>();
        param.put("PKWorkId", MyPrecfence.getActiveInstance(TomorrowsPlanning.this).getWorkID());
        SharedPreferences preferences = getSharedPreferences("IdManager", 0);
        param.put("PKProjectId", preferences.getString("project_id", null));
        param.put("PKEmployeeId", preferences.getString("emp_id", null));
        param.put("PlannedWork", paln_work);
        param.put("MachineryType", m_Type);
        param.put("MachineryAmount", m_Amount);
        param.put("Manpower", manpower);
        param.put("MaterialBoughtOutId", PKMaterialBoughtOutId);
        param.put("Fund", fund);
        param.put("FundRequiredFor", f_Type);
        param.put("RequiredAmount", f_amount);
        param.put("Date", Date);
        Log.v("ohooo", paln_work);

        server.requestServer(Utils.TomorrowsPlanningUrl, param, "", new ServerLis() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    PKTomorrowsPlanningId = jsonObject.getString("PKTomorrowsPlanningId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // progressDialog.dismiss();
                Log.v("tomarrow", response);
                Helper.ShowToast(TomorrowsPlanning.this,"Data saved Successfully.");
                layout.setVisibility(View.GONE);
                btn_save_tp.setVisibility(View.GONE);
                arrayList.add(new Tomorrow(PKTomorrowsPlanningId, paln_work, m_Type, m_Amount, manpower, spin_pos, fund, f_Type, f_amount));
                adapter.notifyDataSetChanged();
                clear();

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public void clear() {
        et_planned_work.setText("");
        et_machnery_amount.setText("");
        et_machnery_type.setText("");
        et_manpower.setText("");
        // et_material.setText("");
        et_fund.setText("");
        et_frf_amount.setText("");
        et_frf_type.setText("");
    }

    @Override
    public void onSuccess(String response) {
        // MyPrecfence.getActiveInstance(MaterialBought.this).setMatterialBought(response);
        matlist.add("Choose Value");
        Log.v("bought", response);
        String Data = "Data";
        helper.getObject(response, Data);

    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void getJson(JSONObject object) throws JSONException {
        Log.v("jyhtffyt", "" + object);
        PKMaterialBoughtOutId = object.getString("PKMaterialBoughtOutId");
        matlist.add(object.getString("Name"));
        spin_adap.notifyDataSetChanged();
    }
}
