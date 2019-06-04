package com.kv.dailyaccountbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kv.dailyaccountbook.db.DBManager;
import com.kv.dailyaccountbook.db.DatabaseHelper;
import com.kv.dailyaccountbook.transaction.MyAdapter;
import com.kv.dailyaccountbook.transaction.TransModel;
import com.kv.dailyaccountbook.util.KeyboardHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kv.dailyaccountbook.home.HomeActivity.RESULT_DELETE;
import static com.kv.dailyaccountbook.util.Utils.showToast;

public class ViewCustomerDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    AppCompatActivity activity;
    DBManager dbManager;
    int id;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_expand)
    TextView tvExpand;
    @BindView(R.id.tv_amt)
    TextView tvAmt;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_dt)
    TextView tvDt;
    @BindView(R.id.rv_client)
    RecyclerView rvClient;
    private User user;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_shop)
    TextView tvShop;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.tv_nametxt)
    TextView tvNametxt;
    @BindView(R.id.iv_delete)
    ImageButton ivDelete;
    @BindView(R.id.add_txt)
    TextView addTxt;
    @BindView(R.id.iv_edit)
    ImageButton ivEdit;
    @BindView(R.id.et_amount)
    TextInputEditText etAmount;
    @BindView(R.id.il_amount)
    TextInputLayout ilAmount;
    @BindView(R.id.sp_payment_type)
    Spinner spPaymentType;
    @BindView(R.id.sp_type)
    Spinner spType;
    @BindView(R.id.et_summery)
    TextInputEditText etSummery;
    @BindView(R.id.il_summery)
    TextInputLayout ilSummery;
    @BindView(R.id.bt_save)
    Button btSave;
    @BindView(R.id.ll_middle)
    LinearLayout llMiddle;
    MyAdapter myAdapter;
    ArrayList<TransModel> arrayListtrans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer_details);
        ButterKnife.bind(this);
        setBodyUI();
    }

    private void setBodyUI() {
        dbManager = new DBManager(this);
        dbManager.open();
        activity = this;
        tvTitle.setText("View Customer");
        id = getIntent().getIntExtra("id", 0);
        user = getCustomerDetails(id);
        tvName.setText(user.getFname() + " " + user.getLname());
        tvMobile.setText(user.getMobile());
        tvShop.setText(user.getShop());
        tvAddress.setText(user.getAddress());

        rvClient.setHasFixedSize(true);
        rvClient.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(activity, arrayListtrans,1);
        rvClient.setAdapter(myAdapter);
        getAllTransaction(id + "");
        myAdapter.notifyDataSetChanged();
    }

    private ArrayList<TransModel> getAllTransaction(String s) {
        Cursor cursor = dbManager.getTrans(s);
        while (!cursor.isAfterLast()) {
            TransModel transModel = new TransModel();

            transModel.setUser_id("");
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

    private User getCustomerDetails(int id) {
        Cursor cursor = dbManager.getCustomerDetails(id);
        User user = new User();

        while (!cursor.isAfterLast()) {
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper._ID))));
            user.setFname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FNAME)));
            user.setLname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.LNAME)));
            user.setMobile(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOBILE)));
            user.setShop(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHOP)));
            user.setAddress(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS)));
            cursor.moveToNext();
        }
        return user;
    }

    @OnClick({R.id.iv_back, R.id.iv_edit, R.id.iv_delete, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_edit:
                Intent i = new Intent(this, AddClientActivity.class);
                i.putExtra("data", user);
                startActivity(i);
                finish();
                break;
            case R.id.iv_delete:
                deleteConfirm();
                break;
            case R.id.bt_save:
                saveEntry();
                break;
        }
    }

    private void saveEntry() {
        String amount = etAmount.getText().toString();
        String type = spType.getSelectedItemPosition() + "";
        String paymentType = spPaymentType.getSelectedItemPosition() + "";
        String summery = etSummery.getText().toString();
        if (amount.length() == 0) {
            etAmount.requestFocus();
            KeyboardHandler.showKeyboard(activity);
            showToast(activity, "Enter a valid Amount");
        } else {
            int status = dbManager.saveEntry(id, amount, type, paymentType, summery, getDate());
            if (status != 0) {
                showToast(activity, "Entry Added");

                dbManager.close();
                setResult(RESULT_OK);
                finish();
            } else {
                showToast(activity, "Something went wrong");
            }
        }
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");// HH:mm:ss
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void deleteConfirm() {
        new AlertDialog.Builder(this)
                .setTitle("Do you really want to delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (dbManager.deactivateCustomer(id) == 1) {
                            Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_DELETE);
                            finish();
                        } else {
                            Toast.makeText(activity, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        setResult(RESULT_DELETE, data);
    }
}
