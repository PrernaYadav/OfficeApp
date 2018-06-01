package software.starling.com.officeapp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import software.starling.com.officeapp.R;
import software.starling.com.officeapp.activity.MaterialReceived;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.Matterial;

/**
 * Created by Admin on 4/23/2018.
 */

public class Material_Recived extends RecyclerView.Adapter<Material_Recived.MyViewHolder> implements ServerLis, getresponce {

    ArrayList<Matterial> arrayList;
    Context context;
    Server server;
    Helper helper;
    String PKMaterialAIPLId;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> strings = new ArrayList<>();
    String Spinnerposition;
    String Spinnerposition1;

    ArrayAdapter<String> arrayAdapter1;
    ArrayAdapter<String> arrayAdapter3;
    ArrayList<String> strings1 = new ArrayList<>();
    ArrayList<String> strings2 = new ArrayList<>();
    String spinnerId;
    String unitnameId;
    ProgressDialog progressDialog;


    public Material_Recived(Context context, ArrayList<Matterial> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        server = new Server((Activity) context);
        server.registerListener((ServerLis) context);

        server.getRequest("http://officeapp.starlingsoftwares.com/api/MaterialAIPL");
        helper = new Helper();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matterial_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Matterial matterial = arrayList.get(position);
        holder.materiall.setText(matterial.getMaterial());
        holder.quantity.setText(matterial.getQuantity());
        holder.tvUnit.setText(matterial.getUnit());
        // holder.et_amount.setText(matterial.getAmount());


        holder.materiall.setEnabled(false);
        holder.quantity.setEnabled(false);


        //  holder.et_amount.setEnabled(false);
        holder.btn_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Wait Please");
                progressDialog.show();*/


                String mid = matterial.getPKMatReceivedId();
                //    Helper.ShowToast(context, mid);

                holder.materiall.setEnabled(true);
                holder.quantity.setEnabled(true);
                holder.tvUnit.setEnabled(true);
                holder.getSpinner();
                holder.setSpinner();


                holder.btn_mr.setVisibility(View.GONE);
                holder.btn_mr1.setVisibility(View.VISIBLE);
                holder.tvUnit.setVisibility(View.GONE);

                holder.materiall.setVisibility(View.GONE);
                holder.spinner.setVisibility(View.VISIBLE);
                holder.spinner1.setVisibility(View.VISIBLE);

            }
        });
        holder.btn_mr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mid = matterial.getPKMatReceivedId();
                //   Helper.ShowToast(context, mid);
                holder.btn_mr1.setVisibility(View.GONE);
                holder.btn_mr.setVisibility(View.VISIBLE);
                holder.save(mid);
                holder.materiall.setVisibility(View.VISIBLE);
                holder.tvUnit.setVisibility(View.VISIBLE);
                holder.spinner.setVisibility(View.GONE);
                holder.spinner1.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onSuccess(String response) {
        MyPrecfence.getActiveInstance(context).getMatterial();
        // progressDialog.dismiss();
        strings.add("Choose Value");
        String Data = "Data";
        helper.getObject(response, Data);
        Log.v("sppppp", response);

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_material_recevd)
        TextView materiall;
        @BindView(R.id.spinner_material_received_adap1)
        Spinner spinner;
        @BindView(R.id.et_material_received_quantity_adap)
        EditText quantity;
        @BindView(R.id.spinner_material_received_unit)
        Spinner spinner1;
        @BindView(R.id.btn_mr)
        Button btn_mr;
        @BindView(R.id.btn_mr1)
        Button btn_mr1;
        @BindView(R.id.tv_unit_adap)
        TextView tvUnit;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            getData();
            arrayAdapter3 = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, strings2);
            spinner1.setAdapter(arrayAdapter3);
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Spinnerposition1 = (String) spinner1.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


        public void save(String id) {
            HashMap<String, String> param = new HashMap<>();
            Date d = new Date();
            final String et_quantity = quantity.getText().toString();
            String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));

            SharedPreferences preferences = context.getSharedPreferences("IdManager", 0);
            param.put("PKProjectId", preferences.getString("project_id", null));
            param.put("PKEmployeeId", preferences.getString("emp_id", null));
            param.put("PKMatReceivedId", id);
            param.put("Date", Date);
            param.put("PKMaterialAIPLId", spinnerId);
//            Log.v("changeamt",spinnerId);
            param.put("Quantity", et_quantity);
            param.put("PKUnitId", MyPrecfence.getActiveInstance(context).getPKUnitId());


            Log.v("A1", "" + param);

            //Log.v("A1",);


            server.requestServer(Utils.MaterialReceived_Save, param, "", new ServerLis() {
                @Override
                public void onSuccess(String response) {
                    Log.v("mradap", response);
                    Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(VolleyError error) {
                    Log.v("rrrrrrrrr", String.valueOf(error));

                }
            });


        }
        public void getData() {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.GetUnitUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("prerna", response);
                    try {

                        JSONObject object1 = new JSONObject(response);
                        JSONArray jsonArray = object1.getJSONArray("Data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.v("harsh", response);
                            //String wn = object.getString("WorkName");

                            String PKUnitId = object.getString("PKUnitId");
                            MyPrecfence.getActiveInstance(context).setPKUnitId(PKUnitId);

                            strings2.add(object.getString("UnitName"));
                        }
                        arrayAdapter3.notifyDataSetChanged();
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
        public void getSpinner() {
            arrayAdapter1 = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, strings1);
            spinner.setAdapter(arrayAdapter1);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Spinnerposition = (String) spinner.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            String spi_value = MyPrecfence.getActiveInstance(context).getMatterial();

            try {
                JSONObject jsonObject = new JSONObject(spi_value);
                JSONArray array = jsonObject.getJSONArray("Data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    spinnerId = object.getString("PKMaterialAIPLId");
                    strings1.add(object.getString("Name"));
                }
                arrayAdapter1.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        public void setSpinner() {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.MaterialBoughtOutUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("materrrrrialll", response);
//                    progressDialog.dismiss();
                    try {

                        JSONObject object1 = new JSONObject(response);
                        JSONArray jsonArray = object1.getJSONArray("Data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.v("harsh", response);
                            //String wn = object.getString("WorkName");

                            unitnameId = object.getString("PKMaterialBoughtOutId");
                            MyPrecfence.getActiveInstance(context).setunitReceived(unitnameId);

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
    }
}
