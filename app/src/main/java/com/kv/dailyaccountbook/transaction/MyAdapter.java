package com.kv.dailyaccountbook.transaction;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kv.dailyaccountbook.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppCompatActivity activity;
    private ArrayList<TransModel> arrayListtrans;
    private int type;

    public MyAdapter(AppCompatActivity activity, ArrayList<TransModel> arrayListtrans, int type) {
        this.activity = activity;
        this.arrayListtrans = arrayListtrans;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new MyHolder1(LayoutInflater.from(activity).inflate(R.layout.row_layout_trans, viewGroup, false));
        } else {
            return new MyHolder2(LayoutInflater.from(activity).inflate(R.layout.row_layout_trans_single, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == 0) {

            MyHolder1 myHolder = (MyHolder1) viewHolder;
            myHolder.tvName.setText(arrayListtrans.get(i).getUser_id());
            myHolder.tvAmt.setText(arrayListtrans.get(i).getAmount());
            myHolder.tvDt.setText(arrayListtrans.get(i).getDate());

            switch (Integer.parseInt(arrayListtrans.get(i).getPaymentType())) {
                case 0:
                    myHolder.tvType.setText("Cash");
                    break;
                case 1:
                    myHolder.tvType.setText("Cheque");
                    break;
                case 2:
                    myHolder.tvType.setText("Card");
                    break;
                case 3:
                    myHolder.tvType.setText("Other");
                    break;
            }

            if (Integer.parseInt(arrayListtrans.get(i).getType()) == 0) {
                myHolder.tvAmt.setTextColor(activity.getResources().getColor(R.color.green));
            } else {//Debit
                myHolder.tvAmt.setTextColor(activity.getResources().getColor(R.color.red));
            }
        } else {
            MyHolder2 myHolder = (MyHolder2) viewHolder;
            myHolder.tvAmt.setText(arrayListtrans.get(i).getAmount());
            myHolder.tvDt.setText(arrayListtrans.get(i).getDate());

            switch (Integer.parseInt(arrayListtrans.get(i).getPaymentType())) {
                case 0:
                    myHolder.tvType.setText("Cash");
                    break;
                case 1:
                    myHolder.tvType.setText("Cheque");
                    break;
                case 2:
                    myHolder.tvType.setText("Card");
                    break;
                case 3:
                    myHolder.tvType.setText("Other");
                    break;
            }

            if (Integer.parseInt(arrayListtrans.get(i).getType()) == 0) {
                myHolder.tvAmt.setTextColor(activity.getResources().getColor(R.color.green));
            } else {//Debit
                myHolder.tvAmt.setTextColor(activity.getResources().getColor(R.color.red));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public int getItemCount() {
        return arrayListtrans.size();
    }

    public class MyHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_amt)
        TextView tvAmt;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_dt)
        TextView tvDt;

        public MyHolder1(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MyHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_amt)
        TextView tvAmt;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_dt)
        TextView tvDt;

        public MyHolder2(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
