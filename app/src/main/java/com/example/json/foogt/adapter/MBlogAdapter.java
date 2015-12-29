package com.example.json.foogt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.json.foogt.R;
import com.example.json.foogt.entity.BlogInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Mzz on 2015/12/28.
 */
public class MBlogAdapter extends RecyclerView.Adapter<MBlogAdapter.ViewHolder> {
    private ArrayList<BlogInfo> list;

    public MBlogAdapter(ArrayList<BlogInfo> list) {
        super();
        this.list = list;
    }

    @Override
    public MBlogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blog, parent, false);
        // TODO: 2015/12/28 what if parent is null , how to measure
        return new MBlogAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MBlogAdapter.ViewHolder holder, int position) {
        holder.username.setText(list.get(position).getUsername());
        holder.postTime.setText(new SimpleDateFormat("MM.dd hh:mm").format(list.get(position).getPostTime()));
        holder.msg.setText(list.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView postTime;
        public TextView msg;
        // TODO: 2015/12/28 headImg

        public ViewHolder(View itemView) {
            super(itemView);
            // TODO: 2015/12/28 complete
            username = (TextView) itemView.findViewById(R.id.txt_blog_name);
            postTime = (TextView) itemView.findViewById(R.id.txt_blog_time);
            msg = (TextView) itemView.findViewById(R.id.txt_blog_msg);
        }
    }
}
