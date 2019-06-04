package com.kv.dailyaccountbook.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.kv.dailyaccountbook.AddClientActivity;
import com.kv.dailyaccountbook.R;
import com.kv.dailyaccountbook.User;
import com.kv.dailyaccountbook.db.DBManager;
import com.kv.dailyaccountbook.db.DatabaseHelper;
import com.kv.dailyaccountbook.transaction.AllTransactionActivity;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    AppCompatActivity activity;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rv_client)
    RecyclerView rvClient;
    DBManager dbManager;
    @BindView(R.id.tv_trans)
    TextView tvTrans;
    private CustomAdapter customAdapter;
    ArrayList<User> userArrayList = new ArrayList<>();
    public static int REQUEST_ADD = 1;
    public static int REQUEST_VIEW = 2;
    public static int RESULT_DELETE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setBodyUI();
    }

    private void setBodyUI() {
        activity = this;
        dbManager = new DBManager(this);
        dbManager.open();
        setSupportActionBar(toolbar);
        rvClient.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        userArrayList = getMyCustomer();
        customAdapter = new CustomAdapter(activity, userArrayList);

        rvClient.setAdapter(customAdapter);
    }

    private ArrayList<User> getMyCustomer() {
        ArrayList<User> temp = new ArrayList<>();
        Cursor cursor = dbManager.fetch();
        while (!cursor.isAfterLast()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID)));
            user.setFname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FNAME)));
            user.setLname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.LNAME)));
            temp.add(user);
            cursor.moveToNext();
        }

        return temp;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.fab, R.id.tv_trans, R.id.tv_credit, R.id.tv_debit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startActivityForResult(new Intent(this, AddClientActivity.class), REQUEST_ADD);
                break;
            case R.id.tv_trans:
                startActivity(new Intent(activity, AllTransactionActivity.class).putExtra("type", "all"));
                break;
            case R.id.tv_credit:
                startActivity(new Intent(activity, AllTransactionActivity.class).putExtra("type", "credit"));
                break;
            case R.id.tv_debit:
                startActivity(new Intent(activity, AllTransactionActivity.class).putExtra("type", "debit"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            int id = data.getIntExtra("id", 0);
            if (id != 0) {
                User user = new User();
                user.setId(id);
                user.setFname(data.getStringExtra("fname"));
                user.setLname(data.getStringExtra("lname"));
                userArrayList.add(user);
                customAdapter.notifyItemInserted(userArrayList.size());
            }
        } else if (requestCode == REQUEST_VIEW && resultCode == RESULT_DELETE) {
            userArrayList.clear();
            userArrayList = getMyCustomer();
            customAdapter = new CustomAdapter(activity, userArrayList);
            rvClient.setAdapter(customAdapter);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
