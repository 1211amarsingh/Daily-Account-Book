package com.kv.dailyaccountbook.transaction;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.kv.dailyaccountbook.R;
import com.kv.dailyaccountbook.db.DBManager;
import com.kv.dailyaccountbook.db.DatabaseHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllTransactionActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_client)
    RecyclerView rvClient;
    AppCompatActivity activity;
    MyAdapter myAdapter;
    ArrayList<TransModel> arrayListtrans = new ArrayList<>();
    private DBManager dbManager;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transaction);
        ButterKnife.bind(this);
        setBodyUI();

    }

    private void setBodyUI() {
        activity = this;
        dbManager = new DBManager(this);
        dbManager.open();
        type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("all")){
            tvTitle.setText("All Transaction");
        }else if (type.equalsIgnoreCase("credit")){
            tvTitle.setText("All Credits");
        }else {
            tvTitle.setText("All Debits");
        }


        rvClient.setHasFixedSize(true);
        rvClient.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(activity, arrayListtrans,0);
        rvClient.setAdapter(myAdapter);
        getAllTransaction(type);
        myAdapter.notifyDataSetChanged();
    }

    private ArrayList<TransModel> getAllTransaction(String s) {
        Cursor cursor = dbManager.getTrans(s);
        while (!cursor.isAfterLast()) {
            TransModel transModel = new TransModel();

            transModel.setUser_id(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FNAME))
                    + " " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.LNAME)));
            transModel.setAmount(cursor.getString(cursor.getColumnIndex(DatabaseHelper.AMOUNT)));
            transModel.setType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TYPE)));
            transModel.setPaymentType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAYMENT_TYPE)));
            transModel.setDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE)));
            transModel.setSummery(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SUMMERY)));
            arrayListtrans.add(transModel);
            cursor.moveToNext();
        }

        return arrayListtrans;
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
