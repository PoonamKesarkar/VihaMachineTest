package com.vihatest.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vihatest.R;
import com.vihatest.model.JobPostData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

class JobAdapter  extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private ArrayList<JobPostData> list;
    private Context context;
    public JobAdapter(Context context, ArrayList<JobPostData> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(contentView);

    }

    @NonNull
    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        String[] array = context.getResources().getStringArray(R.array.profile_array);
        holder.txtJobTitle.setText(list.get(i).getJobtitle());
        holder.txtProfile.setText(array[list.get(i).getProfiletype()]);
        holder.textLocation.setText(list.get(i).getLocation());
        holder.txtSalary.setText(list.get(i).getSalary());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtJobTitle)
        TextView txtJobTitle;
        @BindView(R.id.txtProfile)
        TextView txtProfile;
        @BindView(R.id.textLocation)
        TextView textLocation;
        @BindView(R.id.txtSalary)
        TextView txtSalary;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
