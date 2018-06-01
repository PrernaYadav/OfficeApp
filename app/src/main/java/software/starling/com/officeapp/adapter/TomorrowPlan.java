package software.starling.com.officeapp.adapter;

import android.app.Activity;
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

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.activity.TomorrowsPlanning;
import software.starling.com.officeapp.activity.WorkDoneMachine;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.Tomorrow;

/**
 * Created by Harsh on 4/24/2018.
 */

public class TomorrowPlan extends RecyclerView.Adapter<TomorrowPlan.MyViewHolder> implements ServerLis,getresponce {
    ArrayList<Tomorrow> arrayList;
    Context context;
    String plan_work;
    String m_Type;
    String m_Amount;
    String manpower;
    String mayterial;
    String fund;
    String f_Type;
    String f_amount;
    String SpinnerId;
    Server server;
    ArrayAdapter<String> spin_adap;
    ArrayList<String> matlist = new ArrayList<>();
    String spin_pos;
    Helper helper;
    String PKMaterialBoughtOutId;

    public TomorrowPlan(Context context, ArrayList<Tomorrow> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        server = new Server((Activity) context);


        server.registerListener((ServerLis) context);

        helper = new Helper();

        server = new Server((Activity) context);
        helper.registerListener((getresponce)this);
        server.getRequest("http://officeapp.starlingsoftwares.com/api/MaterialBoughtOut");
        server.registerListener((ServerLis) this);


    }

