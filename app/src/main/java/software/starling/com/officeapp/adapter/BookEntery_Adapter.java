package software.starling.com.officeapp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.activity.MeasurementBookEntry;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.model.BookEntery;

/**
 * Created by Admin on 4/24/2018.
 */

public class BookEntery_Adapter extends RecyclerView.Adapter<BookEntery_Adapter.MyViewHolder> implements ServerLis {
    ArrayList<BookEntery> arrayList;
    long length, bridth, height;
    Server server;
    Context context;
    //ArrayAdapter adapter;
    ProgressDialog progressDialog;
    //Spinner spinner;
    String spinner_pos, PKUnitId;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> ulist = new ArrayList<>();

    public BookEntery_Adapter(Context context, ArrayList<BookEntery> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        server = new Server((Activity) context);
        server.registerListener((ServerLis) context);
    }

    @Override
    public BookEntery_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_entry_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookEntery_Adapter.MyViewHolder holder, int position) {
        final BookEntery entery = arrayList.get(position);
        holder.et_doi.setText(entery.getDesription());
        holder.tv_unit.setText(entery.getSpinner_pos());
        holder.et_nos.setText(entery.getNos());
        holder.et_l.setText(entery.getMesuureL());
        holder.et_b.setText(entery.getMesuremnetB());
        holder.et_h.setText(entery.getMessuremnetH());
        holder.et_qtys.setText(entery.getQues());


        holder.et_doi.setEnabled(false);
        holder.et_nos.setEnabled(false);
        holder.tv_unit.setEnabled(false);
        holder.et_l.setEnabled(false);
        holder.et_b.setEnabled(false);
        holder.et_h.setEnabled(false);
        holder.et_qtys.setEnabled(false);

        holder.btn_be.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.setSpinner();
                holder.et_doi.setEnabled(true);
                holder.et_nos.setEnabled(true);
                holder.tv_unit.setEnabled(true);
                holder.et_l.setEnabled(true);
                holder.et_b.setEnabled(true);
                holder.et_h.setEnabled(true);
                holder.et_qtys.setEnabled(true);

                holder.btn_be.setVisibility(View.GONE);
                holder.btn_be1.setVisibility(View.VISIBLE);

                holder.tv_unit.setVisibility(View.GONE);
                holder.spinner_mbe.setVisibility(View.VISIBLE);

                String mid = entery.getPKMeasurementBookId();
               // Helper.ShowToast(context, mid);

            }
        });
        holder.btn_be1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.et_doi.setEnabled(false);
                holder.et_nos.setEnabled(false);
                holder.tv_unit.setEnabled(false);
                holder.et_l.setEnabled(false);
                holder.et_b.setEnabled(false);
                holder.et_h.setEnabled(false);
                holder.et_qtys.setEnabled(false);

                String mid = entery.getPKMeasurementBookId();
               // Helper.ShowToast(context, mid);

                holder.tv_unit.setVisibility(View.VISIBLE);
                holder.spinner_mbe.setVisibility(View.GONE);


                holder.btn_be.setVisibility(View.VISIBLE);
                holder.btn_be1.setVisibility(View.GONE);


             /*   progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Wait For Server");*/

                holder.savedata(mid);


            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onSuccess(String response) {

    }

    @Override
    public void onError(VolleyError error) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText et_doi, et_nos, et_l, et_b, et_h;
        TextView et_qtys;
        Spinner spinner_mbe;
        TextView tv_unit;
        Button btn_be, btn_be1;

        public MyViewHolder(View itemView) {
            super(itemView);
            et_doi = itemView.findViewById(R.id.et_doi_adap);
            et_nos = itemView.findViewById(R.id.et_nos_adap);
            et_l = itemView.findViewById(R.id.et_l_adap);
            spinner_mbe = itemView.findViewById(R.id.spinner_mbe_adap);
            et_b = itemView.findViewById(R.id.et_b_adap);
            et_h = itemView.findViewById(R.id.et_h_adap);
            et_qtys = itemView.findViewById(R.id.et_qtys_adap);
            btn_be = itemView.findViewById(R.id.btn_be);
            btn_be1 = itemView.findViewById(R.id.btn_be1);
            tv_unit = itemView.findViewById(R.id.tv_unit);


            arrayAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, ulist);
            spinner_mbe.setAdapter(arrayAdapter);
            spinner_mbe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinner_pos = String.valueOf(spinner_mbe.getItemAtPosition(position));
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
        }

        public void savedata(String mid) {
           // BookEntery bookEntery=new BookEntery();

            final String description = et_doi.getText().toString();
            final String nos = et_nos.getText().toString();
           // final String unitId=spinner_mbe.getSpinner_pos();
            final String l = et_l.getText().toString();
            final String b = et_b.getText().toString();
            final String h = et_h.getText().toString();
            final String qtys = et_qtys.getText().toString();
            Date d = new Date();
            String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
            final HashMap<String, String> param = new HashMap<>();
          //  param.put("PKWorkId", MyPrecfence.getActiveInstance(context).getWorkID());
           /* SharedPreferences preferences = context.getSharedPreferences("IdManager", 0);
            param.put("PKProjectId", preferences.getString("project_id", null));
            param.put("PKEmployeeId", preferences.getString("emp_id", null));
            param.put("Item", description);
            param.put("Unit", PKUnitId);
            param.put("Length", l);
            param.put("Number",nos);
            param.put("Breadth", b);
            param.put("Height", h);
            param.put("Quantity", qtys);
            param.put("Date", Date);
            param.put("Latitude", "1212");
            param.put("Longitude", "1213");
*/

            param.put("PKWorkId", MyPrecfence.getActiveInstance(context).getWorkID());
            SharedPreferences preferences = context.getSharedPreferences("IdManager", 0);
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
            param.put("Location","Gurgaon");
            param.put("PKMeasurementBookId",mid);


            Log.v("hey",""+param);


            Log.v("des", description);
            Log.v("spinnnnnnnnnnner",PKUnitId);
         //   Toast.makeText(context, ""+PKUnitId, Toast.LENGTH_SHORT).show();
            Log.v("number", nos);
            Log.v("length", l);
            Log.v("bridth", b);
            Log.v("height", h);
            Log.v("tarikh", Date);
            server.requestServer(Utils.MeasurementBookUrl, param, "", new ServerLis() {
                @Override
                public void onSuccess(String response) {
                    //progressDialog.dismiss();
                    Log.v("mberesponse", response);
                    Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(VolleyError error) {
                    Log.v("ohhhno", error.toString());
                }
            });
        }

        private void setSpinner() {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://officeappapi.starlingsoftwares.com/api/Unit", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
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
                            arrayAdapter.notifyDataSetChanged();


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
            int TimeOut = 30000;
            RetryPolicy retryPolicy = new DefaultRetryPolicy(TimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(stringRequest);
        }


    }
}

