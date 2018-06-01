package software.starling.com.officeapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import software.starling.com.officeapp.R;
import software.starling.com.officeapp.activity.WorkDoneMachine;
import software.starling.com.officeapp.activity.WorkDoneManPower;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.W_D_Machine;
import software.starling.com.officeapp.model.W_done_m;

/**
 * Created by Admin on 4/21/2018.
 */

public class W_Done_Machine extends RecyclerView.Adapter<W_Done_Machine.MyViewHolder> implements ServerLis, getresponce {
    ArrayList<W_D_Machine> arrayList;
    Context context;
    String des, due, amount, rate, hour, paid, SpinnerPosition, PKWorkMachineId,PKDailyMachineWorkId;
    long houra, ratea, amounta, paida, dueamounta;
    Server server;
    Helper helper;
    ArrayList<String> machineList=new ArrayList<>();
    ArrayAdapter<String> machineAdapter;

    public W_Done_Machine(Context context, ArrayList<W_D_Machine> arrayList) {
        this.arrayList = arrayList;
        this.context = context;

        server = new Server((Activity) context);
        server.registerListener((ServerLis) context);
        server.getRequest("http://officeapp.starlingsoftwares.com/api/WorkMachine");

        helper = new Helper();
        helper.registerListener((getresponce) context);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.w_done_machine, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final W_D_Machine machine = arrayList.get(position);

/*
        final W_Done_Machine list = arrayList.get(position);
*/
        holder.textView_descr.setText(machine.getDescription());
        holder.textView_hour.setText(machine.getHours());
        holder.textView_rate.setText(machine.getRate());
        holder.textView_amount.setText(machine.getAmount());
        holder.textView_paid.setText(machine.getPaid());
        holder.textView_dueAmount.setText(machine.getDue_amount());

        holder.textView_descr.setEnabled(false);
        holder.textView_hour.setEnabled(false);
        holder.textView_rate.setEnabled(false);
        holder.textView_amount.setEnabled(false);
        holder.textView_paid.setEnabled(false);
        holder.textView_dueAmount.setEnabled(false);
        // holder.amount.setEnabled(false);
        holder.btn_wdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.setSpinnerValue();
                String s = MyPrecfence.getActiveInstance(context).getPKDailyManWorkId();
                machineAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, machineList);
                holder.spinner_machiene.setAdapter(machineAdapter);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("Data");
                    int i;
                    machineList.add("Choose Value");
                    for (i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String MachineName = object.getString("MachineName");
                        //  Log.v("machinename", "" + object);
                        PKWorkMachineId = object.getString("PKWorkMachineId");
                        machineList.add(MachineName);



                    }
                    machineAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                holder.textView_descr.setEnabled(true);
                holder.textView_hour.setEnabled(true);
                holder.textView_rate.setEnabled(true);
                holder.textView_amount.setEnabled(true);
                holder.textView_paid.setEnabled(true);
                holder.textView_dueAmount.setEnabled(true);

                holder.btn_wdm.setVisibility(View.GONE);
                holder.btn_wdm1.setVisibility(View.VISIBLE);

