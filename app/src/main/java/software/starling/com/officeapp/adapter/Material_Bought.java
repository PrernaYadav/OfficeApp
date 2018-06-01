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

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.activity.MaterialBought;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.constant.getresponce;
import software.starling.com.officeapp.model.M_Bought;

/**
 * Created by Admin on 4/24/2018.
 */

public class Material_Bought extends RecyclerView.Adapter<Material_Bought.MyViewHolder> implements ServerLis,getresponce {
    ArrayList<M_Bought> arrayList;
    long a, b, c;
    ProgressDialog progressDialog;
    Context context;
    Server server;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> strings = new ArrayList<>();
    String Spinner_Pos;
    Helper helper;
    String PKMaterialBoughtOutId;
    String SpinnerId;

    public Material_Bought(Context context, ArrayList<M_Bought> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        server = new Server((Activity) context);
        server.registerListener((ServerLis) context);
      //  context = MaterialBought.this;
     //   server = new Server(this);
        server.registerListener((ServerLis) this);
        server.getRequest(Utils.MaterialBoughtOutUrl);
        helper = new Helper();
        helper.registerListener((getresponce) this);


    }

    @Override
    public Material_Bought.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.materil_bought_item, parent, false);
        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final Material_Bought.MyViewHolder holder, int position) {
        final M_Bought m_bought = arrayList.get(position);
        holder.material.setText(m_bought.getMaterila());
        holder.quantity.setText(m_bought.getQuantity());
        holder.amount.setText(m_bought.getAmount());
        holder.rate.setText(m_bought.getRate());


        holder.material.setEnabled(false);
        holder.quantity.setEnabled(false);
        holder.rate.setEnabled(false);
        holder.amount.setEnabled(false);

        holder.btn_mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.material.setEnabled(true);
                holder.quantity.setEnabled(true);
                holder.rate.setEnabled(true);
                holder.amount.setEnabled(true);

                String mid = m_bought.getPKMaterialBoughtId();
             //   Helper.ShowToast(context, mid);

                holder.spinner_material_bought_adap1.setVisibility(View.VISIBLE);
                holder.material.setVisibility(View.GONE);

                holder.btn_mb.setVisibility(View.GONE);
                holder.btn_mb1.setVisibility(View.VISIBLE);
            }
        });
        holder.btn_mb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.material.setEnabled(false);
                holder.quantity.setEnabled(false);
                holder.rate.setEnabled(false);
                holder.amount.setEnabled(false);


                holder.spinner_material_bought_adap1.setVisibility(View.GONE);
                holder.material.setVisibility(View.VISIBLE);

                String mid = m_bought.getPKMaterialBoughtId();
               // Helper.ShowToast(context, mid);
/*

                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please Wait");
*/

                holder.btn_mb.setVisibility(View.VISIBLE);
                holder.btn_mb1.setVisibility(View.GONE);
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


        Log.v("respooo", "" + object);
        PKMaterialBoughtOutId = object.getString("PKMaterialBoughtOutId");
      //  strings.add(object.getString("Name"));
        arrayAdapter.notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText quantity, rate;
        Button btn_mb, btn_mb1;

        Spinner spinner_material_bought_adap1;
        TextView material, amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.et_material_bought_quantity_adap);
            spinner_material_bought_adap1 = itemView.findViewById(R.id.spinner_material_bought_adap1);
            rate = itemView.findViewById(R.id.et_material_bought_rate_adap);
            amount = itemView.findViewById(R.id.et_materila_bought_amount_adap);
            material = itemView.findViewById(R.id.spinner_material_bought_adap);
            btn_mb = itemView.findViewById(R.id.btn_mb);
            btn_mb1 = itemView.findViewById(R.id.btn_mb1);


            arrayAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, strings);
            spinner_material_bought_adap1.setAdapter(arrayAdapter);
            spinner_material_bought_adap1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Spinner_Pos = String.valueOf(spinner_material_bought_adap1.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
                String spi_val=MyPrecfence.getActiveInstance(context).getMatterialBought();
            try {
                JSONObject jsonObject = new JSONObject(spi_val);
                JSONArray array = jsonObject.getJSONArray("Data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Log.v("dsfsdfsdfs",""+object);
                    SpinnerId = object.getString("PKMaterialBoughtOutId");
                    strings.add(object.getString("Name"));
                }
                arrayAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }



            TextWatcher autoAddTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    a = TextUtils.isEmpty(quantity.getText().toString()) ? 0 : Integer.parseInt(quantity.getText().toString());
                    b = TextUtils.isEmpty(rate.getText().toString()) ? 0 : Integer.parseInt(rate.getText().toString());

                    String amcalculation = String.valueOf(a * b);
                    amount.setText(amcalculation);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            quantity.addTextChangedListener(autoAddTextWatcher);
            rate.addTextChangedListener(autoAddTextWatcher);

        }

        public void savedata(String mid) {
            final String quantityy = quantity.getText().toString();
            final String ratee = rate.getText().toString();
            final String amountt = amount.getText().toString();
            Date d = new Date();
            String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
            HashMap<String, String> param = new HashMap<>();
            param.put("PKMaterialBoughtOutId", PKMaterialBoughtOutId);
             Log.v("spinpos", PKMaterialBoughtOutId);


            SharedPreferences preferences = context.getSharedPreferences("IdManager", 0);
            param.put("PKProjectId", preferences.getString("project_id", null));
            param.put("PKEmployeeId", preferences.getString("emp_id", null));


            param.put("Quantity", quantityy);
            param.put("Amount", amountt);
            param.put("PKMaterialBoughtId",mid);

            Log.v("hdjhdsgh",mid);
             param.put("Rate", ratee);
            param.put("Date", Date);

            server.requestServer(Utils.MaterialBoughtUrl, param, "", new ServerLis() {
                @Override
                public void onSuccess(String response) {
                  //  progressDialog.dismiss();
                    Log.v("bought", response);
                    Toast.makeText(context, "Data Successfully Saved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(VolleyError error) {
                    Helper.ShowToast(context, "oops-Some Error?");
                }
            });
        }
    }
}
