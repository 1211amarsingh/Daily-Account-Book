package com.kv.dailyaccountbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kv.dailyaccountbook.db.DBManager;
import com.kv.dailyaccountbook.util.KeyboardHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kv.dailyaccountbook.db.DBManager.isMobileExist;
import static com.kv.dailyaccountbook.util.Utils.showToast;

public class AddClientActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_fname)
    TextInputEditText etFname;
    @BindView(R.id.et_lname)
    TextInputEditText etLname;
    @BindView(R.id.et_mobile)
    TextInputEditText etMobile;
    @BindView(R.id.et_shop_name)
    TextInputEditText etShopName;
    @BindView(R.id.et_address)
    TextInputEditText etAddress;
    @BindView(R.id.bt_submit)
    Button btSubmit;
    DBManager dbManager;
    private AppCompatActivity activity;
    boolean update;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        ButterKnife.bind(this);

        setBodyUI();
    }

    private void setBodyUI() {
        activity = this;
        if (getIntent().getSerializableExtra("data") == null) {
            tvTitle.setText("Add Customer");
        } else {
            tvTitle.setText("Edit Customer");
            User user = (User) getIntent().getSerializableExtra("data");
            update = true;
            etFname.setText(user.getFname());
            etLname.setText(user.getLname());
            etMobile.setText(user.getMobile());
            etShopName.setText(user.getShop());
            etAddress.setText(user.getAddress());
            id = user.getId();
        }
        dbManager = new DBManager(this);
        dbManager.open();
    }

    @OnClick({R.id.iv_back, R.id.bt_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.bt_submit:
                saveCustomer();
                break;
        }
    }

    private void saveCustomer() {
        String fname = etFname.getText().toString().trim();
        String lname = etLname.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String shop = etShopName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        if (fname.length() < 2) {
            etFname.requestFocus();
            KeyboardHandler.showKeyboard(activity);
            showToast(activity, "Enter First Name");
        } else if (lname.length() < 2) {
            etLname.requestFocus();
            KeyboardHandler.showKeyboard(activity);
            showToast(activity, "Enter Last Name");
        } else if (mobile.length() < 10) {
            etMobile.requestFocus();
            KeyboardHandler.showKeyboard(activity);
            showToast(activity, "Enter a valid Number");
        }
        if (update) {
            int status = dbManager.updateCustomer(id, fname, lname, shop, address, mobile);
            if (status == 1) {
                setResult(RESULT_OK);
                finish();
            } else {
                showToast(activity, "Something went wrong");
            }

        } else {
            if (isMobileExist(mobile) > 0) {
                showToast(activity, "Number Already Exist");
            } else {
                id = dbManager.insert(fname, lname, shop, address, mobile);

                if (id != 0) {
                    dbManager.close();
                    Intent i = new Intent();
                    i.putExtra("id", id);
                    i.putExtra("fname", fname);
                    i.putExtra("lname", lname);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    showToast(activity, "Something went wrong");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