                holder.textView_descr.setVisibility(View.GONE);
                holder.spinner_machiene.setVisibility(View.VISIBLE);
            }
        });
        holder.btn_wdm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mid = machine.getPKDailyMachineWorkId();
            //    Helper.ShowToast(context, mid);


                holder.btn_wdm1.setVisibility(View.GONE);
                holder.btn_wdm.setVisibility(View.VISIBLE);
                holder.save(mid);
                holder.textView_descr.setEnabled(false);
                holder.textView_hour.setEnabled(false);
                holder.textView_rate.setEnabled(false);
                holder.textView_amount.setEnabled(false);
                holder.textView_paid.setEnabled(false);
                holder.textView_dueAmount.setEnabled(false);

                holder.textView_descr.setVisibility(View.VISIBLE);
                holder.spinner_machiene.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void getJson(JSONObject object) throws JSONException {
        //machineList.add(object.getString("MachineName"));
      //  machineAdapter.notifyDataSetChanged();

        Log.v("jebaat", object.getString("MachineName"));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.description_spinner_workDone_M_adap)
        EditText textView_descr;


        @BindView(R.id.et_hours_workDone_M_adap)
        EditText textView_hour;
        @BindView(R.id.et_rate_workDone_M_adap)
        EditText textView_rate;
        @BindView(R.id.et_amount_work_Done_M_adap)
        TextView textView_amount;
        @BindView(R.id.et_paid_workDone_m_adap)
        EditText textView_paid;
        @BindView(R.id.tv_due_amount_workDome_M_adap)
        TextView textView_dueAmount;
        @BindView(R.id.btn_wdm)
        Button btn_wdm;
        @BindView(R.id.btn_wdm1)
        Button btn_wdm1;

        Spinner spinner_machiene;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            spinner_machiene = itemView.findViewById(R.id.description_spinner_workDone_M_adap1);


            TextWatcher autoAddTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    houra = TextUtils.isEmpty(textView_hour.getText().toString()) ? 0 : Integer.parseInt(textView_hour.getText().toString());
                    ratea = TextUtils.isEmpty(textView_rate.getText().toString()) ? 0 : Integer.parseInt(textView_rate.getText().toString());
                    paida = TextUtils.isEmpty(textView_paid.getText().toString()) ? 0 : Integer.parseInt(textView_paid.getText().toString());
                    dueamounta = TextUtils.isEmpty(textView_dueAmount.getText().toString()) ? 0 : Integer.parseInt(textView_dueAmount.getText().toString());

                    String amcalculation = String.valueOf(houra * ratea);
                    textView_amount.setText(amcalculation);

                    amounta = TextUtils.isEmpty(textView_amount.getText().toString()) ? 0 : Integer.parseInt(textView_amount.getText().toString());

                    String duecalculation = String.valueOf(amounta - paida);
                    textView_dueAmount.setText(duecalculation);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            textView_hour.addTextChangedListener(autoAddTextWatcher);
            textView_rate.addTextChangedListener(autoAddTextWatcher);
            textView_paid.addTextChangedListener(autoAddTextWatcher);

        }


        public void setSpinnerValue() {


            spinner_machiene.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SpinnerPosition = String.valueOf(spinner_machiene.getItemAtPosition(position));


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        public void save(final  String mid) {
            server = new Server((Activity) context);
            server.registerListener((ServerLis) context);
            rate = textView_rate.getText().toString();
            amount = textView_amount.getText().toString();
            hour = textView_hour.getText().toString();
            paid = textView_paid.getText().toString();
            des = textView_descr.getText().toString();
            due = textView_dueAmount.getText().toString();



            StringRequest request = new StringRequest(Request.Method.POST, Utils.WorkDoneSave, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("ohhhhhhhhhoh", response);
                    Toast.makeText(context, "Data Successfully Saved", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("ohhhoh", error.toString());
                    Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();



                    SharedPreferences preferences = context.getSharedPreferences("IdManager", 0);
                    param.put("PKProjectId", preferences.getString("project_id", null));
                    param.put("EmpId", preferences.getString("emp_id", null));

                    param.put("PKWorkMachineId", PKWorkMachineId);
                    Log.v("tractor",PKWorkMachineId);
                    param.put("Hours", hour);
                    param.put("Rate", rate);
                    param.put("Amount", amount);
                    param.put("Paid", paid);
                    param.put("DueAmount", due);
                    param.put("PKDailyMachineWorkId",mid);
                    Date d = new Date();
                    String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
                    param.put("Date", Date);
                    return param;
                }
            };
            int TimeOut = 30000;
            RetryPolicy retryPolicy = new DefaultRetryPolicy(TimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(retryPolicy);
            Volley.newRequestQueue(context).add(request);

        }


    }

    @Override
    public void onSuccess(String response) {
        String data = "Data";
        helper.getObject(response, data);
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


    }

    @Override
    public void onError(VolleyError error) {
        Log.v("erroradapspin", "" + error.toString());

    }


}
