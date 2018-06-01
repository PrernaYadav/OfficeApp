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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.activity.PettyCash;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;
import software.starling.com.officeapp.constant.Server;
import software.starling.com.officeapp.constant.ServerLis;
import software.starling.com.officeapp.constant.Utils;
import software.starling.com.officeapp.model.Pretty_cash;

/**
 * Created by Admin on 4/24/2018.
 */

public class PrettyCash extends RecyclerView.Adapter<PrettyCash.MyViewHolder> implements ServerLis {
    ArrayList<Pretty_cash> arrayList;
    Context context;
    Server server;
    Helper helper;
    long amreceived, ampaid, ambalanec;

    public PrettyCash(Context context, ArrayList<Pretty_cash> arrayList) {
        this.arrayList = arrayList;
        this.context=context;

        server = new Server((Activity) context);
        server.registerListener((ServerLis) context);
        helper=new Helper();
    }

    @Override
    public PrettyCash.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pretty_cash_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PrettyCash.MyViewHolder holder, int position) {
       final Pretty_cash cash = arrayList.get(position);
        holder.description.setText(cash.getDescription());
        holder.recvied.setText(cash.getRecv());
        holder.paid.setText(cash.getPaid());
        holder.blance.setText(cash.getBlance());


        holder.description.setEnabled(false);
        holder.recvied.setEnabled(false);
        holder.paid.setEnabled(false);
        holder.blance.setEnabled(false);

        holder.btn_pc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                String mid = cash.getPKPettyCashId();
              //
                //  Helper.ShowToast(context, mid);

                holder.description.setEnabled(true);
                holder.recvied.setEnabled(true);
                holder.paid.setEnabled(true);
                holder.blance.setEnabled(true);

                holder.btn_pc.setVisibility(View.GONE);
                holder.btn_pc1.setVisibility(View.VISIBLE);

            }
        });
        holder.btn_pc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mid = cash.getPKPettyCashId();
              //  Helper.ShowToast(context, mid);

                holder.btn_pc.setVisibility(View.VISIBLE);
                holder.btn_pc1.setVisibility(View.GONE);
                holder.saveToServer(mid);

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
        EditText description, recvied, paid;
        TextView blance;
        Button btn_pc,btn_pc1;

        public MyViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.et_description_petty_adap);
            recvied = itemView.findViewById(R.id.et_received_petty_adap);
            paid = itemView.findViewById(R.id.et_paid_petty_adap);
            blance = itemView.findViewById(R.id.et_balance_petty_adap);
            btn_pc = itemView.findViewById(R.id.btn_pc);
            btn_pc1 = itemView.findViewById(R.id.btn_pc1);


            TextWatcher autoAddTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ampaid = TextUtils.isEmpty(paid.getText().toString()) ? 0 : Integer.parseInt(paid.getText().toString());
                    // ambalanec = TextUtils.isEmpty(et_balance_petty.getText().toString()) ? 0 : Integer.parseInt(et_balance_petty.getText().toString());
                    amreceived = TextUtils.isEmpty(recvied.getText().toString()) ? 0 : Integer.parseInt(recvied.getText().toString());

                    String amcalculation = String.valueOf(amreceived - ampaid);
                    blance.setText(amcalculation);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            recvied.addTextChangedListener(autoAddTextWatcher);
            paid.addTextChangedListener(autoAddTextWatcher);

        }

        public void saveToServer(String mid) {
            final String descrip = description.getText().toString();
            final String recv = recvied.getText().toString();
            final String paiid = paid.getText().toString();
            final String blancee = blance.getText().toString();
            HashMap<String, String> param = new HashMap<>();

            Date d = new Date();
            String Date = String.valueOf(DateFormat.format("MM-dd-yyyy", d.getTime()));


            SharedPreferences preferences = context.getSharedPreferences("IdManager", 0);
            param.put("PKProjectId", preferences.getString("project_id", null));
            param.put("PKEmployeeId", preferences.getString("emp_id", null));

            param.put("PKWorkId", MyPrecfence.getActiveInstance(context).getWorkID());


            param.put("PKPettyCashId",mid);
            param.put("Description",descrip);
            param.put("Received", recv);
            param.put("Paid", paiid);
            param.put("Balance", blancee);
            param.put("Date", Date);

            Log.v("iddddddddd",mid);
            server.requestServer(Utils.PettyCashUrl, param, "", new ServerLis() {
                @Override
                public void onSuccess(String response) {
                    Log.v("prettycash", response);

                    Toast.makeText(context, "Data Successfully Saved", Toast.LENGTH_SHORT).show();                }

                @Override
                public void onError(VolleyError error) {

                }
            });

        }
    }
}