    @Override
    public TomorrowPlan.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tomorrow_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TomorrowPlan.MyViewHolder holder, int position) {
        final Tomorrow tomorrow = arrayList.get(position);
        holder.et_planned_work.setText(tomorrow.getPlanWork());
        holder.et_machnery_type.setText(tomorrow.getM_Type());
        holder.et_machnery_amount.setText(tomorrow.getM_Amount());
        holder.et_manpower.setText(tomorrow.getManPower());
        holder.et_material.setText(tomorrow.getMatterial());
        holder.et_fund.setText(tomorrow.getFund());
        holder.et_frf_amount.setText(tomorrow.getF_amount());
        holder.et_frf_type.setText(tomorrow.getF_Type());


        holder.et_planned_work.setEnabled(false);
        holder.et_machnery_type.setEnabled(false);
        holder.et_machnery_amount.setEnabled(false);
        holder.et_manpower.setEnabled(false);
        holder.et_material.setEnabled(false);
        holder.et_fund.setEnabled(false);
        holder.et_frf_amount.setEnabled(false);
        holder.et_frf_type.setEnabled(false);
        holder.btn_tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id=tomorrow.getPKTomorrowsPlanningId();
              //  Toast.makeText(context, ""+id, Toast.LENGTH_SHORT).show();

                holder.et_planned_work.setEnabled(true);
                holder.et_machnery_type.setEnabled(true);
                holder.et_machnery_amount.setEnabled(true);
                holder.et_manpower.setEnabled(true);
                holder.et_material.setEnabled(true);
                holder.et_fund.setEnabled(true);
                holder.et_frf_amount.setEnabled(true);
                holder.et_frf_type.setEnabled(true);

                holder.et_material.setVisibility(View.GONE);
                holder.spinner_mat.setVisibility(View.VISIBLE);


                holder.btn_tl.setVisibility(View.GONE);
                holder.btn_tl1.setVisibility(View.VISIBLE);
            }
        });
        holder.btn_tl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id=tomorrow.getPKTomorrowsPlanningId();
             //   Toast.makeText(context, ""+id, Toast.LENGTH_SHORT).show();

                holder.et_planned_work.setEnabled(false);
                holder.et_machnery_type.setEnabled(false);
                holder.et_machnery_amount.setEnabled(false);
                holder.et_manpower.setEnabled(false);
                holder.et_material.setEnabled(false);
                holder.et_fund.setEnabled(false);
                holder.et_frf_amount.setEnabled(false);
                holder.et_frf_type.setEnabled(false);
                holder.SaveToServer(id);
                holder.btn_tl.setVisibility(View.VISIBLE);
                holder.btn_tl1.setVisibility(View.GONE);

                holder.et_material.setVisibility(View.VISIBLE);
                holder.spinner_mat.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void getJson(JSONObject object) throws JSONException {
        Log.v("respooo", "" + object);
        PKMaterialBoughtOutId = object.getString("PKMaterialBoughtOutId");
        //  strings.add(object.getString("Name"));


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText et_planned_work, et_machnery_type, et_machnery_amount, et_manpower, et_fund, et_frf_type, et_frf_amount;
        Button btn_tl, btn_tl1;
        TextView et_material;
        Spinner spinner_mat;

        public MyViewHolder(View itemView) {
            super(itemView);
            et_planned_work = itemView.findViewById(R.id.et_planned_work_adap);
            et_machnery_type = itemView.findViewById(R.id.et_machnery_type_adap);
            et_machnery_amount = itemView.findViewById(R.id.et_machnery_amount_adap);
            et_manpower = itemView.findViewById(R.id.et_manpower_adap);
            et_material = itemView.findViewById(R.id.et_material_adap);
            et_fund = itemView.findViewById(R.id.et_fund_adap);
            spinner_mat=itemView.findViewById(R.id.spin_mat_adap);
            et_frf_type = itemView.findViewById(R.id.et_frf_type_adap);
            et_frf_amount = itemView.findViewById(R.id.et_frf_amount_adap);
            btn_tl = itemView.findViewById(R.id.btn_tl);
            btn_tl1 = itemView.findViewById(R.id.btn_tl1);



            spin_adap = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, matlist);
            spinner_mat.setAdapter(spin_adap);
            spinner_mat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spin_pos = String.valueOf(spinner_mat.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            String spi_val = MyPrecfence.getActiveInstance(context).getMatterialBought();


        }

        public void SaveToServer(String id) {
            Date d = new Date();
           plan_work = et_planned_work.getText().toString();
            m_Type = et_machnery_type.getText().toString();
            m_Amount = et_machnery_amount.getText().toString();
           manpower = et_manpower.getText().toString();
            mayterial = et_material.getText().toString();
           fund = et_fund.getText().toString();
           f_Type = et_frf_type.getText().toString();
           f_amount = et_frf_amount.getText().toString();
            String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
            final HashMap<String, String> param = new HashMap<>();
            param.put("PKWorkId", MyPrecfence.getActiveInstance(context).getWorkID());


            SharedPreferences preferences = context.getSharedPreferences("IdManager", 0);
            param.put("PKProjectId", preferences.getString("project_id", null));
            param.put("PKEmployeeId", preferences.getString("emp_id", null));


            param.put("PlannedWork", plan_work);
            param.put("MachineryType", m_Type);
            param.put("MachineryAmount", m_Amount);
            param.put("Manpower", manpower);
            param.put("MaterialBoughtOutId", mayterial);
            param.put("Fund", fund);
            param.put("FundRequiredFor", f_Type);
            param.put("RequiredAmount", f_amount);
            param.put("PKTomorrowsPlanningId",id);
            param.put("Date", Date);
            Log.v("ohooo", plan_work);

            server.requestServer(Utils.TomorrowsPlanningUrl, param, "", new ServerLis() {
                @Override
                public void onSuccess(String response) {
                    Log.v("tomarrow", response);
                    Helper.ShowToast(context,"Data Saved");

                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        }
    }
    @Override
    public void onSuccess(String response) {

        //  matlist.add("Choose Value");
        Log.v("bought", response);
        String Data = "Data";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("Data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                // Log.v("dsfsdfsdfs",""+spi_val);
                SpinnerId = object.getString("PKMaterialBoughtOutId");
                matlist.add(object.getString("Name"));
            }
//            spin_adap.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
