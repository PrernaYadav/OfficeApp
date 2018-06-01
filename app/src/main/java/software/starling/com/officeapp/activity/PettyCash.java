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
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import software.starling.com.officeapp.NewLocation;
import software.starling.com.officeapp.R;
import software.starling.com.officeapp.adapter.PrettyCash;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.model.Pretty_cash;

public class PettyCash extends AppCompatActivity implements ServerLis {

    long amreceived, ampaid, ambalanec;

    EditText et_description_petty, et_received_petty, et_paid_petty;
    TextView et_balance_petty;
    int flag = 0;

    Button btn_save_pretty, btn_next_pretty;
    TextView textView_addItem;
    RecyclerView recyclerView;
    PrettyCash adapter;
    ArrayList<Pretty_cash> arrayList;
    Context context;
    String PKPettyCashId;
    Server server;
    ServerLis serverLis;
    String descrip, recv, paid, blance;
    LinearLayout layout;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petty_cash);
        arrayList = new ArrayList<>();
        context = PettyCash.this;
        server = new Server(PettyCash.this);
        server.registerListener((ServerLis) this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
       // progressDialog.show();

        et_description_petty = findViewById(R.id.et_description_petty);
        et_received_petty = findViewById(R.id.et_received_petty);
        et_paid_petty = findViewById(R.id.et_paid_petty);
        et_balance_petty = findViewById(R.id.et_balance_petty);
        btn_save_pretty = findViewById(R.id.btn_save_pretty);
        btn_next_pretty = findViewById(R.id.btn_next_pretty);
        textView_addItem = findViewById(R.id.add_item_petty);
        layout = findViewById(R.id.layout_prttey);

        recyclerView = findViewById(R.id.recyclerview_prettey);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrettyCash(context, arrayList);
        recyclerView.setAdapter(adapter);
//to check the form is saved
       /* SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Petty", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString("iddd", "pc");
        editor1.commit();*/


        btn_next_pretty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descrip = et_description_petty.getText().toString();
                recv = et_received_petty.getText().toString();
                paid = et_paid_petty.getText().toString();
                blance = et_balance_petty.getText().toString();

                if (flag == 1) {
                    flag = 0;
                    Intent intent = new Intent(PettyCash.this, TomorrowsPlanning.class);
                    startActivity(intent);
                } else {

                    Helper.ShowToast(PettyCash.this, "Fill/Save the Form First");

                }
               /* if (descrip.isEmpty() || recv.isEmpty() || paid.isEmpty()) {
                   // Toast.makeText(context, "Fill the Form First", Toast.LENGTH_SHORT).show();
                    Helper.ShowToast(PettyCash.this, "Fill the Form First");
                } else {


                    Intent intent = new Intent(PettyCash.this, TomorrowsPlanning.class);
                    startActivity(intent);

                }*/
            }
        });
        btn_save_pretty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descrip = et_description_petty.getText().toString();
                recv = et_received_petty.getText().toString();
                paid = et_paid_petty.getText().toString();
                blance = et_balance_petty.getText().toString();
                if (descrip.isEmpty() || recv.isEmpty() || paid.isEmpty() || blance.isEmpty()) {
                    Helper.ShowToast(context, "fill Complete Information");
                } else {
                    SaveToServer();
                    MyPrecfence.getActiveInstance(context).setPetty("p");
                    flag = 1;

                }
            }
        });
        textView_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String descrip = et_description_petty.getText().toString();

                layout.setVisibility(View.VISIBLE);
                btn_save_pretty.setVisibility(View.VISIBLE);


            }
        });
        TextWatcher autoAddTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ampaid = TextUtils.isEmpty(et_paid_petty.getText().toString()) ? 0 : Integer.parseInt(et_paid_petty.getText().toString());
                // ambalanec = TextUtils.isEmpty(et_balance_petty.getText().toString()) ? 0 : Integer.parseInt(et_balance_petty.getText().toString());
                amreceived = TextUtils.isEmpty(et_received_petty.getText().toString()) ? 0 : Integer.parseInt(et_received_petty.getText().toString());

                String amcalculation = String.valueOf(amreceived - ampaid);
                et_balance_petty.setText(amcalculation);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        et_received_petty.addTextChangedListener(autoAddTextWatcher);
        et_paid_petty.addTextChangedListener(autoAddTextWatcher);
    }

    public void SaveToServer() {
        progressDialog.show();
        final String descrip = et_description_petty.getText().toString();
        final String recv = et_received_petty.getText().toString();
        final String paid = et_paid_petty.getText().toString();
        final String blance = et_balance_petty.getText().toString();
        HashMap<String, String> param = new HashMap<>();

        Date d = new Date();
        String Date = String.valueOf(DateFormat.format("MM-dd-yyyy ", d.getTime()));


        SharedPreferences preferences = getSharedPreferences("IdManager", 0);
        param.put("PKProjectId", preferences.getString("project_id", null));
        param.put("PKEmployeeId", preferences.getString("emp_id", null));
        param.put("PKWorkId", MyPrecfence.getActiveInstance(PettyCash.this).getWorkID());


        Log.d("pppppp", MyPrecfence.getActiveInstance(PettyCash.this).getProjectId());
        Log.d("eeeeeeeeee", MyPrecfence.getActiveInstance(PettyCash.this).getPKEmployeeId());
        Log.d("wwwwwww", MyPrecfence.getActiveInstance(PettyCash.this).getWorkID());
        param.put("Description", descrip);
        param.put("Received", recv);
        param.put("Paid", paid);
        param.put("Balance", blance);
        param.put("Date", Date);
        Log.v("pretty", recv);
        server.requestServer(Utils.PettyCashUrl, param, "", new ServerLis() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    PKPettyCashId = jsonObject.getString("PKPettyCashId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.v("prettycash", response);
                layout.setVisibility(View.GONE);
                btn_save_pretty.setVisibility(View.GONE);
                arrayList.add(new Pretty_cash(PKPettyCashId, descrip, recv, paid, blance));
                adapter.notifyDataSetChanged();
                clear();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }


    @Override
    public void onSuccess(String response) {

    }

    @Override
    public void onError(VolleyError error) {

    }

    public void clear() {
        et_description_petty.setText("");
        et_balance_petty.setText("");
        et_received_petty.setText("");
        et_paid_petty.setText("");
    }
}
