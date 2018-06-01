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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.activity.HindranceAndInternalProblem;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.model.Hidrance;

/**
 * Created by Admin on 4/24/2018.
 */

public class Hidrance_Adapter extends RecyclerView.Adapter<Hidrance_Adapter.MyViewHolder> implements ServerLis {

    Context context;
    Server server;
    ArrayList<Hidrance> arrayList;
    ProgressDialog progressDialog;

    public Hidrance_Adapter(Context context, ArrayList<Hidrance> arrayList) {
        this.arrayList = arrayList;
        this.context = context;

        server = new Server((Activity) context);
        server.registerListener((ServerLis) context);
    }

    @Override
    public Hidrance_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hidrance_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Hidrance_Adapter.MyViewHolder holder, int position) {
        final Hidrance hidrance = arrayList.get(position);
        holder.et_hindrance_from_custumer.setText(hidrance.getCustomer());
        holder.et_internal_issue.setText(hidrance.getIssues());

        holder.et_hindrance_from_custumer.setEnabled(false);
        holder.et_internal_issue.setEnabled(false);
        holder.btn_hp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id=hidrance.getPKProblemsId();
             //   Helper.ShowToast(context,""+id);

                holder.et_hindrance_from_custumer.setEnabled(true);
                holder.et_internal_issue.setEnabled(true);

                holder.btn_hp.setVisibility(View.GONE);
                holder.btn_hp1.setVisibility(View.VISIBLE);
            }
        });
        holder.btn_hp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id=hidrance.getPKProblemsId();
              //  Helper.ShowToast(context,""+id);

                holder.et_hindrance_from_custumer.setEnabled(false);
                holder.et_internal_issue.setEnabled(false);

               /* progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Conecting To Server");*/

                holder.btn_hp.setVisibility(View.VISIBLE);
                holder.btn_hp1.setVisibility(View.GONE);
                holder.savedata(id);
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
        EditText et_hindrance_from_custumer, et_internal_issue;
        Button btn_hp, btn_hp1;

        public MyViewHolder(View itemView) {
            super(itemView);
            et_internal_issue = itemView.findViewById(R.id.et_internal_issue_adap);
            et_hindrance_from_custumer = itemView.findViewById(R.id.et_hindrance_from_custumer_adap);
            btn_hp = itemView.findViewById(R.id.btn_hp);
            btn_hp1 = itemView.findViewById(R.id.btn_hp1);
        }

        public void savedata(String id) {
            Date d = new Date();
            final String hirdrance = et_hindrance_from_custumer.getText().toString();
            final String internal = et_internal_issue.getText().toString();

            String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));
            final HashMap<String, String> param = new HashMap<>();
            param.put("PKWorkId", MyPrecfence.getActiveInstance(context).getWorkID());
            SharedPreferences preferences = context.getSharedPreferences("IdManager", 0);
            param.put("PKProjectId", preferences.getString("project_id", null));
            param.put("PKEmployeeId", preferences.getString("emp_id", null));
            param.put("FromCustomer", hirdrance);
            param.put("InternalProblem", internal);
            param.put("PKProblemsId",id);
            Log.v("dhfgjhdgfx",id);
            param.put("Date", Date);


      /*  btn_save_hindranceproblem.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        arrayList.add(new Hidrance(hirdrance, internal));
        adapter.notifyDataSetChanged();
        clear();*/

            server.requestServer(Utils.EmployeeProblemsURL, param, "", new ServerLis() {
                @Override
                public void onSuccess(String response) {
                   // progressDialog.dismiss();
                    Log.v("respoo", response);
                    Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(VolleyError error) {

                }
            });

        }
    }
}
