package software.starling.com.officeapp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.List;
import java.util.Map;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.activity.WorkDoneManPower;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.MySave;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.W_done_m;

/**
 * Created by Harsh on 4/19/2018.
 */

public class WorkDone_mPower extends RecyclerView.Adapter<WorkDone_mPower.MyViewHolder> implements ServerLis, getresponce {
    ArrayList<W_done_m> arrayList;
    Context context;
    String skilledNos;
    String skilledPayment;
    String unskilledNos;
    String unskilledPayment;
    String agencyy;
    String amountt;
    String workname;
    String spinner_value, workid, PKAgencyId, spiner_pos;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<String> list1 = new ArrayList<>();
    ArrayAdapter<String> adapter1;
    Server server;
    Helper helper;
    ProgressDialog progressDialog;

    Spinner spinner_agency;
    long un, up, sn, sp, tams, tamu;


    public WorkDone_mPower(Context context, ArrayList<W_done_m> arrayList) {
        this.arrayList = arrayList;
        this.context = context;

        server = new Server((Activity) context);
        server.registerListener((ServerLis) context);
        server.getRequest(Utils.AgenciesUrl);
        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Loading please wait");
        helper = new Helper();
        helper.registerListener((getresponce) context);


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_done_item, parent, false);


        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final W_done_m list = arrayList.get(position);


        holder.spinner_workname_adap.setText(list.getWork_Name());
        holder.Ski_no.setText(list.getSki_No());
        holder.Ski_paymnet.setText(list.getSki_Payment());
        holder.unski_no.setText(list.getU_ski_No());
        holder.unski_pay.setText(list.getUn_Ski_payment());
        holder.agency.setText(list.getAgency());
        holder.amount.setText(list.getAmount());

        /*
         * */
        holder.unski_pay.setEnabled(false);
        holder.spinner_workname_adap.setEnabled(false);
        holder.Ski_no.setEnabled(false);
        holder.Ski_paymnet.setEnabled(false);
        holder.unski_no.setEnabled(false);
        holder.agency.setEnabled(false);
        holder.amount.setEnabled(false);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.unski_pay.setEnabled(true);
                holder.spinner_workname_adap.setEnabled(true);
                holder.Ski_no.setEnabled(true);
                holder.Ski_paymnet.setEnabled(true);
                holder.unski_no.setEnabled(true);
                holder.agency.setEnabled(true);
                holder.amount.setEnabled(false);
                holder.spinner_workname_adap.setVisibility(View.GONE);
                holder.spinner_workname_adap1.setVisibility(View.VISIBLE);

                holder.agency.setVisibility(View.GONE);
                holder.spinner_agency.setVisibility(View.VISIBLE);

                holder.setspinner();
                holder.getAgencyValue();


