package software.starling.com.officeapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.adapter.Hidrance_Adapter;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.model.Hidrance;

public class HindranceAndInternalProblem extends AppCompatActivity implements ServerLis {
    EditText et_hindrance_from_custumer, et_internal_issue;

    Button btn_save_hindranceproblem, btn_next_hindranceproblem;
    Context context;
    Hidrance_Adapter adapter;
    ArrayList<Hidrance> arrayList;
    RecyclerView recyclerView;
    TextView Add_Item;
    LinearLayout layout;
    Server server;
    ServerLis serverLis;
    ProgressDialog progressDialog;
    String PKProblemsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hindrance_and_internal_problem);
        context = HindranceAndInternalProblem.this;
        arrayList = new ArrayList<>();
        server = new Server(this);
        server.registerListener((ServerLis) this);
        et_internal_issue = findViewById(R.id.et_internal_issue);
        et_hindrance_from_custumer = findViewById(R.id.et_hindrance_from_custumer);
        btn_save_hindranceproblem = findViewById(R.id.btn_save_hindranceproblem);
        btn_next_hindranceproblem = findViewById(R.id.btn_next_hindranceproblem);
        Add_Item = findViewById(R.id.add_item_hidrance);
        layout = findViewById(R.id.layout_hidrance);
        recyclerView = findViewById(R.id.recyclerview_hidrance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Hidrance_Adapter(context, arrayList);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


        btn_next_hindranceproblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MeasurementBookEntry.class));
            }
        });
        btn_save_hindranceproblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hirdrance = et_hindrance_from_custumer.getText().toString();
                String internal = et_internal_issue.getText().toString();
                if (hirdrance.isEmpty() || internal.isEmpty()) {
                    Helper.ShowToast(context, "fill Complete form");
                } else {
                    SaveToServer();
                    progressDialog.show();
                }
            }
        });
        Add_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
                btn_save_hindranceproblem.setVisibility(View.VISIBLE);
            }
        });

    }

    public void SaveToServer() {
        Date d = new Date();
        final String hirdrance = et_hindrance_from_custumer.getText().toString();
        final String internal = et_internal_issue.getText().toString();

        String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
        final HashMap<String, String> param = new HashMap<>();
        param.put("PKWorkId", MyPrecfence.getActiveInstance(HindranceAndInternalProblem.this).getWorkID());
        SharedPreferences preferences = getSharedPreferences("IdManager", 0);
        param.put("PKProjectId", preferences.getString("project_id", null));
        param.put("PKEmployeeId", preferences.getString("emp_id", null));
        param.put("FromCustomer", hirdrance);
        param.put("InternalProblem", internal);
        param.put("Date", Date);


      /*  btn_save_hindranceproblem.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        arrayList.add(new Hidrance(hirdrance, internal));
        adapter.notifyDataSetChanged();
        clear();*/

        server.requestServer(Utils.EmployeeProblemsURL, param, "", new ServerLis() {
            @Override
            public void onSuccess(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Helper.ShowToast(HindranceAndInternalProblem.this, "Data Successfully Saved");
                    PKProblemsId = jsonObject.getString("PKProblemsId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  progressDialog.dismiss();
                Log.v("respoo", response);
                btn_save_hindranceproblem.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                arrayList.add(new Hidrance(PKProblemsId, hirdrance, internal));
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
        et_hindrance_from_custumer.setText("");
        et_internal_issue.setText("");
    }
}
