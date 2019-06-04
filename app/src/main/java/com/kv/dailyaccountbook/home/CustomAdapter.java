package com.kv.dailyaccountbook.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kv.dailyaccountbook.R;
import com.kv.dailyaccountbook.User;
import com.kv.dailyaccountbook.ViewCustomerDetailsActivity;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Random random = new Random();
    private AppCompatActivity activity;
    private ArrayList<User> myCustomer;

    CustomAdapter(AppCompatActivity activity, ArrayList<User> myCustomer) {
        this.activity = activity;
        this.myCustomer = myCustomer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        setRandomColor(viewHolder);
        String name = myCustomer.get(i).getFname() + " " + myCustomer.get(i).getLname();
        String title = myCustomer.get(i).getFname().charAt(0) + "" + myCustomer.get(i).getLname().charAt(0);
        viewHolder.tvName.setText(name);
        viewHolder.tvNameTitle.setText(title);
        viewHolder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ViewCustomerDetailsActivity.class);
                i.putExtra("id", myCustomer.get(viewHolder.getAdapterPosition()).getId());

                activity.startActivityForResult(i,HomeActivity.REQUEST_VIEW);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myCustomer.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_title)
        AppCompatTextView tvNameTitle;
        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        @BindView(R.id.cv_item)
        CardView cvItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void setRandomColor(ViewHolder viewHolder) {
        int randomColor = arr_colors[random.nextInt(arr_colors.length)];
        viewHolder.tvNameTitle.setBackgroundTintList((ColorStateList.valueOf(activity.getResources().getColor(randomColor))));
    }

    private int[] arr_colors = {R.color.orange, R.color.grey, R.color.purple, R.color.blue, R.color.red,
            R.color.green, R.color.lblue, R.color.lgreen, R.color.yellow, R.color.pink};
}