                holder.button.setVisibility(View.GONE);
                holder.button1.setVisibility(View.VISIBLE);

            }
        });
        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.v("checkidresponce",id);
                String d = list.getPKDailyManWorkId();
                // Helper.ShowToast(context, d);
                holder.save(d);
                holder.button.setVisibility(View.VISIBLE);
                holder.button1.setVisibility(View.GONE);

                holder.spinner_workname_adap.setVisibility(View.VISIBLE);
                holder.spinner_workname_adap1.setVisibility(View.GONE);

                holder.agency.setVisibility(View.VISIBLE);
                holder.spinner_agency.setVisibility(View.GONE);

                holder.unski_pay.setEnabled(false);
                holder.spinner_workname_adap.setEnabled(false);
                holder.Ski_no.setEnabled(false);
                holder.Ski_paymnet.setEnabled(false);
                holder.unski_no.setEnabled(false);
                holder.agency.setEnabled(false);
                holder.amount.setEnabled(false);

            }
        });


    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    @Override
    public void onSuccess(String response) {
        Log.v("df", response);
        progressDialog.dismiss();
        list1.add("Choose Value ");
        String Data = "Data";
        helper.getObject(response, Data);


    }

    @Override
    public void onError(VolleyError error) {
        // Log.v("error",);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText Ski_no, Ski_paymnet, unski_no, unski_pay, agency, spinner_workname_adap;
        Button button, button1;
        Spinner spinner_workname_adap1, spinner_agency;
        TextView amount;


        public MyViewHolder(View itemView) {
            super(itemView);
            spinner_workname_adap = itemView.findViewById(R.id.spinner_workname_adap);
            spinner_workname_adap1 = itemView.findViewById(R.id.spinner_workname_adap1);
            Ski_no = itemView.findViewById(R.id.et_s_nos_adap);
            Ski_paymnet = itemView.findViewById(R.id.et_s_payment_adap);
            unski_no = itemView.findViewById(R.id.et_us_nos_adap);
            unski_pay = itemView.findViewById(R.id.et_us_payment_adap);
            agency = itemView.findViewById(R.id.et_agency_adap);
            spinner_agency = itemView.findViewById(R.id.et_agency_adap1);
            amount = itemView.findViewById(R.id.et_amount_adap);
            button = itemView.findViewById(R.id.btn);
            button1 = itemView.findViewById(R.id.btn_save1);

            adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list1);
            spinner_workname_adap1.setAdapter(adapter);
            ArrayList<String> strings = new ArrayList<>();

            adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, strings);
            spinner_agency.setAdapter(adapter1);

           /* progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait");*/

            spinner_workname_adap1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinner_value = String.valueOf(spinner_workname_adap1.getItemAtPosition(position));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner_agency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spiner_pos = String.valueOf(spinner_agency.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            String s = MyPrecfence.getActiveInstance(context).getAgecny();
            Log.d("wdmpdataresponce", "" + s);
            if (s != null) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("Data");
                    int i;
                    for (i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        PKAgencyId = object1.getString("PKAgencyId");
                        String d = object1.getString("AgencyName");

                        strings.add(d);


                    }
                    adapter1.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            TextWatcher autoAddTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sn = TextUtils.isEmpty(Ski_no.getText().toString()) ? 0 : Integer.parseInt(Ski_no.getText().toString());
                    sp = TextUtils.isEmpty(Ski_paymnet.getText().toString()) ? 0 : Integer.parseInt(Ski_paymnet.getText().toString());
                    un = TextUtils.isEmpty(unski_no.getText().toString()) ? 0 : Integer.parseInt(unski_no.getText().toString());
                    up = TextUtils.isEmpty(unski_pay.getText().toString()) ? 0 : Integer.parseInt(unski_pay.getText().toString());

                    String amcalculation = String.valueOf(sn * sp);
                    tams = Integer.parseInt(amcalculation);

                    String duecalculation = String.valueOf(un * up);
                    tamu = Integer.parseInt(duecalculation);

                    String total = String.valueOf(tams + tamu);
                    amount.setText(total);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            Ski_no.addTextChangedListener(autoAddTextWatcher);
            Ski_paymnet.addTextChangedListener(autoAddTextWatcher);
            unski_no.addTextChangedListener(autoAddTextWatcher);
            unski_pay.addTextChangedListener(autoAddTextWatcher);

        }

        public void save(final String id) {


            workname = spinner_workname_adap.getText().toString();
            skilledNos = Ski_no.getText().toString();
            skilledPayment = Ski_paymnet.getText().toString();
            unskilledNos = unski_no.getText().toString();
            unskilledPayment = unski_pay.getText().toString();
            amountt = amount.getText().toString();
            agencyy = agency.getText().toString();

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://officeapp.starlingsoftwares.com/api/DailyManWork", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // progressDialog.show();
                    Log.v("wdmpadap", response);


                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.v("wdmpadap1", response);
                        if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                            //  progressDialog.dismiss();


                            Toast.makeText(context, "Data Successfully Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Fill All the Field First", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.v("reuu", error.toString());
                    //  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {

                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();

                    SharedPreferences preferences = context.getSharedPreferences("IdManager", 0);
                    param.put("PKProjectId", preferences.getString("project_id", null));
                    param.put("EmpId", preferences.getString("emp_id", null));


                    param.put("PKWorkId", MyPrecfence.getActiveInstance(context).getWorkID());
                    param.put("SkilledNumber", skilledNos);
                    param.put("SkilledPayment", skilledPayment);
                    param.put("UnskilledNumber", unskilledNos);
                    param.put("UnskilledPayment", unskilledPayment);
                    param.put("Agency", PKAgencyId);

                    param.put("PKDailyManWorkId", id);


                    Log.v("aggency", agencyy);
                    param.put("Amount", amountt);
                    Date d = new Date();
                    String seq = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
                    param.put("Date", seq);


                    Log.v("rrrrrrrr", MyPrecfence.getActiveInstance(context).getWorkID());

                    return param;


                }


            };
            int TimeOut = 30000;
            RetryPolicy retryPolicy = new DefaultRetryPolicy(TimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(stringRequest);

        }

        public void setspinner() {
              progressDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://officeapp.starlingsoftwares.com/api/Works", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //  list.add("Choose value");
                    progressDialog.dismiss();

                    Log.v("responce", response);
                    try {

                        JSONObject object1 = new JSONObject(response);
                        JSONArray jsonArray = object1.getJSONArray("Data");

                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject object = jsonArray.getJSONObject(i);
                            //  progressDialog.dismiss();
                            Log.v("harsh", response);
                            //String wn = object.getString("WorkName");
                            workid = object.getString("PKWorkId");
                            MyPrecfence.getActiveInstance(context).setPWorkID(workid);

                            list1.add(object.getString("WorkName"));


                        }
                        adapter.notifyDataSetChanged();

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
            int TimeOut = 30000;
            RetryPolicy retryPolicy = new DefaultRetryPolicy(TimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(stringRequest);
        }


        public void getAgencyValue() {


        }


    }

    @Override
    public void getJson(JSONObject object) throws JSONException {
        Log.v("gsgsgsgsg", "" + object);
        PKAgencyId = object.getString("PKAgencyId");


        // list.add(object.getString("AgencyName"));
        Log.v("responce", object.getString("AgencyName"));


        adapter1.notifyDataSetChanged();

    }


}
