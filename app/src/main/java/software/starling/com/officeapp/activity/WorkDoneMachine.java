package software.starling.com.officeapp.activity;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import software.starling.com.officeapp.R;
import software.starling.com.officeapp.adapter.W_Done_Machine;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.W_D_Machine;

public class WorkDoneMachine extends AppCompatActivity implements ServerLis, getresponce {
    long houra, ratea, amounta, paida, dueamounta;
    @BindView(R.id.btn_save_wdm)
    Button btn_save_wdm;
    @BindView(R.id.btn_next_wdm)
    Button btn_next_wdm;
    @BindView(R.id.et_hours_workDone_M)
    EditText et_hours;
    @BindView(R.id.et_amount_work_Done_M)
    TextView et_amount;
    @BindView(R.id.et_rate_workDone_M)
    EditText et_rate;
    @BindView(R.id.et_paid_workDone_m)
    EditText et_paid;
    @BindView(R.id.tv_due_amount_workDome_M)
    TextView Due_Amount;
    @BindView(R.id.description_spinner_workDone_M)
    Spinner description_spinner;

    @BindView(R.id.recycler_view_d_machine)
    RecyclerView recyclerView;
    @BindView(R.id.add_item_m)
    TextView textView_add_item;
    @BindView(R.id.layout_visbi)
    LinearLayout layout;
    ArrayList<String> machineList;
    ArrayAdapter<String> machineAdapter;
    String PKWorkMachineId;
    String SpinnerPosition, amount, rate, hour, paid,due_amount,PKDailyMachineWorkId;
    Server server;
    Helper helper;
    Activity activity = WorkDoneMachine.this;
    W_Done_Machine adapter;
    Context context;
    ArrayList<W_D_Machine> arrayList = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_done_machine);
        context = WorkDoneMachine.this;

        ButterKnife.bind(this);
        machineList = new ArrayList<>();
        helper = new Helper();
        server = new Server(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        server.registerListener((ServerLis) WorkDoneMachine.this);
        server.getRequest("http://officeapp.starlingsoftwares.com/api/WorkMachine");
        helper.registerListener((getresponce) WorkDoneMachine.this);
        chooseValue();
        setSpinnerValue();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new W_Done_Machine(context, arrayList);
        recyclerView.setAdapter(adapter);

        btn_save_wdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidation();
            }
        });
        btn_next_wdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkDoneMachine.this, MaterialReceived.class);
                startActivity(intent);
            }
        });
        textView_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpinnerPosition.contains("Choose Value")) {
                    Helper.ShowToast(context, "Fill Complete Form");
                } else {
                    layout.setVisibility(View.VISIBLE);
                    btn_save_wdm.setVisibility(View.VISIBLE);
                }

            }
        });

        TextWatcher autoAddTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                houra = TextUtils.isEmpty(et_hours.getText().toString()) ? 0 : Integer.parseInt(et_hours.getText().toString());
                ratea = TextUtils.isEmpty(et_rate.getText().toString()) ? 0 : Integer.parseInt(et_rate.getText().toString());
                paida = TextUtils.isEmpty(et_paid.getText().toString()) ? 0 : Integer.parseInt(et_paid.getText().toString());
                dueamounta = TextUtils.isEmpty(Due_Amount.getText().toString()) ? 0 : Integer.parseInt(Due_Amount.getText().toString());

                String amcalculation = String.valueOf(houra * ratea);
                et_amount.setText(amcalculation);

                amounta = TextUtils.isEmpty(et_amount.getText().toString()) ? 0 : Integer.parseInt(et_amount.getText().toString());

                String duecalculation = String.valueOf(amounta - paida);
                Due_Amount.setText(duecalculation);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        et_hours.addTextChangedListener(autoAddTextWatcher);
        et_rate.addTextChangedListener(autoAddTextWatcher);
        et_paid.addTextChangedListener(autoAddTextWatcher);

    }

    public void setSpinnerValue() {
        machineAdapter = new ArrayAdapter<>(WorkDoneMachine.this, android.R.layout.simple_spinner_dropdown_item, machineList);
        description_spinner.setAdapter(machineAdapter);
        description_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerPosition = String.valueOf(description_spinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void CheckValidation() {
        rate = et_rate.getText().toString();
        amount = et_amount.getText().toString();
        hour = et_hours.getText().toString();
        paid = et_paid.getText().toString();
        due_amount=Due_Amount.getText().toString();
        if (rate.isEmpty() || amount.isEmpty() || hour.isEmpty() || paid.isEmpty()) {
            Helper.ShowToast(activity, "Please Fill All Details");
        } else {
            SaveTableWdm();
        }
    }


    public void chooseValue() {


    }

    @Override
    public void onSuccess(String response) {
        progressDialog.dismiss();
        MyPrecfence.getActiveInstance(WorkDoneMachine.this).setPKDailyManWorkId(response);
        Log.v("rerer", "" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("Data");
            int i;
            machineList.add("Choose Value");
            for (i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String v = object.getString("MachineName");
                Log.v("machinename", "" + object);
                PKWorkMachineId = object.getString("PKWorkMachineId");
                machineList.add(v);



            }
            machineAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        helper.getJson(response);

    }

    @Override
    public void onError(VolleyError error) {
        Log.v("checkself", "" + error.toString());
    }

  /*  @Override
    public void getJson(JSONObject object) throws JSONException {

        String v = object.getString("MachineName");
        Log.v("shivbo", "" + object);
        PKWorkMachineId = object.getString("PKWorkMachineId");
        machineList.add(v);
        machineAdapter.notifyDataSetChanged();
    }*/

    public void SaveTableWdm() {
        Date d = new Date();
        rate = et_rate.getText().toString();
        amount = et_amount.getText().toString();
        hour = et_hours.getText().toString();
        paid = et_paid.getText().toString();
        due_amount=Due_Amount.getText().toString();


        String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
        final HashMap<String, String> param = new HashMap<>();


        SharedPreferences preferences = getSharedPreferences("IdManager", 0);
        param.put("PKProjectId", preferences.getString("project_id", null));
        param.put("EmpId", preferences.getString("emp_id", null));


        param.put("PKWorkMachineId", PKWorkMachineId);
        param.put("Hours", hour);
        param.put("Rate", rate);
        param.put("Amount", amount);
        param.put("Paid", paid);
        param.put("DueAmount", due_amount);
        param.put("Date", Date);
        Log.v("ohooo", PKWorkMachineId);
        server.requestServer(Utils.WorkDoneSave, param, "", new ServerLis() {
            @Override
            public void onSuccess(String response) throws JSONException {

                JSONObject jsonObject=new JSONObject(response);
                String PKDailyMachineWorkId=jsonObject.getString("PKDailyMachineWorkId");



                Log.v("ohhhno", PKDailyMachineWorkId);
               progressDialog.dismiss();
                layout.setVisibility(View.GONE);
                btn_save_wdm.setVisibility(View.GONE);
                arrayList.add(new W_D_Machine(PKDailyMachineWorkId,SpinnerPosition, hour, rate, amount, paid,due_amount));
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
        et_hours.setText("");
        et_rate.setText("");
        et_amount.setText("");
        et_paid.setText("");
        machineList.add("");
    }

    @Override
    public void getJson(JSONObject object) throws JSONException {
        Log.v("getjsonobject", "" + object);
    }
}
